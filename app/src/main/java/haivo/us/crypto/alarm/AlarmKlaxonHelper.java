package haivo.us.crypto.alarm;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.receiver.MarketChecker;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class AlarmKlaxonHelper {
    public static void startAlarmKlaxon(Context context, AlarmRecord alarmRecord) {
    }

    public static PendingIntent createAlarmKlaxonDismissPendingIntent(Context context,
                                                                      long checkerRecordId,
                                                                      long alarmRecordId) {
        return PendingIntent.getBroadcast(context,
                                          (int) alarmRecordId,
                                          createAlarmKlaxonDismissIntent(context, checkerRecordId, alarmRecordId),
                                          FLAG_UPDATE_CURRENT);
    }

    public static Intent createAlarmKlaxonDismissIntent(Context context, long checkerRecordId, long alarmRecordId) {
        Intent intent = new Intent(Alarms.ALARM_DISMISS_ACTION);
        intent.putExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, checkerRecordId);
        intent.putExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, alarmRecordId);
        return intent;
    }
}
