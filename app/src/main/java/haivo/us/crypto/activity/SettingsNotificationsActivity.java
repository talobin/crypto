package haivo.us.crypto.activity;

import haivo.us.crypto.activity.generic.SimpleFragmentSubActivity;
import haivo.us.crypto.fragment.SettingsNotificationsFragment;

public class SettingsNotificationsActivity extends SimpleFragmentSubActivity<SettingsNotificationsFragment> {
    protected SettingsNotificationsFragment createChildFragment() {
        return new SettingsNotificationsFragment();
    }
}
