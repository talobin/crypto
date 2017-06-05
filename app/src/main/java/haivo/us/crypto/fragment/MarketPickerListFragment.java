package haivo.us.crypto.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import haivo.us.crypto.R;
import haivo.us.crypto.activity.MarketPickerListActivity;
import haivo.us.crypto.activity.SuggestNewExchangeActivity;
import haivo.us.crypto.adapter.MarketPickerListAdapter;
import haivo.us.crypto.config.MarketsConfig;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.util.PreferencesUtils;
import haivo.us.crypto.util.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;

public class MarketPickerListFragment extends ListFragment implements OnQueryTextListener {
    public static final int SORT_MODE_ALPHABETICALLY = 0;
    public static final int SORT_MODE_NEWEST_FIRST = 1;
    private MarketPickerListAdapter adapter;
    private String searchQuery;
    private SearchView searchView;

    /* renamed from: haivo.us.crypto.fragment.MarketPickerListFragment.1 */
    class C01781 implements Comparator<Market> {
        C01781() {
        }

        public int compare(Market lhs, Market rhs) {
            if (lhs.name == null || rhs == null) {
                return MarketPickerListFragment.SORT_MODE_ALPHABETICALLY;
            }
            return lhs.name.compareToIgnoreCase(rhs.name);
        }
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.adapter = new MarketPickerListAdapter(getActivity(), getActivity().getIntent().getStringExtra(MarketPickerListActivity.EXTRA_MARKET_KEY));
        getListView().setOnItemLongClickListener(this.adapter);
        this.searchQuery = savedInstanceState != null ? savedInstanceState.getString("searchQuery") : null;
        setHasOptionsMenu(true);
        getNewList();
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString("searchQuery", this.searchQuery);
        super.onSaveInstanceState(outState);
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.adapter = null;
    }

    private void getNewList() {
        boolean z = true;
        if (PreferencesUtils.getMarketPickerListSortMode(getActivity()) != SORT_MODE_NEWEST_FIRST) {
            z = false;
        }
        getNewList(z);
    }

    private void getNewList(boolean newestFirst) {
        List<Market> items = new ArrayList();
        Collection<Market> marketsList = MarketsConfig.MARKETS.values();
        if (newestFirst) {
            for (Market market : marketsList) {
                items.add(SORT_MODE_ALPHABETICALLY, market);
            }
        } else {
            items.addAll(marketsList);
            Collections.sort(items, new C01781());
        }
        setNewList(items);
    }

    private void setNewList(List<Market> items) {
        this.adapter.swapItems(items);
        this.adapter.getFilter().filter(this.searchQuery);
        setListAdapter(this.adapter);
    }

    public void onListItemClick(ListView l, View v, int position, long id) {
        if (this.adapter != null) {
            Market market = this.adapter.getItem(position);
            if (market == null) {
                onSuggestMoreExchangesClicked();
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(MarketPickerListActivity.EXTRA_MARKET_KEY, market.key);
            getActivity().setResult(-1, intent);
            getActivity().finish();
        }
    }

    private void onSuggestMoreExchangesClicked() {
        SuggestNewExchangeActivity.startSettingsMainActivity(getActivity());
    }

    public boolean onBackPressed() {
        if (this.searchView == null || this.searchView.isIconified()) {
            return false;
        }
        this.searchView.setQuery(BuildConfig.VERSION_NAME, true);
        this.searchView.setIconified(true);
        return true;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        int checkedSortMenuItem;
        inflater.inflate(R.menu.market_picker_list_fragment, menu);
        switch (PreferencesUtils.getMarketPickerListSortMode(getActivity())) {
            case SORT_MODE_NEWEST_FIRST /*1*/:
                checkedSortMenuItem = R.id.sortNewestFirstItem;
                break;
            default:
                checkedSortMenuItem = R.id.sortAlphabeticallyItem;
                break;
        }
        menu.findItem(checkedSortMenuItem).setChecked(true);
        this.searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        this.searchView.setOnQueryTextListener(this);
        this.searchView.setQuery(this.searchQuery, false);
        if (!TextUtils.isEmpty(this.searchQuery)) {
            this.searchView.setIconified(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.sortAlphabeticallyItem) {
            PreferencesUtils.setMarketPickerListSortMode(getActivity(), SORT_MODE_ALPHABETICALLY);
            item.setChecked(true);
            getNewList(false);
            return true;
        } else if (item.getItemId() != R.id.sortNewestFirstItem) {
            return super.onOptionsItemSelected(item);
        } else {
            PreferencesUtils.setMarketPickerListSortMode(getActivity(), SORT_MODE_NEWEST_FIRST);
            item.setChecked(true);
            getNewList(true);
            return true;
        }
    }

    public boolean onQueryTextChange(String searchQuery) {
        this.searchQuery = searchQuery;
        this.adapter.getFilter().filter(searchQuery);
        return true;
    }

    public boolean onQueryTextSubmit(String searchQuery) {
        Utils.hideKeyboard(getActivity(), this.searchView);
        return true;
    }
}
