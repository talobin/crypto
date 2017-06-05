package haivo.us.crypto.activity;

import haivo.us.crypto.activity.generic.SimpleDonateFragmentSubActivity;
import android.content.Context;
import android.os.Bundle;
import haivo.us.crypto.fragment.SettingsMainFragment;
import haivo.us.crypto.util.SoundFilesManager;

public class SettingsMainActivity extends SimpleDonateFragmentSubActivity<SettingsMainFragment> {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SoundFilesManager.installRingtonesIfNeeded(this);
    }

    protected SettingsMainFragment createChildFragment() {
        return new SettingsMainFragment();
    }

    public static void startSettingsMainActivity(Context context, boolean showDonateDialog) {
        SimpleDonateFragmentSubActivity.startSimpleDonateFragmentActivity(context,
                                                                          SettingsMainActivity.class,
                                                                          showDonateDialog);
    }
}
