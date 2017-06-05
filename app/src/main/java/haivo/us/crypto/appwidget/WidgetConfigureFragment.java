package haivo.us.crypto.appwidget;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.support.v4.preference.PreferenceFragment;
import haivo.us.crypto.R;

public class WidgetConfigureFragment extends PreferenceFragment {

    /* renamed from: haivo.us.crypto.appwidget.WidgetConfigureFragment.1 */
    class C01661 implements OnPreferenceChangeListener {
        final /* synthetic */ ListPreference val$alphaPercentPreference;

        C01661(ListPreference listPreference) {
            this.val$alphaPercentPreference = listPreference;
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            try {
                CharSequence[] entries = this.val$alphaPercentPreference.getEntries();
                int index = this.val$alphaPercentPreference.findIndexOfValue((String) newValue);
                if (index < 0 || index >= entries.length) {
                    index = 0;
                }
                WidgetConfigureFragment.this.setAlphaPercentPreferenceSummary(this.val$alphaPercentPreference,
                                                                              this.val$alphaPercentPreference.getEntries()[index]);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public static WidgetConfigureFragment newInstance(int appWidgetId) {
        WidgetConfigureFragment fragment = new WidgetConfigureFragment();
        Bundle args = new Bundle();
        args.putInt("appWidgetId", appWidgetId);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        int appWidgetId = 0;
        if (getArguments() != null) {
            appWidgetId = getArguments().getInt("appWidgetId", 0);
        }
        getPreferenceManager().setSharedPreferencesName(WidgetPrefsUtils.getSharedPreferencesName(appWidgetId));
        addPreferencesFromResource(R.xml.widget_settings);
        ListPreference alphaPercentPreference =
            (ListPreference) findPreference(getString(R.string.widget_settings_alpha_percent_key));
        setAlphaPercentPreferenceSummary(alphaPercentPreference, alphaPercentPreference.getEntry());
        alphaPercentPreference.setOnPreferenceChangeListener(new C01661(alphaPercentPreference));
    }

    private void setAlphaPercentPreferenceSummary(ListPreference alphaPercentPreference, CharSequence newSummary) {
        alphaPercentPreference.setSummary(String.valueOf(newSummary).replace("%", "%%"));
    }
}
