package haivo.us.crypto.fragment;

import haivo.us.crypto.activity.SettingsMainActivity;
import haivo.us.crypto.activity.SettingsNotificationsActivity;
import haivo.us.crypto.activity.SettingsSoundsActivity;
import haivo.us.crypto.activity.SettingsTTSActivity;
import haivo.us.crypto.activity.SuggestNewExchangeActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import haivo.us.crypto.dialog.ChangeLogDialog;
import haivo.us.crypto.fragment.generic.TTSAwareFragment;
import haivo.us.crypto.R;
import haivo.us.crypto.preferences.FrequencyPickerDialogPreference;
import haivo.us.crypto.receiver.MarketChecker;
import haivo.us.crypto.util.PreferencesUtils;
import haivo.us.crypto.util.Utils;

public class SettingsMainFragment extends TTSAwareFragment {
    private SettingsMainActivity settingsMainActivity;
    private Preference ttsCategoryPreference;

    /* renamed from: haivo.us.crypto.fragment.SettingsMainFragment.1 */
    class C01791 implements OnPreferenceClickListener {
        C01791() {
        }

        public boolean onPreferenceClick(Preference preference) {
            SettingsMainFragment.this.startActivity(new Intent(SettingsMainFragment.this.getActivity(), SettingsNotificationsActivity.class));
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsMainFragment.2 */
    class C01802 implements OnPreferenceChangeListener {
        C01802() {
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            PreferencesUtils.setCheckGlobalFrequency(SettingsMainFragment.this.getActivity(), ((Long) newValue).longValue());
            MarketChecker.resetAlarmManagerForAllEnabledThatUseGlobalFrequency(SettingsMainFragment.this.getActivity());
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsMainFragment.3 */
    class C01813 implements OnPreferenceClickListener {
        C01813() {
        }

        public boolean onPreferenceClick(Preference preference) {
            SettingsMainFragment.this.startActivity(new Intent(SettingsMainFragment.this.getActivity(), SettingsSoundsActivity.class));
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsMainFragment.4 */
    class C01824 implements OnPreferenceClickListener {
        C01824() {
        }

        public boolean onPreferenceClick(Preference preference) {
            SettingsMainFragment.this.startActivity(new Intent(SettingsMainFragment.this.getActivity(), SettingsTTSActivity.class));
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsMainFragment.5 */
    class C01835 implements OnPreferenceClickListener {
        C01835() {
        }

        public boolean onPreferenceClick(Preference preference) {
            Utils.goToWebPage(SettingsMainFragment.this.getActivity(), "https://github.com/talobin/crypto/issues");
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsMainFragment.6 */
    class C01846 implements OnPreferenceClickListener {
        C01846() {
        }

        public boolean onPreferenceClick(Preference preference) {
            Utils.goToWebPage(SettingsMainFragment.this.getActivity(), SuggestNewExchangeActivity.GITHUB_URL);
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsMainFragment.7 */
    class C01857 implements OnPreferenceClickListener {
        C01857() {
        }

        public boolean onPreferenceClick(Preference preference) {
            if (SettingsMainFragment.this.settingsMainActivity != null) {
                SettingsMainFragment.this.settingsMainActivity.showDonateDialog();
            }
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsMainFragment.8 */
    class C01868 implements OnPreferenceClickListener {
        C01868() {
        }

        public boolean onPreferenceClick(Preference preference) {
            SettingsMainFragment.startShareIntent(SettingsMainFragment.this.getActivity());
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsMainFragment.9 */
    class C01879 implements OnPreferenceClickListener {
        C01879() {
        }

        public boolean onPreferenceClick(Preference preference) {
            Utils.goToGooglePlay(SettingsMainFragment.this.getActivity(), SettingsMainFragment.this.getActivity().getPackageName());
            return true;
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SettingsMainActivity) {
            this.settingsMainActivity = (SettingsMainActivity) activity;
        }
    }

    public void onCreateBeforeInitTTS(Bundle savedInstanceState) {
        super.onCreateBeforeInitTTS(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_main);
        findPreference(getString(R.string.settings_notifications_category_key)).setOnPreferenceClickListener(new C01791());
        ((FrequencyPickerDialogPreference) findPreference(getString(R.string.settings_check_global_frequency_key))).setOnPreferenceChangeListener(new C01802());
        findPreference(getString(R.string.settings_sounds_category_key)).setOnPreferenceClickListener(new C01813());
        this.ttsCategoryPreference = findPreference(getString(R.string.settings_tts_category_key));
        this.ttsCategoryPreference.setOnPreferenceClickListener(new C01824());
        findPreference(getString(R.string.settings_about_report_issue_key)).setOnPreferenceClickListener(new C01835());
        findPreference(getString(R.string.settings_about_github_key)).setOnPreferenceClickListener(new C01846());
        findPreference(getString(R.string.settings_about_donate_key)).setOnPreferenceClickListener(new C01857());
        findPreference(getString(R.string.settings_about_share_key)).setOnPreferenceClickListener(new C01868());
        findPreference(getString(R.string.settings_about_rate_key)).setOnPreferenceClickListener(new C01879());
        //findPreference(getString(R.string.settings_about_changelog_key)).setOnPreferenceClickListener(new OnPreferenceClickListener() {
        //    public boolean onPreferenceClick(Preference preference) {
        //        new ChangeLogDialog(SettingsMainFragment.this.getActivity()).show();
        //        return true;
        //    }
        //});
        Preference aboutVersionPreference = findPreference(getString(R.string.settings_about_version_key));
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            aboutVersionPreference.setTitle(getString(R.string.settings_about_version_title, getString(R.string.app_name), pInfo.versionName));
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        aboutVersionPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Utils.sendEmail(SettingsMainFragment.this.getActivity(), "vodinhhai@gmail.com", "Crypto Kit");
                return true;
            }
        });
    }

    protected void onTTSAvailable(boolean available) {
        super.onTTSAvailable(available);
        if (getActivity() != null) {
        }
    }

    public static void startShareIntent(Context context) {
        Intent shareIntent = new Intent("android.intent.action.SEND");
        shareIntent.putExtra("android.intent.extra.TITLE", context.getString(R.string.app_name));
        shareIntent.putExtra("android.intent.extra.SUBJECT", context.getString(R.string.app_name));
        shareIntent.putExtra("android.intent.extra.TEXT", "https://play.google.com/store/apps/details?id=");
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.settings_about_share_title)));
    }
}
