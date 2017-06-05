package haivo.us.crypto.appwidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.content.MaindbContract.Checker;
import haivo.us.crypto.fragment.CheckersListFragment;
import haivo.us.crypto.R;
import java.util.List;
import haivo.us.crypto.mechanoid.db.SQuery;
import haivo.us.crypto.mechanoid.db.SQuery.Op;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.receiver.MarketChecker;
import haivo.us.crypto.util.FormatUtils;
import haivo.us.crypto.util.FormatUtilsBase;
import haivo.us.crypto.util.MarketsConfigUtils;
import haivo.us.crypto.util.NotificationUtils;
import haivo.us.crypto.util.TickerUtils;

@TargetApi(11)
public class ListProvider implements RemoteViewsFactory {
    private int appWidgetId;
    private Context context;
    private List<CheckerRecord> items;

    public ListProvider(Context context, Intent intent) {
        this.context = null;
        this.context = context;
        this.appWidgetId = intent.getIntExtra("appWidgetId", 0);
    }

    public void onCreate() {
    }

    private void refreshItems() {
        this.items = SQuery.newQuery()
                           .expr(MaindbContract.CheckerColumns.ENABLED, Op.EQ, true)
                           .select(Checker.CONTENT_URI, CheckersListFragment.getSortOrderString(this.context));
    }

    public void onDestroy() {
        this.items = null;
    }

    public RemoteViews getViewAt(int position) {
        long time;
        boolean isCompact = WidgetPrefsUtils.getCompactMode(this.context, this.appWidgetId);
        boolean isDarkTheme = WidgetPrefsUtils.getDarkTheme(this.context, this.appWidgetId);
        int textColorPrimary = this.context.getResources()
                                           .getColor(isDarkTheme
                                                     ? R.color.widget_text_color_primary_dark
                                                     : R.color.widget_text_color_primary_light);
        int textColorSecondary = this.context.getResources()
                                             .getColor(isDarkTheme
                                                       ? R.color.widget_text_color_secondary_dark
                                                       : R.color.widget_text_color_secondary_light);
        RemoteViews remoteViews = new RemoteViews(this.context.getPackageName(),
                                                  isCompact
                                                  ? R.layout.widget_list_item_compact
                                                  : R.layout.widget_list_item);
        CheckerRecord item = (CheckerRecord) this.items.get(position);
        Market market = MarketsConfigUtils.getMarketByKey(item.getMarketKey());
        remoteViews.setTextColor(R.id.marketView, textColorSecondary);
        remoteViews.setTextViewText(R.id.marketView, market.name);
        remoteViews.setTextColor(R.id.currencyView, textColorPrimary);
        String currencySrcWithContractType =
            FormatUtils.getCurrencySrcWithContractType(item.getCurrencySrc(), market, (int) item.getContractType());
        String currencyString = this.context.getString(R.string.generic_currency_pair, new Object[] {
            currencySrcWithContractType, item.getCurrencyDst()
        });
        if (isCompact) {
            currencyString =
                this.context.getString(R.string.checkers_list_item_currency_compact, new Object[] { currencyString });
        }
        remoteViews.setTextViewText(R.id.currencyView, currencyString);
        Ticker lastCheckTicker = TickerUtils.fromJson(item.getLastCheckTicker());
        if (item.getErrorMsg() != null) {
            remoteViews.setTextViewText(R.id.lastCheckValueView, this.context.getString(R.string.check_error_generic));
            remoteViews.setViewVisibility(R.id.priceIconView, 8);
            time = item.getLastCheckDate();
        } else if (lastCheckTicker != null) {
            Ticker previousCheckTicker = TickerUtils.fromJson(item.getPreviousCheckTicker());
            remoteViews.setTextViewText(R.id.lastCheckValueView,
                                        FormatUtils.formatPriceWithCurrency(lastCheckTicker.last, item));
            remoteViews.setImageViewResource(R.id.priceIconView,
                                             NotificationUtils.getIconResIdForTickers(previousCheckTicker,
                                                                                      lastCheckTicker,
                                                                                      !isDarkTheme));
            remoteViews.setViewVisibility(R.id.priceIconView, 0);
            time = lastCheckTicker.timestamp;
        } else {
            remoteViews.setTextViewText(R.id.lastCheckValueView, this.context.getString(R.string.generic_none));
            remoteViews.setViewVisibility(R.id.priceIconView, 8);
            time = -1;
        }
        int i = (isCompact || (lastCheckTicker == null && item.getErrorMsg() == null)) ? 8 : 0;
        remoteViews.setViewVisibility(R.id.lastPriceWrapper, i);
        remoteViews.setTextColor(R.id.lastCheckValueView, textColorSecondary);
        if (!isCompact) {
            remoteViews.setTextColor(R.id.lastCheckView, textColorSecondary);
            if (time == -1) {
                remoteViews.setViewVisibility(R.id.lastCheckTimeView, 8);
            } else {
                remoteViews.setTextColor(R.id.lastCheckTimeView, textColorSecondary);
                remoteViews.setTextViewText(R.id.lastCheckTimeView,
                                            FormatUtilsBase.formatSameDayTimeOrDate(this.context, time));
                remoteViews.setViewVisibility(R.id.lastCheckTimeView, 0);
            }
        }
        Bundle extras = new Bundle();
        extras.putLong(MarketChecker.EXTRA_CHECKER_RECORD_ID, item.getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.widgetItem, fillInIntent);
        return remoteViews;
    }

    public int getCount() {
        return this.items != null ? this.items.size() : 0;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    public void onDataSetChanged() {
        refreshItems();
    }

    public long getItemId(int position) {
        CheckerRecord item = (CheckerRecord) this.items.get(position);
        if (item != null) {
            return item.getId();
        }
        return (long) position;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public boolean hasStableIds() {
        return true;
    }
}
