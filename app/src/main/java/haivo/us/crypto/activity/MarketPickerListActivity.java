package haivo.us.crypto.activity;

import haivo.us.crypto.activity.generic.SimpleFragmentSubActivity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import haivo.us.crypto.fragment.MarketPickerListFragment;

public class MarketPickerListActivity extends SimpleFragmentSubActivity<MarketPickerListFragment> {
    public static final String EXTRA_MARKET_KEY = "market_key";

    protected MarketPickerListFragment createChildFragment() {
        return new MarketPickerListFragment();
    }

    public void onBackPressed() {
        if (!((MarketPickerListFragment) getChildFragment()).onBackPressed()) {
            super.onBackPressed();
        }
    }

    public static void startActivityForResult(Fragment fragment, int requestCode, String marketKey) {
        Intent intent = new Intent(fragment.getActivity(), MarketPickerListActivity.class);
        intent.putExtra(EXTRA_MARKET_KEY, marketKey);
        fragment.startActivityForResult(intent, requestCode);
    }
}
