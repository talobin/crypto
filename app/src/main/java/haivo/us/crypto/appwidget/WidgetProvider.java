package haivo.us.crypto.appwidget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.widget.RemoteViews;
import haivo.us.crypto.R;
import haivo.us.crypto.receiver.NotificationAndWidgetReceiver;
import haivo.us.crypto.util.NotificationUtils;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class WidgetProvider extends AppWidgetProvider {
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @TargetApi(11)
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        if (VERSION.SDK_INT >= 11) {
            for (int updateWdgetWithId : appWidgetIds) {
                updateWdgetWithId(context, appWidgetManager, updateWdgetWithId);
            }
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listView);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @TargetApi(11)
    public static final void updateWdgetWithId(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (VERSION.SDK_INT >= 11) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);
            boolean isDarkTheme = WidgetPrefsUtils.getDarkTheme(context, appWidgetId);
            int textColorPrimary = context.getResources()
                                          .getColor(isDarkTheme
                                                    ? R.color.widget_text_color_primary_dark
                                                    : R.color.widget_text_color_primary_light);
            remoteViews.setInt(R.id.backgroundView,
                               "setBackgroundResource",
                               getWidgetBackgroundForAlpha(isDarkTheme,
                                                           WidgetPrefsUtils.getAlphaPercent(context, appWidgetId)));
            remoteViews.setInt(R.id.actionBar,
                               "setBackgroundResource",
                               isDarkTheme
                               ? R.drawable.widget_actionbar_bg_dark
                               : R.drawable.widget_actionbar_bg_light);
            remoteViews.setInt(R.id.refreshAllView,
                               "setBackgroundResource",
                               isDarkTheme
                               ? R.drawable.widget_action_refresh_all_bg_dark
                               : R.drawable.widget_action_refresh_all_bg_light);
            int actionBarVisibility = WidgetPrefsUtils.getShowActionBar(context, appWidgetId) ? 0 : 8;
            remoteViews.setTextColor(R.id.titleView, textColorPrimary);
            remoteViews.setViewVisibility(R.id.actionBar, actionBarVisibility);
            remoteViews.setImageViewResource(R.id.actionBarSeparator,
                                             isDarkTheme
                                             ? R.color.widget_actionbar_separator_dark
                                             : R.color.widget_actionbar_separator_light);
            remoteViews.setViewVisibility(R.id.actionBarSeparator, actionBarVisibility);
            remoteViews.setTextColor(R.id.emptyView,
                                     context.getResources()
                                            .getColor(isDarkTheme
                                                      ? R.color.widget_text_color_hint_dark
                                                      : R.color.widget_text_color_hint_light));
            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra("appWidgetId", appWidgetId);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(1)));
            remoteViews.setRemoteAdapter(appWidgetId, R.id.listView, serviceIntent);
            remoteViews.setEmptyView(R.id.listView, R.id.emptyView);
            remoteViews.setTextViewText(R.id.emptyView, context.getString(R.string.widget_list_empty_text));
            PendingIntent mainActivityPendingIntent = NotificationUtils.createMainActivityPendingIntent(context);
            remoteViews.setOnClickPendingIntent(R.id.actionBar, mainActivityPendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.emptyView, mainActivityPendingIntent);
            remoteViews.setImageViewResource(R.id.refreshAllView,
                                             isDarkTheme
                                             ? R.drawable.ic_action_refresh_dark
                                             : R.drawable.ic_action_refresh);
            remoteViews.setOnClickPendingIntent(R.id.refreshAllView,
                                                PendingIntent.getBroadcast(context,
                                                                           0,
                                                                           new Intent(NotificationAndWidgetReceiver.ACTION_NOTIFICATION_REFRESH_ALL,
                                                                                      null,
                                                                                      context,
                                                                                      NotificationAndWidgetReceiver.class),
                                                                           FLAG_UPDATE_CURRENT));
            Intent refreshIntent = new Intent(NotificationAndWidgetReceiver.ACTION_NOTIFICATION_REFRESH,
                                              null,
                                              context,
                                              NotificationAndWidgetReceiver.class);
            refreshIntent.setData(Uri.parse(refreshIntent.toUri(1)));
            remoteViews.setPendingIntentTemplate(R.id.listView,
                                                 PendingIntent.getBroadcast(context,
                                                                            0,
                                                                            refreshIntent,
                                                                            FLAG_UPDATE_CURRENT));
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }

    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @TargetApi(11)
    public static void refreshWidget(Context context) {
        if (VERSION.SDK_INT >= 11) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetManager.getAppWidgetIds(new ComponentName(context,
                                                                                                               WidgetProvider.class)),
                                                            R.id.listView);
        }
    }

    private static int getWidgetBackgroundForAlpha(boolean isDarkTheme, int alphaPercent) {
        if (alphaPercent == 100) {
            if (isDarkTheme) {
                return R.drawable.card_background_dark;
            }
            return R.drawable.card_background;
        } else if (alphaPercent >= 90) {
            return isDarkTheme ? R.drawable.card_background_dark_90 : R.drawable.card_background_90;
        } else {
            if (alphaPercent >= 75) {
                return isDarkTheme ? R.drawable.card_background_dark_75 : R.drawable.card_background_75;
            } else {
                if (alphaPercent >= 50) {
                    return isDarkTheme ? R.drawable.card_background_dark_50 : R.drawable.card_background_50;
                } else {
                    if (alphaPercent >= 25) {
                        return isDarkTheme ? R.drawable.card_background_dark_25 : R.drawable.card_background_25;
                    } else {
                        if (alphaPercent >= 0) {
                            return R.drawable.transparent;
                        }
                        return R.drawable.card_background;
                    }
                }
            }
        }
    }
}
