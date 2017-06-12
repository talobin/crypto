package haivo.us.crypto.activity;

import haivo.us.crypto.activity.generic.SimpleDonateFragmentSubActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import haivo.us.crypto.R;
import haivo.us.crypto.util.Utils;

public class SuggestNewExchangeActivity extends SimpleDonateFragmentSubActivity<Fragment> {
    public static final String GITHUB_URL = "https://github.com/talobin/crypto";
    @BindView(R.id.suggestButton) Button suggestButton;
    @BindView(R.id.textView) TextView textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        this.textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    protected Fragment createChildFragment() {
        return null;
    }

    protected int getContentViewResId() {
        return R.layout.suggest_new_exchange_activity;
    }

    @OnClick(R.id.donateButton)
    public void onDonateButtonClicked() {
        showDonateDialog();
    }

    @OnClick(R.id.suggestButton)
    public void onSuggestButtonClicked() {
        Utils.goToWebPage(this, GITHUB_URL);
    }

    public static void startSettingsMainActivity(Context context) {
        SimpleDonateFragmentSubActivity.startSimpleDonateFragmentActivity(context,
                                                                          SuggestNewExchangeActivity.class,
                                                                          false);
    }
}
