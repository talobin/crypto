package haivo.us.crypto.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.TextUtils;
import haivo.us.crypto.appwidget.WidgetProvider;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.content.MaindbContract.Checker;
import haivo.us.crypto.fragment.CheckersListFragment;
import haivo.us.crypto.mechanoid.db.ActiveRecord;
import haivo.us.crypto.mechanoid.db.SQuery;
import haivo.us.crypto.mechanoid.db.SQuery.Op;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.util.AsyncTaskCompat;
import haivo.us.crypto.util.CheckErrorsUtils;
import haivo.us.crypto.util.CheckerRecordHelper;
import haivo.us.crypto.util.HttpsHelper;
import haivo.us.crypto.util.NotificationUtils;
import haivo.us.crypto.util.TickerUtils;
import haivo.us.crypto.volley.CheckerErrorParsedError;
import haivo.us.crypto.volley.CheckerVolleyAsyncTask;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class MarketChecker extends BroadcastReceiver {
    private static final String ACTION_CHECK_MARKET = "haivo.us.crypto.receiver.action.check_market";
    public static final String EXTRA_ALARM_RECORD_ID = "alarm_record_id";
    public static final String EXTRA_CHECKER_RECORD_ID = "checker_record_id";
    private static final Object LOCK;
    private static RequestQueue requestQueue;

    /* renamed from: haivo.us.crypto.receiver.MarketChecker.1 */
    static class C02001 extends AsyncTask<Void, Void, Void> {
        final /* synthetic */ Context val$appContext;

        C02001(Context context) {
            this.val$appContext = context;
        }

        protected Void doInBackground(Void... unused) {
            Cursor cursor = SQuery.newQuery().select(Checker.CONTENT_URI, CheckerRecord.PROJECTION);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getInt(1) == 0 || !MarketChecker.isAlarmScheduledForChecker(this.val$appContext,
                                                                                               (long) cursor.getInt(0))) {
                            MarketChecker.resetAlarmManagerForChecker(this.val$appContext,
                                                                      CheckerRecord.fromCursor(cursor),
                                                                      true);
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
            return null;
        }
    }

    /* renamed from: haivo.us.crypto.receiver.MarketChecker.2 */
    static class C03432 implements Listener<Ticker> {
        final /* synthetic */ Context val$appContext;
        final /* synthetic */ CheckerRecord val$checkerRecord;

        C03432(Context context, CheckerRecord checkerRecord) {
            this.val$appContext = context;
            this.val$checkerRecord = checkerRecord;
        }

        public void onResponse(Ticker ticker) {
            MarketChecker.handleNewTicker(this.val$appContext, this.val$checkerRecord, ticker, null);
        }
    }

    /* renamed from: haivo.us.crypto.receiver.MarketChecker.3 */
    static class C03443 implements ErrorListener {
        final /* synthetic */ Context val$appContext;
        final /* synthetic */ CheckerRecord val$checkerRecord;

        C03443(Context context, CheckerRecord checkerRecord) {
            this.val$appContext = context;
            this.val$checkerRecord = checkerRecord;
        }

        public void onErrorResponse(VolleyError error) {
            error.printStackTrace();
            if (!(error instanceof NetworkError)) {
                String errorMsg;
                if (!(error instanceof CheckerErrorParsedError)
                    || TextUtils.isEmpty(((CheckerErrorParsedError) error).getErrorMsg())) {
                    errorMsg = CheckErrorsUtils.parseVolleyErrorMsg(this.val$appContext, error);
                } else {
                    errorMsg = ((CheckerErrorParsedError) error).getErrorMsg();
                }
                MarketChecker.handleNewTicker(this.val$appContext, this.val$checkerRecord, null, errorMsg);
            }
        }
    }

    static {
        requestQueue = null;
        LOCK = new Object();
    }

    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTION_CHECK_MARKET.equals(intent.getAction())) {
            long checkerRecordId = intent.getLongExtra(EXTRA_CHECKER_RECORD_ID, -1);
            if (checkerRecordId > 0) {
                checkMarketAsyncForCheckerRecord(context, CheckerRecord.get(checkerRecordId));
            }
        }
    }

    public static void refreshAllEnabledCheckerRecords(Context context) {
        for (ActiveRecord checkerRecord : SQuery.newQuery()
                                                .expr(MaindbContract.CheckerColumns.ENABLED, Op.EQ, true)
                                                .select(Checker.CONTENT_URI,
                                                        CheckersListFragment.getSortOrderString(context))) {
            checkMarketAsyncForCheckerRecord(context, (CheckerRecord) checkerRecord);
        }
    }

    public static void resetAlarmManager(Context context) {
        Context appContext = context.getApplicationContext();
        for (ActiveRecord checkerRecord : SQuery.newQuery().select(Checker.CONTENT_URI)) {
            resetAlarmManagerForChecker(appContext, (CheckerRecord) checkerRecord, true);
        }
    }

    public static void resetAlarmManagerForAllEnabledThatUseGlobalFrequency(Context context) {
        Context appContext = context.getApplicationContext();
        for (ActiveRecord checkerRecord : SQuery.newQuery()
                                                .expr(MaindbContract.CheckerColumns.FREQUENCY, Op.EQ, -1)
                                                .and()
                                                .expr(MaindbContract.CheckerColumns.ENABLED, Op.EQ, true)
                                                .select(Checker.CONTENT_URI)) {
            resetAlarmManagerForChecker(appContext, (CheckerRecord) checkerRecord, true);
        }
    }

    public static void resetAlarmManagerForChecker(Context context, CheckerRecord checkerRecord, boolean checkNow) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (checkerRecord.getEnabled()) {
            if (checkNow) {
                checkMarketAsyncForCheckerRecord(context, checkerRecord);
            }
            PendingIntent pi = createPendingIntentForMarket(context, checkerRecord.getId());
            alarmManager.cancel(pi);
            long frequency = CheckerRecordHelper.getDisplayedFrequency(context, checkerRecord);
            alarmManager.setInexactRepeating(2, SystemClock.elapsedRealtime() + frequency, frequency, pi);
            return;
        }
        cancelCheckingForCheckerRecord(checkerRecord.getId());
        alarmManager.cancel(createPendingIntentForMarket(context, checkerRecord.getId()));
    }

    private static Intent createIntentForCheckerRecord(Context context, long checkerRecordId) {
        Intent intent = new Intent(context, MarketChecker.class);
        intent.setAction(ACTION_CHECK_MARKET);
        intent.putExtra(EXTRA_CHECKER_RECORD_ID, checkerRecordId);
        intent.setData(Uri.parse(intent.toUri(1)));
        return intent;
    }

    private static PendingIntent createPendingIntentForMarket(Context context, long checkerRecordId) {
        return PendingIntent.getBroadcast(context,
                                          (int) checkerRecordId,
                                          createIntentForCheckerRecord(context, checkerRecordId),
                                          FLAG_UPDATE_CURRENT);
    }

    public static boolean isAlarmScheduledForChecker(Context context, long checkerRecordId) {
        return PendingIntent.getBroadcast(context,
                                          (int) checkerRecordId,
                                          createIntentForCheckerRecord(context, checkerRecordId),
                                          PendingIntent.FLAG_NO_CREATE) != null;
    }

    public static void checkIfAlarmsAreSet(Context context) {
        AsyncTaskCompat.execute(new C02001(context.getApplicationContext()), new Void[0]);
    }

    public static void cancelCheckingForCheckerRecord(long checkerRecordId) {
        CheckerVolleyAsyncTask.cancelCheckingForCheckerRecord(checkerRecordId);
    }

    public static void checkMarketAsyncForCheckerRecord(Context context, CheckerRecord checkerRecord) {
        if (checkerRecord != null && checkerRecord.getEnabled()) {
            synchronized (LOCK) {
                Context appContext = context.getApplicationContext();
                if (requestQueue == null) {
                    requestQueue = HttpsHelper.newRequestQueue(appContext);
                }
                AsyncTaskCompat.execute(new CheckerVolleyAsyncTask(requestQueue,
                                                                   checkerRecord,
                                                                   new C03432(appContext, checkerRecord),
                                                                   new C03443(appContext, checkerRecord)), new Void[0]);
            }
        }
    }

    private static void handleNewTicker(Context appContext,
                                        CheckerRecord checkerRecord,
                                        Ticker ticker,
                                        String errorMsg) {
        checkerRecord.setLastCheckDate(System.currentTimeMillis());
        checkerRecord.setErrorMsg(errorMsg);
        if (ticker != null) {
            checkerRecord.setPreviousCheckTicker(checkerRecord.getLastCheckTicker());
            checkerRecord.setLastCheckTicker(TickerUtils.toJson(ticker));
        }
        checkerRecord.save();
        NotificationUtils.showOngoingNotification(appContext,
                                                  checkerRecord,
                                                  NotificationUtils.checkfThereIsAlertSituationAndShowAlertNotification(
                                                      appContext,
                                                      checkerRecord,
                                                      ticker));
        WidgetProvider.refreshWidget(appContext);
    }
}
