package haivo.us.crypto.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import haivo.us.crypto.R;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.util.PreferencesUtils;

public class MarketPickerListAdapter extends BaseAdapter implements Filterable, OnItemLongClickListener {
    private final int checkMarkDrawableResId;
    private final Context context;
    private List<Market> filteredItems;
    private List<Market> items;
    private String marketDefault;
    private String searchQuery;
    private final String selectedMarketKey;

    /* renamed from: haivo.us.crypto.adapter.MarketPickerListAdapter.1 */
    class C01621 extends Filter {
        C01621() {
        }

        protected FilterResults performFiltering(CharSequence newSearchQuery) {
            MarketPickerListAdapter.this.searchQuery = (String) newSearchQuery;
            FilterResults filterResults = new FilterResults();
            ArrayList<Market> markets = new ArrayList();
            if (TextUtils.isEmpty(MarketPickerListAdapter.this.searchQuery)) {
                markets.addAll(MarketPickerListAdapter.this.items);
            } else if (!MarketPickerListAdapter.this.isEmpty()) {
                for (Market market : MarketPickerListAdapter.this.items) {
                    if (market.name.toLowerCase(Locale.US)
                                   .contains(MarketPickerListAdapter.this.searchQuery.toLowerCase(Locale.US))) {
                        markets.add(market);
                    }
                }
            }
            filterResults.values = markets;
            return filterResults;
        }

        protected void publishResults(CharSequence newSearchQuery, FilterResults results) {
            MarketPickerListAdapter.this.filteredItems = (ArrayList) results.values;
            MarketPickerListAdapter.this.notifyDataSetChanged();
        }
    }

    public MarketPickerListAdapter(Activity context, String selectedMarketKey) {
        this.items = null;
        this.filteredItems = null;
        this.context = context;
        this.selectedMarketKey = selectedMarketKey;
        this.marketDefault = PreferencesUtils.getMarketDefault(context);
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(16843289, value, true);
        this.checkMarkDrawableResId = value.resourceId;
    }

    public void swapItems(List<Market> items) {
        this.items = items;
        this.filteredItems = items;
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.filteredItems != null ? this.filteredItems.size() + 1 : 0;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public Market getItem(int position) {
        return position < this.filteredItems.size() ? (Market) this.filteredItems.get(position) : null;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        boolean z = false;
        if (convertView == null) {
            convertView =
                LayoutInflater.from(this.context).inflate(R.layout.market_picker_list_singlechoice, parent, false);
        }
        TextView marketNameView = (TextView) convertView.findViewById(R.id.marketNameView);
        TextView defaultView = (TextView) convertView.findViewById(R.id.defaultView);
        CheckedTextView singleCheckView = (CheckedTextView) convertView.findViewById(R.id.singleCheckView);
        Market market = getItem(position);
        if (market != null) {
            CharSequence name;
            if (TextUtils.isEmpty(this.searchQuery)) {
                name = market.name;
            } else {
                name =
                    Html.fromHtml(market.name.replaceAll("(?i)(" + Pattern.quote(this.searchQuery) + ")", "<b>$1</b>"));
            }
            marketNameView.setText(name);
            if (market.key.equals(this.marketDefault)) {
                defaultView.setVisibility(View.VISIBLE);
            } else {
                defaultView.setVisibility(View.GONE);
            }
            if (market.key != null && market.key.equals(this.selectedMarketKey)) {
                z = true;
            }
            singleCheckView.setChecked(z);
            singleCheckView.setCheckMarkDrawable(this.checkMarkDrawableResId);
            singleCheckView.setVisibility(View.VISIBLE);
        } else {
            marketNameView.setText(R.string.checker_add_check_market_suggest_more);
            defaultView.setVisibility(View.GONE);
            singleCheckView.setChecked(false);
            singleCheckView.setVisibility(View.GONE);
        }
        return convertView;
    }

    public Filter getFilter() {
        return new C01621();
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (getItem(position) == null) {
            return false;
        }
        this.marketDefault = getItem(position).key;
        PreferencesUtils.setMarketDefault(this.context, this.marketDefault);
        notifyDataSetChanged();
        return true;
    }
}
