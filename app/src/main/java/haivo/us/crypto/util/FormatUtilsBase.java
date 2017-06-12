package haivo.us.crypto.util;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import haivo.us.crypto.model.CurrencySubunit;
import java.text.DecimalFormat;
import java.util.Date;

public class FormatUtilsBase {
    private static final DecimalFormat FORMAT_EIGHT_SIGNIFICANT_AT_MOST;
    private static final DecimalFormat FORMAT_FIVE_SIGNIFICANT_AT_MOST;
    private static final DecimalFormat FORMAT_TWO_DECIMAL;

    static {
        FORMAT_TWO_DECIMAL = new DecimalFormat("0.00");
        FORMAT_FIVE_SIGNIFICANT_AT_MOST = new DecimalFormat("@#####");
        FORMAT_EIGHT_SIGNIFICANT_AT_MOST = new DecimalFormat("@#######");
    }

    public static String formatDouble(double value, boolean isPrice) {
        return formatDouble(value < 1.0d ? FORMAT_FIVE_SIGNIFICANT_AT_MOST : FORMAT_TWO_DECIMAL, value);
    }

    public static String formatDoubleWithFiveMax(double value) {
        return formatDouble(FORMAT_FIVE_SIGNIFICANT_AT_MOST, value);
    }

    protected static String formatDoubleWithEightMax(double value) {
        return formatDouble(FORMAT_EIGHT_SIGNIFICANT_AT_MOST, value);
    }

    protected static final String formatDouble(DecimalFormat decimalFormat, double value) {
        String format;
        synchronized (decimalFormat) {
            try {
                format = decimalFormat.format(value);
            } catch (Exception e) {
                e.printStackTrace();
                format = String.valueOf(value);
            }
        }
        return format;
    }

    public static String formatPriceWithCurrency(double price, CurrencySubunit subunitDst) {
        return formatPriceWithCurrency(price, subunitDst, true);
    }

    protected static String formatPriceWithCurrency(double price, CurrencySubunit subunitDst, boolean showCurrencyDst) {
        String priceString = formatPriceValueForSubunit(price, subunitDst, false, false);
        if (showCurrencyDst) {
            return formatPriceWithCurrency(priceString, subunitDst.name);
        }
        return priceString;
    }

    public static String formatPriceWithCurrency(double value, String currency) {
        return formatPriceWithCurrency(formatDouble(value, true), currency);
    }

    public static String formatPriceWithCurrency(String priceString, String currency) {
        return priceString + CurrencyUtils.getCurrencySymbol(currency);
    }

    public static String formatPriceValueForSubunit(double price,
                                                    CurrencySubunit subunitDst,
                                                    boolean forceInteger,
                                                    boolean skipNoSignificantDecimal) {
        price *= (double) subunitDst.subunitToUnit;
        if (!subunitDst.allowDecimal || forceInteger) {
            return String.valueOf((long) (0.5d + price));
        }
        if (skipNoSignificantDecimal) {
            return formatDoubleWithEightMax(price);
        }
        return formatDouble(price, true);
    }

    public static String formatSameDayTimeOrDate(Context context, long time) {
        if (DateUtils.isToday(time)) {
            return DateFormat.getTimeFormat(context).format(new Date(time));
        }
        return DateFormat.getDateFormat(context).format(new Date(time));
    }

    public static String formatDateTime(Context context, long time) {
        Date date = new Date(time);
        return DateFormat.getTimeFormat(context).format(date) + ", " + DateFormat.getDateFormat(context).format(date);
    }
}
