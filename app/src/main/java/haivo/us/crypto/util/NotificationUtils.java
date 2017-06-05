package haivo.us.crypto.util;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.widget.RemoteViews;
import haivo.us.crypto.BuildConfig;
import haivo.us.crypto.R;
import haivo.us.crypto.activity.CheckerAddActivity;
import haivo.us.crypto.activity.CheckersListActivity;
import haivo.us.crypto.alarm.AlarmKlaxonHelper;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.mechanoid.db.ActiveRecord;
import haivo.us.crypto.mechanoid.db.SQuery;
import haivo.us.crypto.model.CurrencySubunit;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.receiver.MarketChecker;
import haivo.us.crypto.receiver.NotificationAndWidgetReceiver;
import java.math.BigDecimal;
import java.util.Iterator;

import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class NotificationUtils {
    private static final String NOTIFICATION_ALARM_TAG = "alarm";
    private static final String NOTIFICATION_CHECKER_TAG = "checker";

    public static boolean checkfThereIsAlertSituationAndShowAlertNotification(Context context,
                                                                              CheckerRecord checkerRecord,
                                                                              Ticker ticker) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        boolean bl = false;
        if (!checkerRecord.getEnabled()) {
            NotificationUtils.clearAlarmNotificationForCheckerRecord(context, notificationManagerCompat, checkerRecord);
            return false;
        }
        if (checkerRecord.getErrorMsg() != null) {
            return false;
        }
        Iterator<AlarmRecord> iterator = CheckerRecordHelper.getAlarmsForCheckerRecord(checkerRecord, false).iterator();
        while (iterator.hasNext()) {
            AlarmRecord alarmRecord = iterator.next();
            if (alarmRecord == null) continue;
            if (!alarmRecord.getEnabled()) {
                notificationManagerCompat.cancel(NOTIFICATION_ALARM_TAG, (int) alarmRecord.getId());
                continue;
            }
            int n = 1;
            boolean bl2 = false;
            CharSequence object = null;
            Object object2 = null;
            Object object3 = "";
            Object object4 = MarketsConfigUtils.getMarketByKey(checkerRecord.getMarketKey());
            Object string2 = FormatUtils.getCurrencySrcWithContractType(checkerRecord.getCurrencySrc(),
                                                                        (Market) object4,
                                                                        (int) checkerRecord.getContractType());
            CurrencySubunit currencySubunit =
                CurrencyUtils.getCurrencySubunit(checkerRecord.getCurrencyDst(), checkerRecord.getCurrencySubunitDst());
            int n2 = (int) alarmRecord.getType();
            Ticker ticker2;
            boolean bl3;
            BigDecimal bigDecimal;
            switch (n2) {

                default: {
                    n2 = 0;
                    break;
                }
                case 0:
                case 1:
                case 2: {
                    ticker2 = TickerUtils.fromJson(alarmRecord.getLastCheckPointTicker());
                    if (ticker2 == null) {
                        alarmRecord.setLastCheckPointTicker(TickerUtils.toJson(ticker));
                        alarmRecord.save();
                        n2 = 0;
                        break;
                    }
                    double d = AlarmRecordHelper.getDifferenceForPercentageChange(ticker2.last, ticker.last);
                    double d2 = alarmRecord.getValue();
                    if (n2 == 0 && Math.abs(d) < d2) {
                        n2 = 0;
                        break;
                    }
                    if (n2 == 1 && d < d2) {
                        n2 = 0;
                        break;
                    }
                    if (n2 == 2 && d > -d2) {
                        n2 = 0;
                        break;
                    }
                    alarmRecord.setLastCheckPointTicker(TickerUtils.toJson(ticker));
                    alarmRecord.save();
                    bl3 = d < 0.0;
                    object = bl3
                             ? context.getString(R.string.notification_alert_tts_text_down)
                             : context.getString(R.string.notification_alert_tts_text_up);
                    string2 = Html.fromHtml(context.getString(R.string.notification_alert_title_percent_change,

                                                              string2,
                                                              "<b>"
                                                                  + FormatUtils.formatPriceWithCurrency(ticker.last,
                                                                                                        currencySubunit)
                                                                  + "</b>",
                                                              ((Market) object4).name));
                    object2 = new StringBuilder().append("<b>");
                    object3 = d > 0.0 ? "+" : "";
                    Spanned crap = Html.fromHtml(context.getString(R.string.notification_alert_text_percent_change,

                                                                   ((StringBuilder) object2).append((String) object3)
                                                                                            .append(FormatUtils.formatDouble(
                                                                                                d,
                                                                                                false))
                                                                                            .append("%</b>")
                                                                                            .toString(),
                                                                   "<b>"
                                                                       + FormatUtils.formatPriceWithCurrency(ticker2.last,
                                                                                                             currencySubunit)
                                                                       + "</b>",
                                                                   FormatUtils.formatRelativeTime(context,
                                                                                                  ticker2.timestamp,
                                                                                                  ticker.timestamp,
                                                                                                  true)));
                    object4 = context.getString(R.string.notification_alert_tts_text_percent_value_change,

                                                object,
                                                FormatUtils.formatTextForTTS(context,
                                                                             ticker2.last,
                                                                             checkerRecord,
                                                                             true,
                                                                             currencySubunit,
                                                                             true,
                                                                             (Market) object4,
                                                                             true),
                                                FormatUtils.formatTextForTTS(context,
                                                                             ticker.last,
                                                                             checkerRecord,
                                                                             true,
                                                                             currencySubunit,
                                                                             false,
                                                                             (Market) object4,
                                                                             false));
                    bl2 = bl3;
                    n2 = n;
                    object = crap;
                    object2 = string2;
                    object3 = object4;
                    if (!PreferencesUtils.getTTSFormatSpeakBaseCurrency(context)) break;
                    object3 = context.getString(R.string.tts_format_base_currency,
                                                new Object[] { checkerRecord.getCurrencySrc(), object4 });
                    bl2 = bl3;
                    n2 = n;
                    object = crap;
                    object2 = string2;
                    break;
                }
                case 3:
                case 4:
                case 5: {
                    ticker2 = TickerUtils.fromJson(alarmRecord.getLastCheckPointTicker());
                    if (ticker2 == null) {
                        alarmRecord.setLastCheckPointTicker(TickerUtils.toJson(ticker));
                        alarmRecord.save();
                        n2 = 0;
                        break;
                    }
                    BigDecimal bigDecimal2 = FormatUtils.fixSatoshi(alarmRecord.getValue());
                    bigDecimal = FormatUtils.fixSatoshi(ticker.last).subtract(FormatUtils.fixSatoshi(ticker2.last));
                    if (n2 == 3 && bigDecimal.abs().compareTo(bigDecimal2) < 0) {
                        n2 = 0;
                        break;
                    }
                    if (n2 == 4 && bigDecimal.compareTo(bigDecimal2) < 0) {
                        n2 = 0;
                        break;
                    }
                    if (n2 == 5 && bigDecimal.compareTo(bigDecimal2.negate()) > 0) {
                        n2 = 0;
                        break;
                    }
                    alarmRecord.setLastCheckPointTicker(TickerUtils.toJson(ticker));
                    alarmRecord.save();
                    bl3 = bigDecimal.doubleValue() < 0.0;
                    object = bl3
                             ? context.getString(R.string.notification_alert_tts_text_down)
                             : context.getString(R.string.notification_alert_tts_text_up);
                    string2 = Html.fromHtml((String) context.getString(R.string.notification_alert_title_value_change,
                                                                       string2,
                                                                       "<b>" + FormatUtils.formatPriceWithCurrency(
                                                                           ticker.last,
                                                                           currencySubunit) + "</b>",
                                                                       ((Market) object4).name));
                    object2 = new StringBuilder().append("<b>");
                    object3 = bigDecimal.doubleValue() > 0.0 ? "+" : "";
                    Spanned crap =
                        Html.fromHtml((String) context.getString(R.string.notification_alert_text_value_change,

                                                                 ((StringBuilder) object2).append((String) object3)
                                                                                          .append(FormatUtils.formatPriceWithCurrency(
                                                                                              bigDecimal.doubleValue(),
                                                                                              currencySubunit))
                                                                                          .append("</b>")
                                                                                          .toString(),
                                                                 "<b>"
                                                                     + FormatUtils.formatPriceWithCurrency(ticker2.last,
                                                                                                           currencySubunit)
                                                                     + "</b>",
                                                                 FormatUtils.formatRelativeTime(context,
                                                                                                ticker2.timestamp,
                                                                                                ticker.timestamp,
                                                                                                true)));
                    object4 = context.getString(R.string.notification_alert_tts_text_percent_value_change,
                                                object,
                                                FormatUtils.formatTextForTTS(context,
                                                                             ticker2.last,
                                                                             checkerRecord,
                                                                             true,
                                                                             currencySubunit,
                                                                             true,
                                                                             (Market) object4,
                                                                             true),
                                                FormatUtils.formatTextForTTS(context,
                                                                             ticker.last,
                                                                             checkerRecord,
                                                                             true,
                                                                             currencySubunit,
                                                                             false,
                                                                             (Market) object4,
                                                                             false));
                    bl2 = bl3;
                    n2 = n;
                    object = crap;
                    object2 = string2;
                    object3 = object4;
                    if (!PreferencesUtils.getTTSFormatSpeakBaseCurrency(context)) break;
                    object3 = context.getString(R.string.tts_format_base_currency,
                                                new Object[] { checkerRecord.getCurrencySrc(), object4 });
                    bl2 = bl3;
                    n2 = n;
                    object = crap;
                    object2 = string2;
                    break;
                }
                case 6: {
                    if (ticker.last < alarmRecord.getValue()) {
                        n2 = 0;
                        break;
                    }
                    bl2 = false;
                    object2 = Html.fromHtml(context.getString(R.string.notification_alert_title_greater_lower,

                                                              string2,
                                                              "<b>"
                                                                  + FormatUtils.formatPriceWithCurrency(ticker.last,
                                                                                                        currencySubunit)
                                                                  + "</b>",
                                                              ((Market) object4).name));
                    object = Html.fromHtml(context.getString(R.string.notification_alert_text_greater_than,

                                                             "<b>"
                                                                 + FormatUtils.formatPriceWithCurrency(alarmRecord.getValue(),
                                                                                                       currencySubunit)
                                                                 + "</b>"));
                    object3 = context.getString(R.string.notification_alert_tts_text_greater_lower,
                                                FormatUtils.formatTextForTTS(context,
                                                                             ticker.last,
                                                                             checkerRecord,
                                                                             currencySubunit,
                                                                             (Market) object4));
                    n2 = n;
                    break;
                }
                case 7: {
                    if (ticker.last > alarmRecord.getValue()) {
                        n2 = 0;
                        break;
                    }
                    bl2 = true;
                    object2 = Html.fromHtml(context.getString(R.string.notification_alert_title_greater_lower,

                                                              string2,
                                                              "<b>"
                                                                  + FormatUtils.formatPriceWithCurrency(ticker.last,
                                                                                                        currencySubunit)
                                                                  + "</b>",
                                                              ((Market) object4).name));
                    object = Html.fromHtml(context.getString(R.string.notification_alert_text_lower_than,
                                                             "<b>"
                                                                 + FormatUtils.formatPriceWithCurrency(alarmRecord.getValue(),
                                                                                                       currencySubunit)
                                                                 + "</b>"));
                    object3 = context.getString(R.string.notification_alert_tts_text_greater_lower,
                                                FormatUtils.formatTextForTTS(context,
                                                                             ticker.last,
                                                                             checkerRecord,
                                                                             currencySubunit,
                                                                             (Market) object4));
                    n2 = n;
                }
            }
            if (n2 == 0) {
                notificationManagerCompat.cancel(NOTIFICATION_CHECKER_TAG, (int) alarmRecord.getId());
                continue;
            }
            Builder builder = new NotificationCompat.Builder(context).setContentTitle((CharSequence) object2)
                                                                     .setContentText((CharSequence) object)
                                                                     .setTicker((CharSequence) object2)
                                                                     .setOngoing(false)
                                                                     .setAutoCancel(true)
                                                                     .setOnlyAlertOnce(false)
                                                                     .setWhen(ticker.timestamp)
                                                                     .setPriority(2)
                                                                     .setContentIntent(NotificationUtils.createCheckerAlertDetailsPendingIntent(
                                                                         context,
                                                                         checkerRecord,
                                                                         alarmRecord));

            if (Build.VERSION.SDK_INT >= 21) {
                builder.setSmallIcon(bl2 ? R.drawable.ic_notify_alert_down_white : R.drawable.ic_notify_alert_up_white);
                builder.setColor(context.getResources().getColor(bl2 ? R.color.circle_red : R.color.circle_green));
            } else {
                builder.setSmallIcon(bl2 ? R.drawable.ic_notify_alert_down : R.drawable.ic_notify_alert_up);
            }
            builder.setDeleteIntent(NotificationUtils.createAlarmDismissPendingIntent(context,
                                                                                      checkerRecord,
                                                                                      alarmRecord));
            if (false) {
                //hai
                //builder.addAction(2130837630,
                //                  context.getResources().getString(2131165317),
                //                  AlarmKlaxonHelper.createAlarmKlaxonDismissPendingIntent(context,
                //                                                                          checkerRecord.getId(),
                //                                                                          alarmRecord.getId()));
            } else if (alarmRecord.getSound()) {
                object = bl2
                         ? PreferencesUtils.getSoundAlarmNotificationDown(context)
                         : PreferencesUtils.getSoundAlarmNotificationUp(context);
                builder.setSound(Uri.parse((String) object), PreferencesUtils.getSoundsAdvancedAlarmStream(context));
            }
            if (alarmRecord.getVibrate()) {
                builder.setVibrate(new long[] { 0, 500, 250, 500 });
            }
            if (alarmRecord.getLed()) {
                builder.setLights(bl2 ? Color.RED : Color.GREEN, 1000, 1000);
            }
            notificationManagerCompat.cancel(NOTIFICATION_CHECKER_TAG, (int) alarmRecord.getId());
            notificationManagerCompat.notify(NOTIFICATION_CHECKER_TAG, (int) alarmRecord.getId(), builder.build());
            if (false) {
                AlarmKlaxonHelper.startAlarmKlaxon(context, alarmRecord);
            }
            if (!alarmRecord.getTtsEnabled() || TextUtils.isEmpty((CharSequence) object3)) continue;
            TTSHelper.speak(context.getApplicationContext(), (String) object3);
            bl |= true;
        }
        return bl;
    }

    //NotificationManagerCompat from = NotificationManagerCompat.from(context);
    //    if (!checkerRecord.getEnabled()) {
    //    clearAlarmNotificationForCheckerRecord(context, from, checkerRecord);
    //    return false;
    //} else if (checkerRecord.getErrorMsg() != null) {
    //    return false;
    //} else {
    //    boolean z = false;
    //    for (AlarmRecord alarmRecord : CheckerRecordHelper.getAlarmsForCheckerRecord(checkerRecord, false)) {
    //        if (alarmRecord != null) {
    //            if (alarmRecord.getEnabled()) {
    //                Object obj;
    //                Object obj2 = null;
    //                CharSequence charSequence = null;
    //                CharSequence charSequence2 = null;
    //                Object obj3 = BuildConfig.VERSION_NAME;
    //                Market marketByKey = MarketsConfigUtils.getMarketByKey(checkerRecord.getMarketKey());
    //                String currencySrcWithContractType =
    //                    FormatUtils.getCurrencySrcWithContractType(checkerRecord.getCurrencySrc(),
    //                                                               marketByKey,
    //                                                               (int) checkerRecord.getContractType());
    //                CurrencySubunit currencySubunit =
    //                    CurrencyUtils.getCurrencySubunit(checkerRecord.getCurrencyDst(),
    //                                                     checkerRecord.getCurrencySubunitDst());
    //                int type = (int) alarmRecord.getType();
    //                Ticker fromJson;
    //                Object[] objArr;
    //                String[] strArr;
    //                Spanned fromHtml;
    //                String[] strArr2;
    //                String string;
    //                Object obj4;
    //                String str;
    //                int i;
    //                Object obj5;
    //                String[] strArr3;
    //                switch (type) {
    //                    case 0 /*0*/:
    //                    case HeaderViewListener.STATE_MINIMIZED /*1*/:
    //                    case HeaderViewListener.STATE_HIDDEN /*2*/:
    //                        fromJson = TickerUtils.fromJson(alarmRecord.getLastCheckPointTicker());
    //                        if (fromJson != null) {
    //                            double differenceForPercentageChange =
    //                                AlarmRecordHelper.getDifferenceForPercentageChange(fromJson.last, ticker.last);
    //                            double value = alarmRecord.getValue();
    //                            if (type != 0 || Math.abs(differenceForPercentageChange) >= value) {
    //                                if (type == 1 && differenceForPercentageChange < value) {
    //                                    obj = null;
    //                                    break;
    //                                }
    //                                if (type == 2) {
    //                                    if (differenceForPercentageChange > (-value)) {
    //                                        obj = null;
    //                                        break;
    //                                    }
    //                                }
    //                                alarmRecord.setLastCheckPointTicker(TickerUtils.toJson(ticker));
    //                                alarmRecord.save();
    //                                Object obj6 = differenceForPercentageChange < 0.0d ? 1 : null;
    //                                String string2 = context.getString(obj6 != null
    //                                                                   ? R.string.notification_alert_tts_text_down
    //                                                                   : R.string.notification_alert_tts_text_up);
    //                                objArr = new Object[3];
    //                                objArr[1] = "<b>"
    //                                    + FormatUtilsBase.formatPriceWithCurrency(ticker.last,
    //                                                                              currencySubunit)
    //                                    + "</b>";
    //                                objArr[2] = marketByKey.name;
    //                                Spanned fromHtml2 =
    //                                    Html.fromHtml(context.getString(R.string.notification_alert_title_percent_change,
    //                                                                    objArr));
    //                                strArr = new Object[3];
    //                                strArr[0] = "<b>" + (differenceForPercentageChange > 0.0d
    //                                                     ? "+"
    //                                                     : BuildConfig.VERSION_NAME) + FormatUtilsBase.formatDouble(
    //                                    differenceForPercentageChange,
    //                                    false) + "%</b>";
    //                                strArr[1] = "<b>"
    //                                    + FormatUtilsBase.formatPriceWithCurrency(fromJson.last,
    //                                                                              currencySubunit)
    //                                    + "</b>";
    //                                strArr[2] = FormatUtils.formatRelativeTime(context,
    //                                                                           fromJson.timestamp,
    //                                                                           ticker.timestamp,
    //                                                                           true);
    //                                fromHtml =
    //                                    Html.fromHtml(context.getString(R.string.notification_alert_text_percent_change,
    //                                                                    strArr));
    //                                strArr2 = new Object[3];
    //                                strArr2[1] = FormatUtils.formatTextForTTS(context,
    //                                                                          fromJson.last,
    //                                                                          checkerRecord,
    //                                                                          true,
    //                                                                          currencySubunit,
    //                                                                          true,
    //                                                                          marketByKey,
    //                                                                          true);
    //                                strArr2[2] = FormatUtils.formatTextForTTS(context,
    //                                                                          ticker.last,
    //                                                                          checkerRecord,
    //                                                                          true,
    //                                                                          currencySubunit,
    //                                                                          false,
    //                                                                          marketByKey,
    //                                                                          false);
    //                                string =
    //                                    context.getString(R.string.notification_alert_tts_text_percent_value_change,
    //                                                      strArr2);
    //                                if (!PreferencesUtils.getTTSFormatSpeakBaseCurrency(context)) {
    //                                    obj4 = fromHtml;
    //                                    str = string;
    //                                    i = 1;
    //                                    obj5 = fromHtml2;
    //                                    obj2 = obj6;
    //                                    break;
    //                                }
    //                                obj4 = fromHtml;
    //                                str = context.getString(R.string.tts_format_base_currency, new Object[] {
    //                                    checkerRecord.getCurrencySrc(), string
    //                                });
    //                                i = 1;
    //                                obj5 = fromHtml2;
    //                                obj2 = obj6;
    //                                break;
    //                            }
    //                            obj = null;
    //                            break;
    //                        }
    //                        alarmRecord.setLastCheckPointTicker(TickerUtils.toJson(ticker));
    //                        alarmRecord.save();
    //                        obj = null;
    //                        break;
    //                    break;
    //                    case C0377R.styleable.Theme_panelMenuListWidth /*3*/:
    //                    case C0377R.styleable.Theme_panelMenuListTheme /*4*/:
    //                    case C0377R.styleable.Theme_listChoiceBackgroundIndicator /*5*/:
    //                        fromJson = TickerUtils.fromJson(alarmRecord.getLastCheckPointTicker());
    //                        if (fromJson != null) {
    //                            BigDecimal fixSatoshi = FormatUtils.fixSatoshi(alarmRecord.getValue());
    //                            BigDecimal subtract = FormatUtils.fixSatoshi(ticker.last)
    //                                                             .subtract(FormatUtils.fixSatoshi(fromJson.last));
    //                            if (type == 3) {
    //                                if (subtract.abs().compareTo(fixSatoshi) < 0) {
    //                                    obj = null;
    //                                    break;
    //                                }
    //                            }
    //                            if (type != 4 || subtract.compareTo(fixSatoshi) >= 0) {
    //                                if (type == 5 && subtract.compareTo(fixSatoshi.negate()) > 0) {
    //                                    obj = null;
    //                                    break;
    //                                }
    //                                alarmRecord.setLastCheckPointTicker(TickerUtils.toJson(ticker));
    //                                alarmRecord.save();
    //                                obj2 = subtract.doubleValue() < 0.0d ? 1 : null;
    //                                String string3 = context.getString(obj2 != null
    //                                                                   ? R.string.notification_alert_tts_text_down
    //                                                                   : R.string.notification_alert_tts_text_up);
    //                                objArr = new Object[3];
    //                                objArr[1] = "<b>"
    //                                    + FormatUtilsBase.formatPriceWithCurrency(ticker.last,
    //                                                                              currencySubunit)
    //                                    + "</b>";
    //                                objArr[2] = marketByKey.name;
    //                                fromHtml =
    //                                    Html.fromHtml(context.getString(R.string.notification_alert_title_value_change,
    //                                                                    objArr));
    //                                strArr2 = new Object[3];
    //                                strArr2[0] = "<b>"
    //                                    + (subtract.doubleValue() > 0.0d ? "+" : BuildConfig.VERSION_NAME)
    //                                    + FormatUtilsBase.formatPriceWithCurrency(subtract.doubleValue(),
    //                                                                              currencySubunit)
    //                                    + "</b>";
    //                                strArr2[1] = "<b>"
    //                                    + FormatUtilsBase.formatPriceWithCurrency(fromJson.last,
    //                                                                              currencySubunit)
    //                                    + "</b>";
    //                                strArr2[2] = FormatUtils.formatRelativeTime(context,
    //                                                                            fromJson.timestamp,
    //                                                                            ticker.timestamp,
    //                                                                            true);
    //                                Spanned fromHtml3 =
    //                                    Html.fromHtml(context.getString(R.string.notification_alert_text_value_change,
    //                                                                    strArr2));
    //                                strArr = new Object[3];
    //                                strArr[1] = FormatUtils.formatTextForTTS(context,
    //                                                                         fromJson.last,
    //                                                                         checkerRecord,
    //                                                                         true,
    //                                                                         currencySubunit,
    //                                                                         true,
    //                                                                         marketByKey,
    //                                                                         true);
    //                                strArr[2] = FormatUtils.formatTextForTTS(context,
    //                                                                         ticker.last,
    //                                                                         checkerRecord,
    //                                                                         true,
    //                                                                         currencySubunit,
    //                                                                         false,
    //                                                                         marketByKey,
    //                                                                         false);
    //                                string =
    //                                    context.getString(R.string.notification_alert_tts_text_percent_value_change,
    //                                                      strArr);
    //                                if (!PreferencesUtils.getTTSFormatSpeakBaseCurrency(context)) {
    //                                    obj4 = fromHtml3;
    //                                    str = string;
    //                                    i = 1;
    //                                    obj5 = fromHtml;
    //                                    break;
    //                                }
    //                                obj4 = fromHtml3;
    //                                str = context.getString(R.string.tts_format_base_currency, new Object[] {
    //                                    checkerRecord.getCurrencySrc(), string
    //                                });
    //                                i = 1;
    //                                obj5 = fromHtml;
    //                                break;
    //                            }
    //                            obj = null;
    //                            break;
    //                        }
    //                        alarmRecord.setLastCheckPointTicker(TickerUtils.toJson(ticker));
    //                        alarmRecord.save();
    //                        obj = null;
    //                        break;
    //                    break;
    //                    case C0377R.styleable.Spinner_prompt /*6*/:
    //                        if (ticker.last >= alarmRecord.getValue()) {
    //                            obj2 = null;
    //                            charSequence2 =
    //                                Html.fromHtml(context.getString(R.string.notification_alert_title_greater_lower,
    //                                                                new Object[] {
    //                                                                    currencySrcWithContractType,
    //                                                                    "<b>"
    //                                                                        + FormatUtilsBase.formatPriceWithCurrency(
    //                                                                        ticker.last,
    //                                                                        currencySubunit)
    //                                                                        + "</b>",
    //                                                                    marketByKey.name
    //                                                                }));
    //                            charSequence =
    //                                Html.fromHtml(context.getString(R.string.notification_alert_text_greater_than,
    //                                                                new Object[] {
    //                                                                    "<b>"
    //                                                                        + FormatUtilsBase.formatPriceWithCurrency(
    //                                                                        alarmRecord.getValue(),
    //                                                                        currencySubunit)
    //                                                                        + "</b>"
    //                                                                }));
    //                            strArr3 = new Object[1];
    //                            strArr3[0] = FormatUtils.formatTextForTTS(context,
    //                                                                      ticker.last,
    //                                                                      checkerRecord,
    //                                                                      currencySubunit,
    //                                                                      marketByKey);
    //                            obj3 =
    //                                context.getString(R.string.notification_alert_tts_text_greater_lower, strArr3);
    //                            i = 1;
    //                            break;
    //                        }
    //                        obj = null;
    //                        break;
    //                    case C0377R.styleable.Spinner_spinnerMode /*7*/:
    //                        if (ticker.last <= alarmRecord.getValue()) {
    //                            obj2 = 1;
    //                            charSequence2 =
    //                                Html.fromHtml(context.getString(R.string.notification_alert_title_greater_lower,
    //                                                                new Object[] {
    //                                                                    currencySrcWithContractType,
    //                                                                    "<b>"
    //                                                                        + FormatUtilsBase.formatPriceWithCurrency(
    //                                                                        ticker.last,
    //                                                                        currencySubunit)
    //                                                                        + "</b>",
    //                                                                    marketByKey.name
    //                                                                }));
    //                            charSequence =
    //                                Html.fromHtml(context.getString(R.string.notification_alert_text_lower_than,
    //                                                                new Object[] {
    //                                                                    "<b>"
    //                                                                        + FormatUtilsBase.formatPriceWithCurrency(
    //                                                                        alarmRecord.getValue(),
    //                                                                        currencySubunit)
    //                                                                        + "</b>"
    //                                                                }));
    //                            strArr3 = new Object[1];
    //                            strArr3[0] = FormatUtils.formatTextForTTS(context,
    //                                                                      ticker.last,
    //                                                                      checkerRecord,
    //                                                                      currencySubunit,
    //                                                                      marketByKey);
    //                            obj3 =
    //                                context.getString(R.string.notification_alert_tts_text_greater_lower, strArr3);
    //                            i = 1;
    //                            break;
    //                        }
    //                        obj = null;
    //                        break;
    //                    default:
    //                        obj = null;
    //                        break;
    //                }
    //                if (obj == null) {
    //                    from.cancel(NOTIFICATION_ALARM_TAG, (int) alarmRecord.getId());
    //                } else {
    //                    Builder contentIntent = new Builder(context).setContentTitle(charSequence2)
    //                                                                .setContentText(charSequence)
    //                                                                .setTicker(charSequence2)
    //                                                                .setOngoing(false)
    //                                                                .setAutoCancel(true)
    //                                                                .setOnlyAlertOnce(false)
    //                                                                .setWhen(ticker.timestamp)
    //                                                                .setPriority(2)
    //                                                                .setContentIntent(
    //                                                                    createCheckerAlertDetailsPendingIntent(
    //                                                                        context,
    //                                                                        checkerRecord,
    //                                                                        alarmRecord));
    //                    if (VERSION.SDK_INT >= 21) {
    //                        contentIntent.setSmallIcon(obj2 != null
    //                                                   ? R.drawable.ic_notify_alert_down_white
    //                                                   : R.drawable.ic_notify_alert_up_white);
    //                        contentIntent.setColor(context.getResources()
    //                                                      .getColor(obj2 != null
    //                                                                ? R.color.circle_red
    //                                                                : R.color.circle_green));
    //                    } else {
    //                        contentIntent.setSmallIcon(obj2 != null
    //                                                   ? R.drawable.ic_notify_alert_down
    //                                                   : R.drawable.ic_notify_alert_up);
    //                    }
    //                    contentIntent.setDeleteIntent(createAlarmDismissPendingIntent(context,
    //                                                                                  checkerRecord,
    //                                                                                  alarmRecord));
    //                    if (alarmRecord.getSound()) {
    //                        contentIntent.setSound(Uri.parse(obj2 != null
    //                                                         ? PreferencesUtils.getSoundAlarmNotificationDown(
    //                            context)
    //                                                         : PreferencesUtils.getSoundAlarmNotificationUp(context)),
    //                                               PreferencesUtils.getSoundsAdvancedAlarmStream(context));
    //                    }
    //                    if (alarmRecord.getVibrate()) {
    //                        contentIntent.setVibrate(new long[] { 0, 500, 250, 500 });
    //                    }
    //                    if (alarmRecord.getLed()) {
    //                        contentIntent.setLights(obj2 != null ? SupportMenu.CATEGORY_MASK : -16711936,
    //                                                1000,
    //                                                1000);
    //                    }
    //                    from.cancel(NOTIFICATION_ALARM_TAG, (int) alarmRecord.getId());
    //                    from.notify(NOTIFICATION_ALARM_TAG, (int) alarmRecord.getId(), contentIntent.build());
    //                    if (alarmRecord.getTtsEnabled() && !TextUtils.isEmpty(obj3)) {
    //                        TTSHelper.speak(context.getApplicationContext(), obj3);
    //                        z |= 1;
    //                    }
    //                }
    //            } else {
    //                from.cancel(NOTIFICATION_ALARM_TAG, (int) alarmRecord.getId());
    //            }
    //        }
    //    }
    //    return z;
    //}

    public static void clearAlarmNotificationForAlarmRecord(Context context, long j) {
        if (j > 0) {
            NotificationManagerCompat.from(context).cancel(NOTIFICATION_ALARM_TAG, (int) j);
        }
    }

    private static void clearAlarmNotificationForCheckerRecord(Context context,
                                                               NotificationManagerCompat notificationManagerCompat,
                                                               CheckerRecord checkerRecord) {
        for (Long longValue : CheckerRecordHelper.getAlarmsIdsForCheckerRecord(checkerRecord.getId())) {
            long longValue2 = longValue.longValue();
            if (longValue2 > 0) {
                notificationManagerCompat.cancel(NOTIFICATION_ALARM_TAG, (int) longValue2);
            }
        }
    }

    public static void clearAlarmNotificationForCheckerRecord(Context context, CheckerRecord checkerRecord) {
        clearAlarmNotificationForCheckerRecord(context, NotificationManagerCompat.from(context), checkerRecord);
    }

    public static void clearNotificationsForCheckerRecord(Context context, CheckerRecord checkerRecord) {
        NotificationManagerCompat from = NotificationManagerCompat.from(context);
        clearOngoingNotificationForCheckerRecord(context, from, checkerRecord);
        clearAlarmNotificationForCheckerRecord(context, from, checkerRecord);
    }

    public static void clearOngoingNotification(Context context, CheckerRecord checkerRecord) {
        clearOngoingNotificationForCheckerRecord(context, NotificationManagerCompat.from(context), checkerRecord);
    }

    private static void clearOngoingNotificationForCheckerRecord(Context context,
                                                                 NotificationManagerCompat notificationManagerCompat,
                                                                 CheckerRecord checkerRecord) {
        notificationManagerCompat.cancel(NOTIFICATION_CHECKER_TAG, (int) checkerRecord.getId());
    }

    private static PendingIntent createAlarmDismissPendingIntent(Context context,
                                                                 CheckerRecord checkerRecord,
                                                                 AlarmRecord alarmRecord) {
        Intent intent = new Intent(NotificationAndWidgetReceiver.ACTION_NOTIFICATION_ALARM_DISMISS,
                                   null,
                                   context,
                                   NotificationAndWidgetReceiver.class);
        intent.putExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, checkerRecord.getId());
        intent.putExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, alarmRecord.getId());
        return PendingIntent.getBroadcast(context, (int) alarmRecord.getId(), intent, FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent createCheckerAlertDetailsPendingIntent(Context context,
                                                                        CheckerRecord checkerRecord,
                                                                        AlarmRecord alarmRecord) {
        Intent intent = new Intent(NotificationAndWidgetReceiver.ACTION_NOTIFICATION_CHECKER_ALARM_DETAILS,
                                   null,
                                   context,
                                   NotificationAndWidgetReceiver.class);
        intent.putExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, checkerRecord.getId());
        intent.putExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, alarmRecord.getId());
        return PendingIntent.getBroadcast(context, (int) alarmRecord.getId(), intent, 0);
    }

    private static PendingIntent createCheckerDetailsPendingIntent(Context context, CheckerRecord checkerRecord) {
        Intent intent = new Intent(context, CheckerAddActivity.class);
        intent.putExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, checkerRecord.getId());
        return PendingIntent.getActivity(context, (int) checkerRecord.getId(), intent, 0);
    }

    public static PendingIntent createMainActivityPendingIntent(Context context) {
        Intent intent = new Intent(context, CheckersListActivity.class);
        intent.setFlags(603979776);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    public static int getIconBackgroundColorResIdForTickersLollipop(Ticker ticker, Ticker ticker2) {
        return (ticker == null || ticker2 == null || ticker.last == ticker2.last)
               ? R.color.circle_gray
               : ticker2.last > ticker.last ? R.color.circle_green : R.color.circle_red;
    }

    private static int getIconBackgroundResIdForTickersLollipop(Ticker ticker, Ticker ticker2) {
        return (ticker == null || ticker2 == null || ticker.last == ticker2.last)
               ? R.drawable.circle_gray
               : ticker2.last > ticker.last ? R.drawable.circle_green : R.drawable.circle_red;
    }

    private static int getIconResIdForNotification(Ticker ticker, Ticker ticker2) {
        Object obj = VERSION.SDK_INT >= 21 ? 1 : null;
        return (ticker == null || ticker2 == null || ticker.last == ticker2.last)
               ? obj != null
                 ? R.drawable.ic_notify_right_white
                 : R.drawable.ic_notify_right
               : ticker2.last > ticker.last
                 ? obj != null ? R.drawable.ic_notify_up_white : R.drawable.ic_notify_up
                 : obj != null ? R.drawable.ic_notify_down_white : R.drawable.ic_notify_down;
    }

    public static int getIconResIdForTickers(Ticker ticker, Ticker ticker2, boolean z) {
        return (ticker == null || ticker2 == null || ticker.last == ticker2.last)
               ? z
                 ? R.drawable.ic_notify_right_gray
                 : R.drawable.ic_notify_right
               : ticker2.last > ticker.last ? R.drawable.ic_notify_up : R.drawable.ic_notify_down;
    }

    public static void refreshOngoingNotifications(Context context) {
        for (ActiveRecord showOngoingNotification : SQuery.newQuery()
                                                          .expr(MaindbContract.CheckerColumns.ENABLED,
                                                                SQuery.Op.EQ,
                                                                true)
                                                          .and()
                                                          .expr(MaindbContract.CheckerColumns.NOTIFICATION_PRIORITY,
                                                                SQuery.Op.GTEQ,
                                                                -2)
                                                          .select(MaindbContract.Checker.CONTENT_URI)) {
            showOngoingNotification(context, (CheckerRecord) showOngoingNotification, true);
        }
    }

    @TargetApi(16)
    public static void showOngoingNotification(Context context, CheckerRecord checkerRecord, boolean z) {
        //    NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        //    if (object == null) {
        //        return;
        //    }
        //    if (!object.getEnabled()) {
        //        notificationManagerCompat.cancel(NOTIFICATION_CHECKER_TAG, (int) object.getId());
        //        return;
        //    }
        //    Market market = MarketsConfigUtils.getMarketByKey(object.getMarketKey());
        //    Ticker ticker = TickerUtils.fromJson(object.getLastCheckTicker());
        //    if (market == null) return;
        //    if (ticker == null) {
        //        if (object.getErrorMsg() == null) return;
        //    }
        //    Object object2 = null;
        //    Object object3 =
        //        FormatUtils.getCurrencySrcWithContractType(object.getCurrencySrc(), market, (int) object.getContractType());
        //    Object object4 = object.getCurrencyDst();
        //    CurrencySubunit currencySubunit =
        //        CurrencyUtils.getCurrencySubunit((String) object4, object.getCurrencySubunitDst());
        //    if (object.getNotificationPriority() >= -2) {
        //        long l;
        //        Object object5;
        //        Object string2;
        //        int n;
        //        Object string3;
        //        int n2;
        //        if (object.getErrorMsg() != null) {
        //            n = NotificationUtils.getIconResIdForNotification(null, null);
        //            n2 = NotificationUtils.getIconBackgroundResIdForTickersLollipop(null, null);
        //            l = object.getLastCheckDate();
        //            string3 = context.getString(R.string.notification_ongoing_title_error,
        //
        //                                        context.getString(R.string.generic_currency_pair, object3, object4),
        //                                        market.name);
        //            string2 = context.getString(R.string.check_error_generic_prefix, object.getErrorMsg());
        //            object3 = string2;
        //            object4 = object3;
        //        } else {
        //            if (ticker == null) return;
        //            object5 = TickerUtils.fromJson(object.getPreviousCheckTicker());
        //            n = NotificationUtils.getIconResIdForNotification((Ticker) object5, ticker);
        //            n2 = NotificationUtils.getIconBackgroundResIdForTickersLollipop((Ticker) object5, ticker);
        //            l = ticker.timestamp;
        //            string2 = Html.fromHtml(context.getString(R.string.notification_ongoing_title,
        //                                                      object3,
        //                                                      "<b>"
        //                                                          + FormatUtils.formatPriceWithCurrency(ticker.last,
        //                                                                                                currencySubunit)
        //                                                          + "</b>",
        //                                                      market.name));
        //            string3 = string2;
        //            object4 = object3 = null;
        //            if (ticker.high >= 0.0) {
        //                object4 = object3;
        //                if (ticker.low >= 0.0) {
        //                    object4 = Html.fromHtml(context.getString(R.string.notification_ongoing_text,
        //                                                              "<b>"
        //                                                                  + FormatUtils.formatPriceWithCurrency(ticker.high,
        //                                                                                                        currencySubunit)
        //                                                                  + "</b>",
        //                                                              "<b>"
        //                                                                  + FormatUtils.formatPriceWithCurrency(ticker.low,
        //                                                                                                        currencySubunit)
        //                                                                  + "</b>"));
        //                }
        //            }
        //            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        //            if (ticker.bid >= 0.0) {
        //                spannableStringBuilder.append(Html.fromHtml(context.getString(R.string.notification_ongoing_text_bid,
        //
        //                                                                              "<b>"
        //                                                                                  + FormatUtils.formatPriceWithCurrency(
        //                                                                                  ticker.bid,
        //                                                                                  currencySubunit)
        //                                                                                  + "</b>")));
        //            }
        //            if (ticker.ask >= 0.0) {
        //                if (spannableStringBuilder.length() > 0) {
        //                    spannableStringBuilder.append(context.getString(R.string.notification_ongoing_text_separator));
        //                }
        //                spannableStringBuilder.append(Html.fromHtml(context.getString(R.string.notification_ongoing_text_ask,
        //
        //                                                                              "<b>"
        //                                                                                  + FormatUtils.formatPriceWithCurrency(
        //                                                                                  ticker.ask,
        //                                                                                  currencySubunit)
        //                                                                                  + "</b>")));
        //            } object2 = null;
        //            if (ticker.vol >= 0.0) {
        //                object2 = Html.fromHtml(context.getString(R.string.notification_ongoing_text_volume,
        //                                                          "<b>"
        //                                                              + FormatUtils.formatPriceWithCurrency(ticker.vol,
        //                                                                                                    object.getCurrencySrc())
        //                                                              + "</b>"));
        //            }
        //            String string4 = "";
        //            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
        //            object3 = string4;
        //            if (object4 != null) {
        //                object3 = string4;
        //                if (((String) object4).length() > 0) {
        //                    object3 = object4;
        //                    spannableStringBuilder2.append((CharSequence) object4);
        //                }
        //            }
        //            object4 = object3;
        //            if (spannableStringBuilder != null) {
        //                object4 = object3;
        //                if (spannableStringBuilder.length() > 0) {
        //                    if (spannableStringBuilder2.length() > 0) {
        //                        spannableStringBuilder2.append("\n");
        //                    } else {
        //                        object3 = spannableStringBuilder;
        //                    }
        //                    spannableStringBuilder2.append(spannableStringBuilder);
        //                    object4 = object3;
        //                }
        //            }
        //            object3 = object4;
        //            if (object2 != null) {
        //                object3 = object4;
        //                if (((String) object2).length() > 0) {
        //                    if (spannableStringBuilder2.length() > 0) {
        //                        spannableStringBuilder2.append("\n");
        //                        object3 = object4;
        //                    } else {
        //                        object3 = object2;
        //                    }
        //                    spannableStringBuilder2.append((CharSequence) object2);
        //                }
        //            }
        //            if (TextUtils.isEmpty(spannableStringBuilder2)) {
        //                object3 = new SpannedString(context.getString(R.string.notification_ongoing_text_no_info));
        //                spannableStringBuilder2.append((CharSequence) object3);
        //            }
        //            object4 = spannableStringBuilder2;
        //            object2 = object5;
        //        } NotificationCompat.Builder object6 = new NotificationCompat.Builder(context).setSmallIcon(n)
        //                                                                                      .setContentTitle((String) string3)
        //                                                                                      .setContentText((CharSequence) object3)
        //                                                                                      .setOngoing(true)
        //                                                                                      .setOnlyAlertOnce(true)
        //                                                                                      .setWhen(l)
        //                                                                                      .setPriority(-2)
        //                                                                                      .setContentIntent(
        //                                                                                          NotificationUtils.createMainActivityPendingIntent(
        //                                                                                              context));
        //        if (PreferencesUtils.getCheckNotificationTicker(context)) {
        //            object6.setTicker((String) string2);
        //        }
        //        RemoteViews object7;
        //        if (Build.VERSION.SDK_INT >= 14 && PreferencesUtils.getCheckNotificationCustomLayout(context)) {
        //            object7 = new RemoteViews(context.getPackageName(), R.layout.notification_ongoing);
        //            object7.setImageViewResource(R.id.iconView, n);
        //            if (Build.VERSION.SDK_INT >= 21) {
        //                object7.setInt(R.id.iconView, "setBackgroundResource", n2);
        //                object6.setColor(context.getResources()
        //                                        .getColor(NotificationUtils.getIconBackgroundColorResIdForTickersLollipop((Ticker) object2,
        //                                                                                                                  ticker)));
        //            }
        //            object7.setTextViewText(R.id.notificationTitle, (CharSequence) string3);
        //            object7.setTextViewText(R.id.notificationText, (CharSequence) object3);
        //            Intent intent = new Intent(NotificationAndWidgetReceiver.ACTION_NOTIFICATION_REFRESH,
        //                                       null,
        //                                       context,
        //                                       NotificationAndWidgetReceiver.class);
        //            intent.putExtra("checker_record_id", object.getId());
        //            object7.setOnClickPendingIntent(R.id.refreshButton,
        //                                            PendingIntent.getBroadcast(context,
        //                                                                       ((int) object.getId()),
        //                                                                       intent,
        //                                                                       FLAG_UPDATE_CURRENT));
        //            object6.setContent((RemoteViews) object7);
        //        }
        //        if (Build.VERSION.SDK_INT >= 16 && PreferencesUtils.getCheckNotificationExpandable(context)) {
        //
        //            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        //            bigTextStyle.setBigContentTitle((CharSequence) string3);
        //            bigTextStyle.bigText((CharSequence) object4);
        //            object6.setStyle(bigTextStyle);
        //            object6.addAction(R.drawable.ic_notify_edit,
        //                              context.getString(R.string.generic_edit),
        //                              NotificationUtils.createCheckerDetailsPendingIntent(context, object));
        //
        //            Intent intent = new Intent(NotificationAndWidgetReceiver.ACTION_NOTIFICATION_REFRESH,
        //                                       null,
        //                                       context,
        //                                       NotificationAndWidgetReceiver.class);
        //            intent.putExtra("checker_record_id", object.getId());
        //            PendingIntent pendingIntent =
        //                PendingIntent.getBroadcast(context, ((int) object.getId()), intent, FLAG_UPDATE_CURRENT);
        //            object6.addAction(R.drawable.ic_notify_refresh,
        //                              context.getString(R.string.generic_refresh),
        //                              pendingIntent);
        //        }
        //        notificationManagerCompat.notify("checker", (int) object.getId(), object6.build());
        //    } else {
        //        notificationManagerCompat.cancel("checker", (int) object.getId());
        //    } if (bl) return;
        //    if (!object.getTtsEnabled()) return;
        //    if (ticker == null) return;
        //    if (object.getErrorMsg() != null) return;
        //    String string = FormatUtils.formatTextForTTS(context, ticker.last, object, currencySubunit, market);
        //    TTSHelper.speak(context.getApplicationContext(), string);
        //}

        NotificationManagerCompat from = NotificationManagerCompat.from(context);
        if (checkerRecord != null) {
            if (checkerRecord.getEnabled()) {
                Market marketByKey = MarketsConfigUtils.getMarketByKey(checkerRecord.getMarketKey());
                Ticker fromJson = TickerUtils.fromJson(checkerRecord.getLastCheckTicker());
                if (marketByKey == null) {
                    return;
                }
                if (fromJson != null || checkerRecord.getErrorMsg() != null) {
                    Ticker ticker = null;
                    String currencySrcWithContractType =
                        FormatUtils.getCurrencySrcWithContractType(checkerRecord.getCurrencySrc(),
                                                                   marketByKey,
                                                                   (int) checkerRecord.getContractType());
                    CurrencySubunit currencySubunit = CurrencyUtils.getCurrencySubunit(checkerRecord.getCurrencyDst(),
                                                                                       checkerRecord.getCurrencySubunitDst());
                    if (checkerRecord.getNotificationPriority() >= -2) {
                        long lastCheckDate;
                        CharSequence string;
                        CharSequence string2;
                        int i;
                        int i2;
                        CharSequence charSequence;
                        CharSequence charSequence2;
                        if (checkerRecord.getErrorMsg() != null) {
                            int iconResIdForNotification = getIconResIdForNotification(null, null);
                            int iconBackgroundResIdForTickersLollipop =
                                getIconBackgroundResIdForTickersLollipop(null, null);
                            lastCheckDate = checkerRecord.getLastCheckDate();

                            string = context.getString(R.string.notification_ongoing_title_error,
                                                       context.getString(R.string.generic_currency_pair,
                                                                         currencySrcWithContractType,
                                                                         checkerRecord.getCurrencyDst()),
                                                       marketByKey.name);
                            string2 =
                                context.getString(R.string.check_error_generic_prefix, checkerRecord.getErrorMsg());
                            i = iconResIdForNotification;
                            i2 = iconBackgroundResIdForTickersLollipop;
                            charSequence = string2;
                            charSequence2 = string2;
                        } else if (fromJson != null) {
                            Object[] objArr;
                            Ticker fromJson2 = TickerUtils.fromJson(checkerRecord.getPreviousCheckTicker());
                            i2 = getIconResIdForNotification(fromJson2, fromJson);
                            int iconBackgroundResIdForTickersLollipop2 =
                                getIconBackgroundResIdForTickersLollipop(fromJson2, fromJson);
                            lastCheckDate = fromJson.timestamp;
                            Object[] r4 = new Object[3];
                            r4[0] = FormatUtils.getCurrencySrcWithContractType(checkerRecord.getCurrencySrc(),
                                                                               marketByKey,
                                                                               (int) checkerRecord.getContractType());
                            r4[1] = "<b>"
                                + FormatUtilsBase.formatPriceWithCurrency(fromJson.last, currencySubunit)
                                + "</b>";
                            r4[2] = marketByKey.name;
                            string = Html.fromHtml(context.getString(R.string.notification_ongoing_title, r4));
                            string2 = null;
                            if (fromJson.high >= 0.0d) {
                                if (fromJson.low >= 0.0d) {
                                    Object[] r3 = new Object[2];
                                    r3[0] = "<b>"
                                        + FormatUtilsBase.formatPriceWithCurrency(fromJson.high, currencySubunit)
                                        + "</b>";
                                    r3[1] = "<b>"
                                        + FormatUtilsBase.formatPriceWithCurrency(fromJson.low, currencySubunit)
                                        + "</b>";
                                    string2 = Html.fromHtml(context.getString(R.string.notification_ongoing_text, r3));
                                }
                            }
                            charSequence = new SpannableStringBuilder();
                            if (fromJson.bid >= 0.0d) {
                                objArr = new Object[1];
                                objArr[0] = "<b>"
                                    + FormatUtilsBase.formatPriceWithCurrency(fromJson.bid, currencySubunit)
                                    + "</b>";
                                ((SpannableStringBuilder) charSequence).append(Html.fromHtml(context.getString(R.string.notification_ongoing_text_bid,
                                                                                                               objArr)));
                            }
                            if (fromJson.ask >= 0.0d) {
                                if (charSequence.length() > 0) {
                                    ((SpannableStringBuilder) charSequence).append(context.getString(R.string.notification_ongoing_text_separator));
                                }
                                objArr = new Object[1];
                                objArr[0] = "<b>"
                                    + FormatUtilsBase.formatPriceWithCurrency(fromJson.ask, currencySubunit)
                                    + "</b>";
                                ((SpannableStringBuilder) charSequence).append(Html.fromHtml(context.getString(R.string.notification_ongoing_text_ask,
                                                                                                               objArr)));
                            }
                            CharSequence charSequence3 = null;
                            if (fromJson.vol >= 0.0d) {
                                objArr = new Object[1];
                                objArr[0] = "<b>"
                                    + FormatUtilsBase.formatPriceWithCurrency(fromJson.vol,
                                                                              checkerRecord.getCurrencySrc())
                                    + "</b>";
                                charSequence3 =
                                    Html.fromHtml(context.getString(R.string.notification_ongoing_text_volume, objArr));
                            }
                            String str = BuildConfig.VERSION_NAME;
                            CharSequence spannableStringBuilder = new SpannableStringBuilder();
                            if (string2 == null || string2.length() <= 0) {
                                Object obj = str;
                            } else {
                                ((SpannableStringBuilder) spannableStringBuilder).append(string2);
                            }
                            if (charSequence != null && charSequence.length() > 0) {
                                if (spannableStringBuilder.length() > 0) {
                                    ((SpannableStringBuilder) spannableStringBuilder).append("\n");
                                } else {
                                    string2 = charSequence;
                                }
                                ((SpannableStringBuilder) spannableStringBuilder).append(charSequence);
                            }
                            if (charSequence3 != null && charSequence3.length() > 0) {
                                if (spannableStringBuilder.length() > 0) {
                                    ((SpannableStringBuilder) spannableStringBuilder).append("\n");
                                } else {
                                    string2 = charSequence3;
                                }
                                ((SpannableStringBuilder) spannableStringBuilder).append(charSequence3);
                            }
                            if (TextUtils.isEmpty(spannableStringBuilder)) {
                                string2 =
                                    new SpannedString(context.getString(R.string.notification_ongoing_text_no_info));
                                ((SpannableStringBuilder) spannableStringBuilder).append(string2);
                            }
                            ticker = fromJson2;
                            charSequence = spannableStringBuilder;
                            charSequence2 = string2;
                            i = i2;
                            string2 = string;
                            i2 = iconBackgroundResIdForTickersLollipop2;
                        } else {
                            return;
                        }
                        Builder contentIntent = new Builder(context).setSmallIcon(i)
                                                                    .setContentTitle(string)
                                                                    .setContentText(charSequence2)
                                                                    .setOngoing(true)
                                                                    .setOnlyAlertOnce(true)
                                                                    .setWhen(lastCheckDate)
                                                                    .setPriority(-2)
                                                                    .setContentIntent(createMainActivityPendingIntent(
                                                                        context));
                        if (PreferencesUtils.getCheckNotificationTicker(context)) {
                            contentIntent.setTicker(string2);
                        }
                        if (VERSION.SDK_INT >= 14 && PreferencesUtils.getCheckNotificationCustomLayout(context)) {
                            RemoteViews remoteViews =
                                new RemoteViews(context.getPackageName(), R.layout.notification_ongoing);
                            remoteViews.setImageViewResource(R.id.iconView, i);
                            if (VERSION.SDK_INT >= 21) {
                                remoteViews.setInt(R.id.iconView, "setBackgroundResource", i2);
                                contentIntent.setColor(context.getResources()
                                                              .getColor(getIconBackgroundColorResIdForTickersLollipop(
                                                                  ticker,
                                                                  fromJson)));
                            }
                            remoteViews.setTextViewText(R.id.notificationTitle, string);
                            remoteViews.setTextViewText(R.id.notificationText, charSequence2);
                            Intent intent = new Intent(NotificationAndWidgetReceiver.ACTION_NOTIFICATION_REFRESH,
                                                       null,
                                                       context,
                                                       NotificationAndWidgetReceiver.class);
                            intent.putExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, checkerRecord.getId());
                            remoteViews.setOnClickPendingIntent(R.id.refreshButton,
                                                                PendingIntent.getBroadcast(context,
                                                                                           (int) checkerRecord.getId(),
                                                                                           intent,
                                                                                           FLAG_UPDATE_CURRENT));
                            contentIntent.setContent(remoteViews);
                        }
                        if (VERSION.SDK_INT >= 16 && PreferencesUtils.getCheckNotificationExpandable(context)) {
                            NotificationCompat.Style bigTextStyle = new NotificationCompat.BigTextStyle();
                            ((NotificationCompat.BigTextStyle) bigTextStyle).setBigContentTitle(string);
                            ((NotificationCompat.BigTextStyle) bigTextStyle).bigText(charSequence);
                            contentIntent.setStyle(bigTextStyle);
                            contentIntent.addAction(R.drawable.ic_notify_edit,
                                                    context.getString(R.string.generic_edit),
                                                    createCheckerDetailsPendingIntent(context, checkerRecord));
                            Intent intent2 = new Intent(NotificationAndWidgetReceiver.ACTION_NOTIFICATION_REFRESH,
                                                        null,
                                                        context,
                                                        NotificationAndWidgetReceiver.class);
                            intent2.putExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, checkerRecord.getId());
                            contentIntent.addAction(R.drawable.ic_notify_refresh,
                                                    context.getString(R.string.generic_refresh),
                                                    PendingIntent.getBroadcast(context,
                                                                               (int) checkerRecord.getId(),
                                                                               intent2,
                                                                               FLAG_UPDATE_CURRENT));
                        }
                        from.notify(NOTIFICATION_CHECKER_TAG, (int) checkerRecord.getId(), contentIntent.build());
                    } else {
                        from.cancel(NOTIFICATION_CHECKER_TAG, (int) checkerRecord.getId());
                    }
                    if (!z
                        && checkerRecord.getTtsEnabled()
                        && fromJson != null
                        && checkerRecord.getErrorMsg() == null) {
                        TTSHelper.speak(context.getApplicationContext(),
                                        FormatUtils.formatTextForTTS(context,
                                                                     fromJson.last,
                                                                     checkerRecord,
                                                                     currencySubunit,
                                                                     marketByKey));
                        return;
                    }
                    return;
                }
                return;
            }
            from.cancel(NOTIFICATION_CHECKER_TAG, (int) checkerRecord.getId());
        }
    }
}

