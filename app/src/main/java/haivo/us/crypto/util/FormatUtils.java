package haivo.us.crypto.util;

import android.content.Context;
import android.content.res.Resources;
import haivo.us.crypto.R;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.model.CurrencySubunit;
import haivo.us.crypto.model.Futures;
import haivo.us.crypto.model.FuturesMarket;
import haivo.us.crypto.model.Market;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatUtils extends FormatUtilsBase {
    private static final int[] CONTRACT_TYPE_NAMES;

    public static BigDecimal fixSatoshi(double value) {
        return new BigDecimal(value).setScale(8, RoundingMode.HALF_UP);
    }

    public static String getCurrencySrcWithContractType(String currencySrc, Market market, int contractType) {
        if (!(market instanceof FuturesMarket)) {
            return currencySrc;
        }
        String contractTypeShortName = Futures.getContractTypeShortName(contractType);
        if (contractTypeShortName != null) {
            return currencySrc + contractTypeShortName;
        }
        return currencySrc;
    }

    public static String getCurrencySrcWithContractTypeForTTS(Context context, String currencySrc, Market market, int contractType) {
        if (!(market instanceof FuturesMarket)) {
            return currencySrc;
        }
        CharSequence contractTypeName = getContractTypeName(context, contractType);
        if (contractTypeName != null) {
            return currencySrc + " " + contractTypeName;
        }
        return currencySrc;
    }

    public static String formatPriceWithCurrency(double price, CheckerRecord checkerRecord) {
        return formatPrice(price, checkerRecord, true);
    }

    public static String formatPrice(double price, CheckerRecord checkerRecord, boolean showCurrencyDst) {
        return FormatUtilsBase.formatPriceWithCurrency(price, CurrencyUtils.getCurrencySubunit(checkerRecord.getCurrencyDst(), checkerRecord.getCurrencySubunitDst()), showCurrencyDst);
    }

    private static String formatValueForTTS(Context context, double value, CurrencySubunit subunitDst) {
        boolean speakIntegersOnly = value >= 1.0d && PreferencesUtils.getTTSFormatIntegerOnly(context);
        return FormatUtilsBase.formatPriceValueForSubunit(value, subunitDst, speakIntegersOnly, true);
    }

    public static String formatTextForTTS(Context context, double value, CheckerRecord checkerRecord, CurrencySubunit subunitDst, Market market) {
        return formatTextForTTS(context, value, checkerRecord, false, subunitDst, false, market, false);
    }

    public static String formatTextForTTS(Context context, double value, CheckerRecord checkerRecord, boolean skipCurrencySrc, CurrencySubunit subunitDst, boolean skipCurrencyDst, Market market, boolean skipExchangeName) {
        return formatTextForTTS(context, value, checkerRecord.getCurrencySrc(), (int) checkerRecord.getContractType(), skipCurrencySrc, subunitDst, skipCurrencyDst, market, market.ttsName, skipExchangeName);
    }

    public static String formatTextForTTS(Context context, double value, String currencySrc, int contractType, boolean skipCurrencySrc, CurrencySubunit subunitDst, boolean skipCurrencyDst, Market market, String exchangeName, boolean skipExchangeName) {
        String spokenString = formatValueForTTS(context, value, subunitDst);
        if (!skipCurrencySrc && PreferencesUtils.getTTSFormatSpeakBaseCurrency(context)) {
            spokenString = context.getString(R.string.tts_format_base_currency, new Object[]{getCurrencySrcWithContractTypeForTTS(context, currencySrc, market, contractType), spokenString});
        }
        if (!skipCurrencyDst && PreferencesUtils.getTTSFormatSpeakCounterCurrency(context)) {
            spokenString = context.getString(R.string.tts_format_counter_currency, new Object[]{spokenString, subunitDst.name});
        }
        if (skipExchangeName || !PreferencesUtils.getTTSFormatSpeakExchange(context)) {
            return spokenString;
        }
        return context.getString(R.string.tts_format_exchange, new Object[]{spokenString, exchangeName});
    }

    public static String formatRelativeTime(Context context, long time, long now, boolean useShortNames) {
        int timeValue;
        int pluralsResId;
        String relativeDate;
        Resources res = context.getResources();
        long ms = time - now;
        boolean isInPast = false;
        if (ms < 0) {
            isInPast = true;
            ms = -ms;
        }
        if (ms < TimeUtils.MILLIS_IN_MINUTE) {
            timeValue = (int) (((float) ms) / 1000.0f);
            if (useShortNames) {
                pluralsResId = R.string.time_s;
            } else {
                pluralsResId = R.plurals.time_seconds;
            }
        } else if (ms < TimeUtils.MILLIS_IN_HOUR) {
            timeValue = (int) (((float) ms) / 60000.0f);
            pluralsResId = useShortNames ? R.string.time_m : R.plurals.time_minutes;
        } else if (ms < TimeUtils.MILLIS_IN_DAY) {
            timeValue = (int) (((float) ms) / 3600000.0f);
            pluralsResId = useShortNames ? R.string.time_h : R.plurals.time_hours;
        } else if (ms < 2563200000L) {
            timeValue = (int) (((float) ms) / 8.64E7f);
            pluralsResId = useShortNames ? R.string.time_d : R.plurals.time_days;
        } else if (ms < 30758400000L) {
            timeValue = (int) (((float) ms) / 2.5632E9f);
            pluralsResId = useShortNames ? R.string.time_mth : R.plurals.time_months;
        } else {
            timeValue = (int) (((float) ms) / 3.07584E10f);
            pluralsResId = useShortNames ? R.string.time_y : R.plurals.time_years;
        }
        if (useShortNames) {
            relativeDate = timeValue + res.getString(pluralsResId);
        } else {
            relativeDate = res.getQuantityString(pluralsResId, timeValue, new Object[]{ Integer.valueOf(timeValue)});
        }
        if (isInPast) {
            return res.getString(R.string.time_ago, new Object[]{relativeDate});
        }
        return res.getString(R.string.time_in, new Object[]{relativeDate});
    }

    static {
        CONTRACT_TYPE_NAMES = new int[]{R.string.futures_contract_type_weekly, R.string.futures_contract_type_biweekly, R.string.futures_contract_type_monthly, R.string.futures_contract_type_bimonthly, R.string.futures_contract_type_quarterly};
    }

    public static CharSequence getContractTypeName(Context context, int contractType) {
        if (contractType < 0 || contractType >= CONTRACT_TYPE_NAMES.length) {
            return Futures.getContractTypeShortName(contractType);
        }
        return context.getString(CONTRACT_TYPE_NAMES[contractType]);
    }
}
