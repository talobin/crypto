package haivo.us.crypto.util;

import android.content.Context;
import haivo.us.crypto.alarm.AlarmKlaxonHelper;
import haivo.us.crypto.appwidget.WidgetProvider;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.content.MaindbContract.Alarm;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.receiver.MarketChecker;
import haivo.us.crypto.mechanoid.db.SQuery;
import haivo.us.crypto.mechanoid.db.SQuery.Op;
import java.util.List;

public class CheckerRecordHelper {
    public static CheckerInfo createCheckerInfo(CheckerRecord checkerRecord) {
        return new CheckerInfo(checkerRecord.getCurrencySrc(), checkerRecord.getCurrencyDst(), checkerRecord.getCurrencyPairId(), (int) checkerRecord.getContractType());
    }

    public static final void doAfterEdit(Context context, CheckerRecord checkerRecord, boolean updateWidget) {
        MarketChecker.cancelCheckingForCheckerRecord(checkerRecord.getId());
        MarketChecker.resetAlarmManagerForChecker(context.getApplicationContext(), checkerRecord, true);
        if (!checkerRecord.getEnabled()) {
            NotificationUtils.clearNotificationsForCheckerRecord(context, checkerRecord);
        }
        if (updateWidget) {
            WidgetProvider.refreshWidget(context.getApplicationContext());
        }
        context.sendBroadcast(AlarmKlaxonHelper.createAlarmKlaxonDismissIntent(context, checkerRecord.getId(), -1));
    }

    public static final void doBeforeDelete(Context context, CheckerRecord checkerRecord) {
        checkerRecord.setEnabled(false);
        doAfterEdit(context, checkerRecord, false);
        Alarm.delete("checkerId = ?", new String[]{ String.valueOf(checkerRecord.getId())});
    }

    public static final void doAfterDelete(Context context, CheckerRecord checkerRecord) {
        WidgetProvider.refreshWidget(context.getApplicationContext());
    }

    public static List<AlarmRecord> getAlarmsForCheckerRecord(CheckerRecord checkerRecord, boolean enabledOnly) {
        return getAlarmsForCheckerRecord(checkerRecord.getId(), enabledOnly);
    }

    private static List<AlarmRecord> getAlarmsForCheckerRecord(long checkerRecordId, boolean enabledOnly) {
        SQuery sQuery = SQuery.newQuery().expr(MaindbContract.AlarmColumns.CHECKER_ID, Op.EQ, checkerRecordId);
        if (enabledOnly) {
            sQuery.and().expr(MaindbContract.CheckerColumns.ENABLED, Op.EQ, true);
        }
        return sQuery.select(Alarm.CONTENT_URI);
    }

    public static List<Long> getAlarmsIdsForCheckerRecord(long checkerRecordId) {
        return SQuery.newQuery().expr(MaindbContract.AlarmColumns.CHECKER_ID, Op.EQ, checkerRecordId).selectLongList(Alarm.CONTENT_URI, "_id");
    }

    public static long getDisplayedFrequency(Context context, CheckerRecord checkerRecord) {
        if (checkerRecord.getFrequency() <= -1) {
            return PreferencesUtils.getCheckGlobalFrequency(context);
        }
        return checkerRecord.getFrequency();
    }
}
