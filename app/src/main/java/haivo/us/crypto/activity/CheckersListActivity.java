package haivo.us.crypto.activity;

import haivo.us.crypto.activity.generic.SimpleFragmentActivity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import haivo.us.crypto.appwidget.WidgetProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import haivo.us.crypto.fragment.CheckersListFragment;
import haivo.us.crypto.R;
import haivo.us.crypto.receiver.MarketChecker;
import haivo.us.crypto.util.PreferencesUtils;
import haivo.us.crypto.util.Utils;
import com.tjeannin.apprate.AppRate;

public class CheckersListActivity extends SimpleFragmentActivity<CheckersListFragment> {
    private static final int CURRENT_NEWS_ID = 3;
    @BindView(R.id.donateBar) View donateBar;
    private boolean wasDialogShown;

    /* renamed from: haivo.us.crypto.activity.CheckersListActivity.1 */
    class C01501 implements OnClickListener {
        C01501() {
        }

        public void onClick(DialogInterface dialog, int which) {
            CheckersListActivity.this.wasDialogShown = true;
            PreferencesUtils.setDefaultItemAdded(CheckersListActivity.this);
            CheckerAddActivity.startCheckerAddActivity(CheckersListActivity.this, null);
        }
    }

    /* renamed from: haivo.us.crypto.activity.CheckersListActivity.2 */
    class C01512 implements OnClickListener {
        C01512() {
        }

        public void onClick(DialogInterface dialog, int which) {
            CheckersListActivity.this.wasDialogShown = true;
            PreferencesUtils.setNewsShown(CheckersListActivity.this, CheckersListActivity.CURRENT_NEWS_ID);
            Utils.goToWebPage(CheckersListActivity.this, SuggestNewExchangeActivity.GITHUB_URL);
        }
    }

    /* renamed from: haivo.us.crypto.activity.CheckersListActivity.3 */
    class C01523 implements OnClickListener {
        C01523() {
        }

        public void onClick(DialogInterface dialog, int which) {
            CheckersListActivity.this.wasDialogShown = true;
            PreferencesUtils.setNewsShown(CheckersListActivity.this, CheckersListActivity.CURRENT_NEWS_ID);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String appName = getString(R.string.app_name);
        AppRate appRate =
            new AppRate(this).setShowIfAppHasCrashed(false).setMinDaysUntilPrompt(4).setMinLaunchesUntilPrompt(10);
        appRate.setCustomDialog(new Builder(this).setTitle(getString(R.string.rate_app_title, new Object[] { appName }))
                                                 .setMessage(getResources().getText(R.string.rate_app_text, appName))
                                                 .setPositiveButton(R.string.rate_app_positive, appRate)
                                                 .setNeutralButton(R.string.rate_app_neutral, appRate)
                                                 .setNegativeButton(R.string.rate_app_negative, appRate)
                                                 .setOnCancelListener(appRate));
        appRate.init();
        if (savedInstanceState == null) {
            WidgetProvider.refreshWidget(this);
            MarketChecker.checkIfAlarmsAreSet(this);
        }
        this.wasDialogShown = savedInstanceState != null ? savedInstanceState.getBoolean("wasDialogShown") : false;
        if (!this.wasDialogShown) {
            if (!PreferencesUtils.isDefaultItemAdded(this)) {
                new Builder(this).setCancelable(false)
                                 .setTitle(R.string.checkers_list_first_time_dialog_title)
                                 .setMessage(R.string.checkers_list_first_time_dialog_message)
                                 .setPositiveButton(R.string.checkers_list_add_title, new C01501())
                                 .show();
            } else if (!PreferencesUtils.wasNewsShown(this, CURRENT_NEWS_ID)) {
                new Builder(this).setCancelable(false)
                                 .setTitle(R.string.checker_add_exchange_tutorial2_title)
                                 .setMessage(R.string.checker_add_exchange_tutorial2_text)
                                 .setNeutralButton(R.string.ok, new C01523())
                                 .setPositiveButton(R.string.checkers_list_news_dialog_github_button, new C01512())
                                 .show();
            }
        }
        ButterKnife.bind(this);
    }

    protected void onStart() {
        super.onStart();
        this.donateBar.setVisibility(PreferencesUtils.getDonationMade(this) ? View.GONE : View.VISIBLE);
    }

    protected int getContentViewResId() {
        return R.layout.checkers_list_activity;
    }

    protected CheckersListFragment createChildFragment() {
        return new CheckersListFragment();
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("wasDialogShown", this.wasDialogShown);
        super.onSaveInstanceState(outState);
    }

    public void onDonateClicked(View target) {
        SettingsMainActivity.startSettingsMainActivity(this, true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.checkers_list_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.settingsItem) {
            return super.onOptionsItemSelected(item);
        }
        SettingsMainActivity.startSettingsMainActivity(this, false);
        return true;
    }
}
