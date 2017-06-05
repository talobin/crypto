package haivo.us.crypto.preferences;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import haivo.us.crypto.R;
import haivo.us.crypto.util.TimeUtils;
import net.simonvt.numberpicker.NumberPicker;

public class FrequencyPickerDialogPreference extends DialogPreference {
    public static final long DEFAULT_FREQUENCY_MILLIS = 600000;
    public static final long[] FREQUENCY_TYPE_MULTIPLAYERS;
    public static final int[] FREQUENCY_TYPE_NAMES;
    public static final int[] FREQUENCY_TYPE_PLURALS;
    public static final int[] FREQUENCY_VALUE_MAX;
    public static final int[] FREQUENCY_VALUE_MIN;
    public static final long MIN_FREQUENCY_MILLIS;
    private long baseFrequencyMillis;
    private String[] frequencyTypeEntries;
    private NumberPicker frequencyTypePicker;
    private NumberPicker frequencyValuePicker;
    private long viewFrequencyMillis;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        long baseFrequencyMillis;
        long viewFrequencyMillis;

        /* renamed from: haivo.us.crypto.preferences.FrequencyPickerDialogPreference.SavedState.1 */
        static class C01981 implements Creator<SavedState> {
            C01981() {
            }

            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        }

        public SavedState(Parcelable superState) {
            super(superState);
            this.viewFrequencyMillis = -1;
        }

        public SavedState(Parcel source) {
            super(source);
            this.baseFrequencyMillis = source.readLong();
            this.viewFrequencyMillis = source.readLong();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeLong(this.baseFrequencyMillis);
            dest.writeLong(this.viewFrequencyMillis);
        }

        static {
            CREATOR = new C01981();
        }
    }

    /* renamed from: haivo.us.crypto.preferences.FrequencyPickerDialogPreference.1 */
    class C03411 implements NumberPicker.OnValueChangeListener {
        C03411() {
        }

        public void onValueChange(NumberPicker np, int oldVal, int newVal) {
            FrequencyPickerDialogPreference.this.frequencyValuePicker.setMinValue(FrequencyPickerDialogPreference.FREQUENCY_VALUE_MIN[newVal]);
            FrequencyPickerDialogPreference.this.frequencyValuePicker.setMaxValue(FrequencyPickerDialogPreference.FREQUENCY_VALUE_MAX[newVal]);
        }
    }

    /* renamed from: haivo.us.crypto.preferences.FrequencyPickerDialogPreference.2 */
    class C03422 implements NumberPicker.OnValueChangeListener {
        C03422() {
        }

        public void onValueChange(NumberPicker np, int oldVal, int newVal) {
        }
    }

    static {
        FREQUENCY_TYPE_NAMES =
            new int[] { R.string.time_seconds, R.string.time_minutes, R.string.time_hours, R.string.time_days };
        FREQUENCY_TYPE_PLURALS =
            new int[] { R.plurals.time_seconds, R.plurals.time_minutes, R.plurals.time_hours, R.plurals.time_days };
        FREQUENCY_TYPE_MULTIPLAYERS =
            new long[] { 1000, TimeUtils.MILLIS_IN_MINUTE, TimeUtils.MILLIS_IN_HOUR, TimeUtils.MILLIS_IN_DAY };
        FREQUENCY_VALUE_MIN = new int[] { 5, 1, 1, 1 };
        FREQUENCY_VALUE_MAX = new int[] { 59, 59, 23, 365 };
        MIN_FREQUENCY_MILLIS = ((long) FREQUENCY_VALUE_MIN[0]) * FREQUENCY_TYPE_MULTIPLAYERS[0];
    }

    public FrequencyPickerDialogPreference(Context context) {
        this(context, null);
    }

    public FrequencyPickerDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.frequencyTypeEntries = new String[4];
        for (int i = 0; i < FREQUENCY_TYPE_NAMES.length; i++) {
            this.frequencyTypeEntries[i] = context.getString(FREQUENCY_TYPE_NAMES[i]);
        }
        setDialogLayoutResource(R.layout.frequency_picker_dialog);
        setPositiveButtonText(R.string.ok);
        setNegativeButtonText(R.string.cancel);
        setDialogIcon(null);
        setDefaultValue(Long.valueOf(DEFAULT_FREQUENCY_MILLIS));
    }

    protected void onSetInitialValue(boolean restore, Object defaultValue) {
        setFrequencyMillis(restore ? getPersistedLong(DEFAULT_FREQUENCY_MILLIS) : ((Long) defaultValue).longValue());
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return Long.valueOf(DEFAULT_FREQUENCY_MILLIS);
    }

    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setInverseBackgroundForced(true);
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.frequencyTypePicker = (NumberPicker) view.findViewById(R.id.frequencyTypePicker);
        this.frequencyValuePicker = (NumberPicker) view.findViewById(R.id.frequencyValuePicker);
        view.findViewById(R.id.useGlobalFrequencyCheckBox).setVisibility(View.GONE);
        this.frequencyTypePicker.setMinValue(0);
        this.frequencyTypePicker.setMaxValue(this.frequencyTypeEntries.length - 1);
        this.frequencyTypePicker.setDisplayedValues(this.frequencyTypeEntries);
        this.frequencyTypePicker.setOnValueChangedListener(new C03411());
        this.frequencyValuePicker.setOnValueChangedListener(new C03422());
        if (this.viewFrequencyMillis < 0) {
            this.viewFrequencyMillis = this.baseFrequencyMillis;
        }
        int frequencyTypeId = parseFrequencyTypeId(this.viewFrequencyMillis);
        this.frequencyTypePicker.setValue(frequencyTypeId);
        this.frequencyValuePicker.setMinValue(FREQUENCY_VALUE_MIN[this.frequencyTypePicker.getValue()]);
        this.frequencyValuePicker.setMaxValue(FREQUENCY_VALUE_MAX[this.frequencyTypePicker.getValue()]);
        this.frequencyValuePicker.setValue(parseFrequencyValue(this.viewFrequencyMillis, frequencyTypeId));
    }

    private long getFrequencyMillis(int frequencyTypeId, int frequencyValue) {
        return FREQUENCY_TYPE_MULTIPLAYERS[frequencyTypeId] * ((long) frequencyValue);
    }

    private int parseFrequencyTypeId(long frequencyMillis) {
        for (int i = FREQUENCY_TYPE_MULTIPLAYERS.length - 1; i >= 0; i--) {
            if (frequencyMillis >= FREQUENCY_TYPE_MULTIPLAYERS[i] * ((long) FREQUENCY_VALUE_MIN[1])) {
                return i;
            }
        }
        return 0;
    }

    private int parseFrequencyValue(long frequencyMillis, int frequencyTypeId) {
        return (int) (frequencyMillis / FREQUENCY_TYPE_MULTIPLAYERS[frequencyTypeId]);
    }

    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            long frequencyMillis =
                getFrequencyMillis(this.frequencyTypePicker.getValue(), this.frequencyValuePicker.getValue());
            if (callChangeListener(Long.valueOf(frequencyMillis))) {
                setFrequencyMillis(frequencyMillis);
                return;
            }
            return;
        }
        this.viewFrequencyMillis = -1;
    }

    public void setFrequencyMillis(long frequencyMillis) {
        if (frequencyMillis < MIN_FREQUENCY_MILLIS) {
            frequencyMillis = MIN_FREQUENCY_MILLIS;
        }
        this.viewFrequencyMillis = frequencyMillis;
        int frequencyTypeId = parseFrequencyTypeId(frequencyMillis);
        int frequencyValue = parseFrequencyValue(frequencyMillis, frequencyTypeId);
        setSummary(getContext().getResources()
                               .getQuantityString(FREQUENCY_TYPE_PLURALS[frequencyTypeId],
                                                  frequencyValue,
                                                  new Object[] {
                                                      Integer.valueOf(frequencyValue)
                                                  }));
        if (frequencyMillis != this.baseFrequencyMillis) {
            this.baseFrequencyMillis = frequencyMillis;
            persistLong(frequencyMillis);
            notifyChanged();
        }
    }

    private long getFrequencyMillis() {
        return this.baseFrequencyMillis;
    }

    protected Parcelable onSaveInstanceState() {
        SavedState myState = new SavedState(super.onSaveInstanceState());
        myState.baseFrequencyMillis = getFrequencyMillis();
        if (!(this.frequencyTypePicker == null || this.frequencyValuePicker == null)) {
            myState.viewFrequencyMillis =
                getFrequencyMillis(this.frequencyTypePicker.getValue(), this.frequencyValuePicker.getValue());
        }
        return myState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        setFrequencyMillis(myState.baseFrequencyMillis);
        this.viewFrequencyMillis = myState.viewFrequencyMillis;
        super.onRestoreInstanceState(myState.getSuperState());
    }
}
