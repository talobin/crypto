package haivo.us.crypto.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.content.MaindbContract.Checker;
import haivo.us.crypto.fragment.CheckersListFragment;
import haivo.us.crypto.mechanoid.db.ActiveRecord;
import haivo.us.crypto.mechanoid.db.SQuery;
import haivo.us.crypto.mechanoid.db.SQuery.Op;
import haivo.us.crypto.util.PreferencesUtils;

public class MyHelperEventsReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            MarketChecker.resetAlarmManager(context);
        } else if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            try {
                NetworkInfo activeNetwork = (NetworkInfo) intent.getParcelableExtra("networkInfo");
                if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                    long now = System.currentTimeMillis();
                    for (ActiveRecord checkerRecord : SQuery.newQuery()
                                                            .expr(MaindbContract.CheckerColumns.ENABLED, Op.EQ, true)
                                                            .and()
                                                            .append("((lastCheckDate > "
                                                                        + Long.toString(now)
                                                                        + ") OR ("
                                                                        + MaindbContract.CheckerColumns.FREQUENCY
                                                                        + Op.EQ
                                                                        + -1
                                                                        + " AND "
                                                                        + MaindbContract.CheckerColumns.LAST_CHECK_DATE
                                                                        + Op.LT
                                                                        + Long.toString(now
                                                                                            - PreferencesUtils.getCheckGlobalFrequency(
                                                                context))
                                                                        + ") OR ("
                                                                        + MaindbContract.CheckerColumns.FREQUENCY
                                                                        + " <> "
                                                                        + -1
                                                                        + " AND "
                                                                        + MaindbContract.CheckerColumns.LAST_CHECK_DATE
                                                                        + Op.LT
                                                                        + Long.toString(now)
                                                                        + "-"
                                                                        + MaindbContract.CheckerColumns.FREQUENCY
                                                                        + "))", new String[0])
                                                            .select(Checker.CONTENT_URI,
                                                                    CheckersListFragment.getSortOrderString(context))) {
                        MarketChecker.checkMarketAsyncForCheckerRecord(context, (CheckerRecord) checkerRecord);
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
