package haivo.us.crypto.appwidget;

import android.content.Context;
import android.content.SharedPreferences;
import haivo.us.crypto.R;

public class WidgetPrefsUtils {
    private static final String PREFS_NAME = "widget_prefs_";

    private static SharedPreferences getSharedPreferencesForWidget(Context context, int appWidgetId) {
        return context.getApplicationContext().getSharedPreferences(getSharedPreferencesName(appWidgetId), 0);
    }

    public static String getSharedPreferencesName(int appWidgetId) {
        return PREFS_NAME + appWidgetId;
    }

    public static boolean getShowActionBar(Context context, int appWidgetId) {
        return getSharedPreferencesForWidget(context,
                                             appWidgetId).getBoolean(context.getString(R.string.widget_settings_show_actionbar_key),
                                                                     true);
    }

    public static boolean getCompactMode(Context context, int appWidgetId) {
        return getSharedPreferencesForWidget(context,
                                             appWidgetId).getBoolean(context.getString(R.string.widget_settings_compact_mode_key),
                                                                     false);
    }

    public static int getAlphaPercent(Context context, int appWidgetId) {
        return getSharedPreferencesForWidget(context,
                                             appWidgetId).getInt(context.getString(R.string.widget_settings_alpha_percent_key),
                                                                 100);
    }

    public static boolean getDarkTheme(Context context, int appWidgetId) {
        return getSharedPreferencesForWidget(context,
                                             appWidgetId).getBoolean(context.getString(R.string.widget_settings_dark_theme_key),
                                                                     false);
    }
}
