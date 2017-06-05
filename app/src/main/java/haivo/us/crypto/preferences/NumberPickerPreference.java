package haivo.us.crypto.preferences;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import haivo.us.crypto.R;

public class NumberPickerPreference extends DialogPreference {
    int initialValue;
    NumberPicker picker;
    int viewValue;

    private static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR;
        int initialValue;
        int viewValue;

        /* renamed from: haivo.us.crypto.preferences.NumberPickerPreference.SavedState.1 */
        static class C01991 implements Creator<SavedState> {
            C01991() {
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
        }

        public SavedState(Parcel source) {
            super(source);
            this.initialValue = source.readInt();
            this.viewValue = source.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.initialValue);
            dest.writeInt(this.viewValue);
        }

        static {
            CREATOR = new C01991();
        }
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.number_picker_dialog);
        setPositiveButtonText(R.string.ok);
        setNegativeButtonText(R.string.cancel);
    }

    protected void onSetInitialValue(boolean restore, Object defaultValue) {
        setValue(restore ? getPersistedInt(1) : ((Integer) defaultValue).intValue());
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return Integer.valueOf(a.getInt(index, 1));
    }

    protected void onPrepareDialogBuilder(Builder builder) {
        super.onPrepareDialogBuilder(builder);
        builder.setInverseBackgroundForced(true);
    }

    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        this.picker = (NumberPicker) view.findViewById(R.id.pref_num_picker);
        if (this.viewValue == 0) {
            this.viewValue = this.initialValue;
        }
        this.picker.setMinValue(1);
        this.picker.setMaxValue(100);
        this.picker.setValue(this.viewValue);
    }

    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            int value = this.picker.getValue();
            if (callChangeListener(Integer.valueOf(value))) {
                setValue(value);
                return;
            }
            return;
        }
        this.viewValue = 0;
    }

    public void setValue(int value) {
        this.viewValue = value;
        setSummary(value + "%");
        if (value != this.initialValue) {
            this.initialValue = value;
            persistInt(value);
            notifyChanged();
        }
    }

    protected Parcelable onSaveInstanceState() {
        SavedState myState = new SavedState(super.onSaveInstanceState());
        myState.initialValue = this.initialValue;
        if (this.picker != null) {
            myState.viewValue = this.picker.getValue();
        }
        return myState;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        if (state == null || !state.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState myState = (SavedState) state;
        setValue(myState.initialValue);
        this.viewValue = myState.viewValue;
        super.onRestoreInstanceState(myState.getSuperState());
    }
}
