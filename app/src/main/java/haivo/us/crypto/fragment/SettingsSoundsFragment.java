package haivo.us.crypto.fragment;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.RingtonePreference;
import android.support.v4.preference.PreferenceFragment;
import haivo.us.crypto.R;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;
import haivo.us.crypto.util.PreferencesUtils;
import haivo.us.crypto.util.SoundFilesManager;
import haivo.us.crypto.util.Utils;

public class SettingsSoundsFragment extends PreferenceFragment {

    /* renamed from: haivo.us.crypto.fragment.SettingsSoundsFragment.1 */
    class C01901 extends RingtonePreference {
        C01901(Context x0) {
            super(x0);
        }

        protected Uri onRestoreRingtone() {
            Uri uri = super.onRestoreRingtone();
            if (uri == null) {
                return SoundFilesManager.getUriForRingtone(SettingsSoundsFragment.this.getActivity(),
                                                           SoundFilesManager.BITCOIN_CHECKER_UP_CHEERS_FILENAME);
            }
            return uri;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsSoundsFragment.2 */
    class C01912 implements OnPreferenceChangeListener {
        final /* synthetic */ RingtonePreference val$alarmNotificationUpPreference;

        C01912(RingtonePreference ringtonePreference) {
            this.val$alarmNotificationUpPreference = ringtonePreference;
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            this.val$alarmNotificationUpPreference.setSummary(SettingsSoundsFragment.this.getRingtoneName((String) newValue));
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsSoundsFragment.3 */
    class C01923 extends RingtonePreference {
        C01923(Context x0) {
            super(x0);
        }

        protected Uri onRestoreRingtone() {
            Uri uri = super.onRestoreRingtone();
            if (uri == null) {
                return SoundFilesManager.getUriForRingtone(SettingsSoundsFragment.this.getActivity(),
                                                           SoundFilesManager.BITCOIN_CHECKER_DOWN_ALERT_FILENAME);
            }
            return uri;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsSoundsFragment.4 */
    class C01934 implements OnPreferenceChangeListener {
        final /* synthetic */ RingtonePreference val$alarmNotificationDownPreference;

        C01934(RingtonePreference ringtonePreference) {
            this.val$alarmNotificationDownPreference = ringtonePreference;
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            this.val$alarmNotificationDownPreference.setSummary(SettingsSoundsFragment.this.getRingtoneName((String) newValue));
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsSoundsFragment.5 */
    class C01945 implements OnPreferenceChangeListener {
        final /* synthetic */ ListPreference val$advancedAlarmStreamPreference;

        C01945(ListPreference listPreference) {
            this.val$advancedAlarmStreamPreference = listPreference;
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return Utils.handleIntListOnPreferenceChange(this.val$advancedAlarmStreamPreference, newValue);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_sounds);
        RingtonePreference alarmNotificationUpPreference = new C01901(getActivity());
        alarmNotificationUpPreference.setKey(getString(R.string.settings_sounds_alarm_notification_up_key));
        alarmNotificationUpPreference.setTitle(R.string.settings_sounds_alarm_notification_up_title);
        alarmNotificationUpPreference.setRingtoneType(2);
        alarmNotificationUpPreference.setSummary(getRingtoneName(PreferencesUtils.getSoundAlarmNotificationUp(
            getActivity())));
        alarmNotificationUpPreference.setOnPreferenceChangeListener(new C01912(alarmNotificationUpPreference));
        RingtonePreference alarmNotificationDownPreference = new C01923(getActivity());
        alarmNotificationDownPreference.setKey(getString(R.string.settings_sounds_alarm_notification_down_key));
        alarmNotificationDownPreference.setTitle(R.string.settings_sounds_alarm_notification_down_title);
        alarmNotificationDownPreference.setRingtoneType(2);
        alarmNotificationDownPreference.setSummary(getRingtoneName(PreferencesUtils.getSoundAlarmNotificationDown(
            getActivity())));
        alarmNotificationDownPreference.setOnPreferenceChangeListener(new C01934(alarmNotificationDownPreference));
        PreferenceCategory alarmNotificationCategory =
            (PreferenceCategory) findPreference(getString(R.string.settings_sounds_alarm_notification_category_key));
        alarmNotificationCategory.addPreference(alarmNotificationUpPreference);
        alarmNotificationCategory.addPreference(alarmNotificationDownPreference);
        ListPreference advancedAlarmStreamPreference =
            (ListPreference) findPreference(getString(R.string.settings_sounds_advanced_alarm_stream_key));
        advancedAlarmStreamPreference.setSummary(advancedAlarmStreamPreference.getEntry());
        advancedAlarmStreamPreference.setOnPreferenceChangeListener(new C01945(advancedAlarmStreamPreference));
    }

    private String getRingtoneName(String ringtoneUriString) {
        if (ringtoneUriString != null) {
            Uri ringtoneUri = Uri.parse(ringtoneUriString);
            if (ringtoneUri != null) {
                Ringtone ringtone = RingtoneManager.getRingtone(getActivity(), ringtoneUri);
                if (ringtone != null) {
                    return ringtone.getTitle(getActivity());
                }
            }
        }
        return BuildConfig.VERSION_NAME;
    }
}
