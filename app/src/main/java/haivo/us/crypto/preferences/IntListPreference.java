package haivo.us.crypto.preferences;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class IntListPreference extends ListPreference {
    public IntListPreference(Context context) {
        super(context);
        init();
    }

    public IntListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setDefaultEntryValues();
    }

    public void setDefaultEntryValues() {
        if (getEntryValues() == null && getEntries() != null) {
            int count = getEntries().length;
            CharSequence[] entryValues = new String[count];
            for (int i = 0; i < count; i++) {
                entryValues[i] = String.valueOf(i);
            }
            setEntryValues(entryValues);
        }
    }

    protected boolean persistString(String value) {
        if (value == null) {
            return false;
        }
        return persistInt(Integer.valueOf(value).intValue());
    }

    protected String getPersistedString(String defaultReturnValue) {
        return !getSharedPreferences().contains(getKey()) ? defaultReturnValue : String.valueOf(getPersistedInt(0));
    }
}
