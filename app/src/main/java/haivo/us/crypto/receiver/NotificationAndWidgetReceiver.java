package haivo.us.crypto.receiver;

import haivo.us.crypto.activity.CheckerAddActivity;
import haivo.us.crypto.alarm.AlarmKlaxonHelper;
import haivo.us.crypto.alarm.Alarms;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.mechanoid.Mechanoid;
import haivo.us.crypto.util.AlarmRecordHelper;
import haivo.us.crypto.util.NotificationUtils;

public class NotificationAndWidgetReceiver extends BroadcastReceiver {
    public static final String ACTION_NOTIFICATION_ALARM_DISMISS = "haivo.us.crypto.receiver.action.notification_alarm_dismiss";
    public static final String ACTION_NOTIFICATION_CHECKER_ALARM_DETAILS =
        "haivo.us.crypto.receiver.action.notification_checker_alarm_details";
    public static final String ACTION_NOTIFICATION_REFRESH = "haivo.us.crypto.receiver.action.notification_refresh";
    public static final String ACTION_NOTIFICATION_REFRESH_ALL = "haivo.us.crypto.receiver.action.notification_refresh_all";

    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_NOTIFICATION_REFRESH.equals(action)) {
                long checkerRecrdId = intent.getLongExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, -1);
                if (checkerRecrdId > 0) {
                    MarketChecker.checkMarketAsyncForCheckerRecord(context, CheckerRecord.get(checkerRecrdId));
                }
            } else if (ACTION_NOTIFICATION_REFRESH_ALL.equals(action)) {
                MarketChecker.refreshAllEnabledCheckerRecords(context);
            } else if (ACTION_NOTIFICATION_CHECKER_ALARM_DETAILS.equals(action)) {
                long checkerRecordId = intent.getLongExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, -1);
                long alarmRecordId = intent.getLongExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, -1);
                if (checkerRecordId > 0 && alarmRecordId > 0) {
                    disableAlarmRecordIfNeeded(alarmRecordId);
                    CheckerRecord checkerRecord = CheckerRecord.get(checkerRecordId);
                    if (checkerRecord != null) {
                        CheckerAddActivity.startCheckerAddActivity(context, checkerRecord, alarmRecordId, true);
                    }
                }
            } else if (ACTION_NOTIFICATION_ALARM_DISMISS.equals(action)) {
                long checkerRecordId = intent.getLongExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, -1);
                long alarmRecordId = intent.getLongExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, -1);
                if (checkerRecordId > 0 && alarmRecordId > 0) {
                    disableAlarmRecordIfNeeded(alarmRecordId);
                    context.sendBroadcast(AlarmKlaxonHelper.createAlarmKlaxonDismissIntent(context,
                                                                                           checkerRecordId,
                                                                                           alarmRecordId));
                }
            } else if (Alarms.ALARM_DISMISS_ACTION.equals(action)) {
                long alarmRecordId = intent.getLongExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, -1);
                if (alarmRecordId > 0) {
                    NotificationUtils.clearAlarmNotificationForAlarmRecord(context, alarmRecordId);
                    disableAlarmRecordIfNeeded(alarmRecordId);
                }
            } else if (Alarms.ALARM_DONE_ACTION.equals(action)) {
                long alarmRecordId = intent.getLongExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, -1);
                if (alarmRecordId > 0) {
                    NotificationUtils.clearAlarmNotificationForAlarmRecord(context, alarmRecordId);
                }
            }
        }
    }

    private boolean disableAlarmRecordIfNeeded(long alarmRecordId) {
        AlarmRecord alarmRecord = AlarmRecord.get(alarmRecordId);
        if (alarmRecord == null || !AlarmRecordHelper.shouldDisableAlarmAfterDismiss(alarmRecord)) {
            return false;
        }
        alarmRecord.setEnabled(false);
        try {
            alarmRecord.save(true);
            Mechanoid.getContentResolver().notifyChange(MaindbContract.Checker.CONTENT_URI, null);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }
}
