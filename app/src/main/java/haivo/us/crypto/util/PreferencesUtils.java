package haivo.us.crypto.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import com.google.gson.Gson;
import haivo.us.crypto.R;
import haivo.us.crypto.config.Settings;
import haivo.us.crypto.model.market.Bitstamp;
import haivo.us.crypto.preferences.FrequencyPickerDialogPreference;
import haivo.us.crypto.mechanoid.Mechanoid;

public class PreferencesUtils {
    private static final String CHECKERS_LIST_SORT_MODE_KEY = "checkers_list_sort_mode";
    private static final String DEFAULT_ITEM_ADDED_KEY = "default_item_added";
    private static final String DONATION_MADE_KEY = "donation_made";
    private static final String EXCHANGE_TUTORIAL_DONE_KEY = "exchange_tutorial_done";
    private static final String MARKET_DEFAULT_KEY = "market_default";
    private static final String MARKET_PICKER_LIST_SORT_MODE_KEY = "market_picker_list_sort_mode";
    private static final String NEWS_SHOWN_KEY = "news_shown";
    private static final String USER_COUNTRY_KEY = "user_country";

    private static SharedPreferences getSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context != null ? context.getApplicationContext() : Mechanoid.getApplicationContext());
    }

    public static boolean isDefaultItemAdded(Context context) {
        return getSharedPreferences(context).getBoolean(DEFAULT_ITEM_ADDED_KEY, false);
    }

    public static void setDefaultItemAdded(Context context) {
        getSharedPreferences(context).edit().putBoolean(DEFAULT_ITEM_ADDED_KEY, true).commit();
    }

    public static boolean wasNewsShown(Context context, int newsId) {
        return getSharedPreferences(context).getInt(NEWS_SHOWN_KEY, -1) >= newsId;
    }

    public static void setNewsShown(Context context, int newsId) {
        getSharedPreferences(context).edit().putInt(NEWS_SHOWN_KEY, newsId).commit();
    }

    public static boolean isExchangeTutorialDone(Context context) {
        return getSharedPreferences(context).getBoolean(EXCHANGE_TUTORIAL_DONE_KEY, false);
    }

    public static void setExchangeTutorialDone(Context context) {
        getSharedPreferences(context).edit().putBoolean(EXCHANGE_TUTORIAL_DONE_KEY, true).commit();
    }

    public static boolean getDonationMade(Context context) {
        return getSharedPreferences(context).getBoolean(DONATION_MADE_KEY, false);
    }

    public static void setDonationMade(Context context) {
        getSharedPreferences(context).edit().putBoolean(DONATION_MADE_KEY, true).commit();
    }

    public static int getCheckersListSortMode(Context context) {
        return getSharedPreferences(context).getInt(CHECKERS_LIST_SORT_MODE_KEY, 0);
    }

    public static void setCheckersListSortMode(Context context, int checkersListSortMode) {
        getSharedPreferences(context).edit().putInt(CHECKERS_LIST_SORT_MODE_KEY, checkersListSortMode).commit();
    }

    public static void setCheckGlobalFrequency(Context context, long frequency) {
        getSharedPreferences(context).edit().putLong(context.getString(R.string.settings_check_global_frequency_key), frequency).commit();
    }

    public static long getCheckGlobalFrequency(Context context) {
        return getSharedPreferences(context).getLong(context.getString(R.string.settings_check_global_frequency_key), FrequencyPickerDialogPreference.DEFAULT_FREQUENCY_MILLIS);
    }

    public static int getMarketPickerListSortMode(Context context) {
        return getSharedPreferences(context).getInt(MARKET_PICKER_LIST_SORT_MODE_KEY, 0);
    }

    public static void setMarketPickerListSortMode(Context context, int marketPickerListSortMode) {
        getSharedPreferences(context).edit().putInt(MARKET_PICKER_LIST_SORT_MODE_KEY, marketPickerListSortMode).commit();
    }

    public static String getMarketDefault(Context context) {
        return getSharedPreferences(context).getString(MARKET_DEFAULT_KEY, Bitstamp.class.getSimpleName());
    }

    public static void setMarketDefault(Context context, String marketDefault) {
        getSharedPreferences(context).edit().putString(MARKET_DEFAULT_KEY, marketDefault).commit();
    }

    public static String getUserCountry(Context context) {
        Settings.userCountry = getSharedPreferences(context).getString(USER_COUNTRY_KEY, null);
        return Settings.userCountry;
    }

    public static void setUserCountry(Context context, String userCountry) {
        Settings.userCountry = userCountry;
        getSharedPreferences(context).edit().putString(USER_COUNTRY_KEY, userCountry).commit();
    }

    public static void setCheckNotificationExpandable(Context context, boolean enabled) {
        getSharedPreferences(context).edit().putBoolean(context.getString(R.string.settings_check_notification_expandable_key), enabled).commit();
    }

    public static boolean getCheckNotificationExpandable(Context context) {
        return getSharedPreferences(context).getBoolean(context.getString(R.string.settings_check_notification_expandable_key), true);
    }

    public static void setCheckNotificationCustomLayout(Context context, boolean enabled) {
        getSharedPreferences(context).edit().putBoolean(context.getString(R.string.settings_check_notification_custom_layout_key), enabled).commit();
    }

    public static boolean getCheckNotificationCustomLayout(Context context) {
        return getSharedPreferences(context).getBoolean(context.getString(R.string.settings_check_notification_custom_layout_key), true);
    }

    public static boolean getCheckNotificationTicker(Context context) {
        return getSharedPreferences(context).getBoolean(context.getString(R.string.settings_check_notification_ticker_key), true);
    }

    public static String getSoundAlarmNotificationUp(Context context) {
        Uri defaultUri = SoundFilesManager.getUriForRingtone(context, SoundFilesManager.BITCOIN_CHECKER_UP_CHEERS_FILENAME);
        String uriString = getSharedPreferences(context).getString(context.getString(R.string.settings_sounds_alarm_notification_up_key), defaultUri != null ? defaultUri.toString() : null);
        if (uriString == null) {
            return "android.resource://raw/bitcoin_checker_up_cheers";
        }
        return uriString;
    }

    public static String getSoundAlarmNotificationDown(Context context) {
        Uri defaultUri = SoundFilesManager.getUriForRingtone(context, SoundFilesManager.BITCOIN_CHECKER_DOWN_ALERT_FILENAME);
        String uriString = getSharedPreferences(context).getString(context.getString(R.string.settings_sounds_alarm_notification_down_key), defaultUri != null ? defaultUri.toString() : null);
        if (uriString == null) {
            return "android.resource://raw/bitcoin_checker_down_alert";
        }
        return uriString;
    }

    public static int getSoundsAdvancedAlarmStream(Context context) {
        return getSharedPreferences(context).getInt(context.getString(R.string.settings_sounds_advanced_alarm_stream_key), 5);
    }

    public static boolean getTTSFormatSpeakBaseCurrency(Context context) {
        return getSharedPreferences(context).getBoolean(context.getString(R.string.settings_tts_format_speak_base_currency_key), true);
    }

    public static boolean getTTSFormatIntegerOnly(Context context) {
        return getSharedPreferences(context).getBoolean(context.getString(R.string.settings_tts_format_integer_only_key), true);
    }

    public static boolean getTTSFormatSpeakCounterCurrency(Context context) {
        return getSharedPreferences(context).getBoolean(context.getString(R.string.settings_tts_format_speak_counter_currency_key), false);
    }

    public static boolean getTTSFormatSpeakExchange(Context context) {
        return getSharedPreferences(context).getBoolean(context.getString(R.string.settings_tts_format_speak_exchange_key), false);
    }

    public static boolean getTTSDisplayOffOnly(Context context) {
        return getSharedPreferences(context).getBoolean(context.getString(R.string.settings_tts_screen_off_only_key), false);
    }

    public static boolean getTTSHeadphonesOnly(Context context) {
        return getSharedPreferences(context).getBoolean(context.getString(R.string.settings_tts_headphones_only_key), false);
    }

    public static int getTTSAdvancedStream(Context context) {
        return getSharedPreferences(context).getInt(context.getString(R.string.settings_tts_advanced_stream_key), 5);
    }

    private static Editor putDouble(Editor edit, String key, double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    private static double getDouble(SharedPreferences prefs, String key, double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    private static final void putObject(Context context, String key, Object obj) {
        String jsonString = null;
        if (obj != null) {
            try {
                jsonString = new Gson().toJson(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getSharedPreferences(context).edit().putString(key, jsonString).commit();
    }

    private static final <T> T getObject(Context context, String key, Class<T> clazz) {
        T t = null;
        try {
            t = (T) new Gson().fromJson(getSharedPreferences(context).getString(key, null), (Class) clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }
}
