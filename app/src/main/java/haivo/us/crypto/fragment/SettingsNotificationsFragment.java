package haivo.us.crypto.fragment;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.support.v4.preference.PreferenceFragment;
import haivo.us.crypto.R;
import haivo.us.crypto.util.NotificationUtils;
import haivo.us.crypto.util.PreferencesUtils;

public class SettingsNotificationsFragment extends PreferenceFragment {

    /* renamed from: haivo.us.crypto.fragment.SettingsNotificationsFragment.1 */
    class C01881 implements OnPreferenceChangeListener {
        C01881() {
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            PreferencesUtils.setCheckNotificationExpandable(SettingsNotificationsFragment.this.getActivity(),
                                                            ((Boolean) newValue).booleanValue());
            NotificationUtils.refreshOngoingNotifications(SettingsNotificationsFragment.this.getActivity());
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsNotificationsFragment.2 */
    class C01892 implements OnPreferenceChangeListener {
        C01892() {
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            PreferencesUtils.setCheckNotificationCustomLayout(SettingsNotificationsFragment.this.getActivity(),
                                                              ((Boolean) newValue).booleanValue());
            NotificationUtils.refreshOngoingNotifications(SettingsNotificationsFragment.this.getActivity());
            return true;
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_notifications);
        PreferenceCategory checkNotificationCategoryPreference =
            (PreferenceCategory) findPreference(getString(R.string.settings_check_notification_category_key));
        Preference checkNotificationExpandablePreference =
            findPreference(getString(R.string.settings_check_notification_expandable_key));
        if (VERSION.SDK_INT < 16) {
            checkNotificationCategoryPreference.removePreference(checkNotificationExpandablePreference);
        } else {
            checkNotificationExpandablePreference.setOnPreferenceChangeListener(new C01881());
        }
        Preference checkNotificationCustomLayoutPreference =
            findPreference(getString(R.string.settings_check_notification_custom_layout_key));
        if (VERSION.SDK_INT < 14) {
            checkNotificationCategoryPreference.removePreference(checkNotificationCustomLayoutPreference);
        } else {
            checkNotificationCustomLayoutPreference.setOnPreferenceChangeListener(new C01892());
        }
    }
}
