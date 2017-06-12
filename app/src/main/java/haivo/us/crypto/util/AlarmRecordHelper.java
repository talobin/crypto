package haivo.us.crypto.util;

import android.content.Context;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.R;
import haivo.us.crypto.model.CurrencySubunit;

public class AlarmRecordHelper {
    public static final int ALARM_TYPE_GREATER_THAN = 6;
    public static final int ALARM_TYPE_LOWER_THAN = 7;
    public static final int ALARM_TYPE_PERCENT_CHANGE = 0;
    public static final int ALARM_TYPE_PERCENT_CHANGE_DOWN = 2;
    public static final int ALARM_TYPE_PERCENT_CHANGE_UP = 1;
    public static final int ALARM_TYPE_VALUE_CHANGE = 3;
    public static final int ALARM_TYPE_VALUE_CHANGE_DOWN = 5;
    public static final int ALARM_TYPE_VALUE_CHANGE_UP = 4;

    public static AlarmRecord generateDefaultAlarmRecord(boolean enabled) {
        AlarmRecord alarmRecord = new AlarmRecord();
        alarmRecord.setEnabled(enabled);
        alarmRecord.setValue(3.0d);
        alarmRecord.setSound(true);
        alarmRecord.setVibrate(true);
        alarmRecord.setLed(true);
        return alarmRecord;
    }

    public static int getPositionForAlarmType(Context context, AlarmRecord alarmRecord) {
        int[] alarmTypesValues = context.getResources().getIntArray(R.array.checker_add_alarm_types_values);
        if (alarmTypesValues != null) {
            for (int i = ALARM_TYPE_PERCENT_CHANGE; i < alarmTypesValues.length; i += ALARM_TYPE_PERCENT_CHANGE_UP) {
                if (((long) alarmTypesValues[i]) == alarmRecord.getType()) {
                    return i;
                }
            }
        }
        return ALARM_TYPE_PERCENT_CHANGE;
    }

    public static int getAlarmTypeForPosition(Context context, int position) {
        int[] alarmTypesValues = context.getResources().getIntArray(R.array.checker_add_alarm_types_values);
        if (alarmTypesValues == null || position <= 0 || position >= alarmTypesValues.length) {
            return ALARM_TYPE_PERCENT_CHANGE;
        }
        return alarmTypesValues[position];
    }

    public static String getPrefixForAlarmType(CheckerRecord checkerRecord, AlarmRecord alarmRecord) {
        switch ((int) alarmRecord.getType()) {
            case ALARM_TYPE_PERCENT_CHANGE_UP /*1*/:
            case ALARM_TYPE_VALUE_CHANGE_UP /*4*/:
                return "+";
            case ALARM_TYPE_PERCENT_CHANGE_DOWN /*2*/:
            case ALARM_TYPE_VALUE_CHANGE_DOWN /*5*/:
                return "-";
            case ALARM_TYPE_GREATER_THAN /*6*/:
                return ">";
            case ALARM_TYPE_LOWER_THAN /*7*/:
                return "<";
            default:
                return "\u00b1";
        }
    }

    public static String getValueForAlarmType(CheckerRecord checkerRecord, AlarmRecord alarmRecord) {
        return getValueForAlarmType(CurrencyUtils.getCurrencySubunit(checkerRecord.getCurrencyDst(),
                                                                     checkerRecord.getCurrencySubunitDst()),
                                    alarmRecord);
    }

    public static double parseEnteredValueForAlarmType(CurrencySubunit subunit,
                                                       AlarmRecord alarmRecord,
                                                       double newValue) {
        switch ((int) alarmRecord.getType()) {
            case ALARM_TYPE_VALUE_CHANGE /*3*/:
            case ALARM_TYPE_VALUE_CHANGE_UP /*4*/:
            case ALARM_TYPE_VALUE_CHANGE_DOWN /*5*/:
            case ALARM_TYPE_GREATER_THAN /*6*/:
            case ALARM_TYPE_LOWER_THAN /*7*/:
                if (subunit != null) {
                    return newValue / ((double) subunit.subunitToUnit);
                }
                return newValue;
            default:
                return newValue;
        }
    }

    public static String getValueForAlarmType(CurrencySubunit subunitDst, AlarmRecord alarmRecord) {
        switch ((int) alarmRecord.getType()) {
            case ALARM_TYPE_VALUE_CHANGE /*3*/:
            case ALARM_TYPE_VALUE_CHANGE_UP /*4*/:
            case ALARM_TYPE_VALUE_CHANGE_DOWN /*5*/:
            case ALARM_TYPE_GREATER_THAN /*6*/:
            case ALARM_TYPE_LOWER_THAN /*7*/:
                return FormatUtilsBase.formatPriceValueForSubunit(alarmRecord.getValue(), subunitDst, false, true);
            default:
                return FormatUtilsBase.formatDoubleWithFiveMax(alarmRecord.getValue());
        }
    }

    public static String getSufixForAlarmType(CheckerRecord checkerRecord, AlarmRecord alarmRecord) {
        switch ((int) alarmRecord.getType()) {
            case ALARM_TYPE_VALUE_CHANGE /*3*/:
            case ALARM_TYPE_VALUE_CHANGE_UP /*4*/:
            case ALARM_TYPE_VALUE_CHANGE_DOWN /*5*/:
            case ALARM_TYPE_GREATER_THAN /*6*/:
            case ALARM_TYPE_LOWER_THAN /*7*/:
                return CurrencyUtils.getCurrencySymbol(CurrencyUtils.getCurrencySubunit(checkerRecord.getCurrencyDst(),
                                                                                        checkerRecord.getCurrencySubunitDst()).name);
            default:
                return "%";
        }
    }

    public static boolean isCheckPointAvailableForAlarmType(AlarmRecord alarmRecord) {
        switch ((int) alarmRecord.getType()) {
            case ALARM_TYPE_GREATER_THAN /*6*/:
            case ALARM_TYPE_LOWER_THAN /*7*/:
                return false;
            default:
                return true;
        }
    }

    public static boolean shouldDisableAlarmAfterDismiss(AlarmRecord alarmRecord) {
        int alarmTypeInt = (int) alarmRecord.getType();
        return alarmTypeInt == ALARM_TYPE_GREATER_THAN || alarmTypeInt == ALARM_TYPE_LOWER_THAN;
    }

    public static double getDifferenceForPercentageChange(double oldPrice, double newPrice) {
        if (oldPrice == 0.0d) {
            return 0.0d;
        }
        return ((newPrice - oldPrice) / oldPrice) * 100.0d;
    }

    public static double getTriggerPriceForPercentageChange(double checkpointPrice, double percents) {
        return ((percents / 100.0d) * checkpointPrice) + checkpointPrice;
    }
}
