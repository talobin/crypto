package haivo.us.crypto.activity;

import haivo.us.crypto.activity.generic.SimpleFragmentSubActivity;
import haivo.us.crypto.fragment.SettingsTTSFragment;

public class SettingsTTSActivity extends SimpleFragmentSubActivity<SettingsTTSFragment> {
    protected SettingsTTSFragment createChildFragment() {
        return new SettingsTTSFragment();
    }
}
