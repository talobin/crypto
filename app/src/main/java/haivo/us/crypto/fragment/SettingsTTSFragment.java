package haivo.us.crypto.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.support.v4.preference.PreferenceFragment;
import haivo.us.crypto.R;
import haivo.us.crypto.model.CurrencySubunit;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import haivo.us.crypto.model.market.Bitstamp;
import haivo.us.crypto.util.CurrencyUtils;
import haivo.us.crypto.util.FormatUtils;
import haivo.us.crypto.util.TTSHelper;
import haivo.us.crypto.util.Utils;

public class SettingsTTSFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener {
    private final String currencyDst;
    private final String currencySrc;
    private Preference formatExamplePreference;
    private final double lastPrice;
    private final Market market;
    private final CurrencySubunit subunitDst;

    /* renamed from: haivo.us.crypto.fragment.SettingsTTSFragment.1 */
    class C01951 implements OnPreferenceClickListener {
        C01951() {
        }

        public boolean onPreferenceClick(Preference preference) {
            try {
                Intent intent = new Intent();
                intent.setAction("com.android.settings.TTS_SETTINGS");
                intent.setFlags(268435456);
                SettingsTTSFragment.this.getActivity().startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsTTSFragment.2 */
    class C01962 implements OnPreferenceClickListener {
        C01962() {
        }

        public boolean onPreferenceClick(Preference preference) {
            TTSHelper.speak(SettingsTTSFragment.this.getActivity(),
                            FormatUtils.formatTextForTTS(SettingsTTSFragment.this.getActivity(),
                                                         712.67d,
                                                         VirtualCurrency.BTC,
                                                         0,
                                                         false,
                                                         SettingsTTSFragment.this.subunitDst,
                                                         false,
                                                         SettingsTTSFragment.this.market,
                                                         SettingsTTSFragment.this.market.ttsName,
                                                         false));
            return true;
        }
    }

    /* renamed from: haivo.us.crypto.fragment.SettingsTTSFragment.3 */
    class C01973 implements OnPreferenceChangeListener {
        final /* synthetic */ ListPreference val$advancedStreamPreference;

        C01973(ListPreference listPreference) {
            this.val$advancedStreamPreference = listPreference;
        }

        public boolean onPreferenceChange(Preference preference, Object newValue) {
            return Utils.handleIntListOnPreferenceChange(this.val$advancedStreamPreference, newValue);
        }
    }

    public SettingsTTSFragment() {
        this.market = new Bitstamp();
        this.currencySrc = VirtualCurrency.BTC;
        this.currencyDst = Currency.USD;
        this.subunitDst = CurrencyUtils.getCurrencySubunit(Currency.USD, 1);
        this.lastPrice = 712.67d;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_tts);
        findPreference(getString(R.string.settings_tts_configure_key)).setOnPreferenceClickListener(new C01951());
        this.formatExamplePreference = findPreference(getString(R.string.settings_tts_format_example_key));
        String currencyString = getString(R.string.generic_currency_pair, VirtualCurrency.BTC, Currency.USD);
        this.formatExamplePreference.setTitle(getString(R.string.settings_tts_format_example_title,
                                                        currencyString,
                                                        this.market.name));
        this.formatExamplePreference.setOnPreferenceClickListener(new C01962());
        ListPreference advancedStreamPreference =
            (ListPreference) findPreference(getString(R.string.settings_tts_advanced_stream_key));
        advancedStreamPreference.setSummary(advancedStreamPreference.getEntry());
        advancedStreamPreference.setOnPreferenceChangeListener(new C01973(advancedStreamPreference));
    }

    public void onStart() {
        super.onStart();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        refreshFormatExamplePreference();
    }

    public void onStop() {
        super.onStop();
        try {
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.settings_tts_format_speak_base_currency_key).equals(key)
            || getString(R.string.settings_tts_format_integer_only_key).equals(key)
            || getString(R.string.settings_tts_format_speak_counter_currency_key).equals(key)
            || getString(R.string.settings_tts_format_speak_exchange_key).equals(key)) {
            refreshFormatExamplePreference();
        }
    }

    private void refreshFormatExamplePreference() {
        this.formatExamplePreference.setSummary(FormatUtils.formatTextForTTS(getActivity(),
                                                                             712.67d,
                                                                             VirtualCurrency.BTC,
                                                                             0,
                                                                             false,
                                                                             this.subunitDst,
                                                                             false,
                                                                             this.market,
                                                                             this.market.name,
                                                                             false));
    }
}
