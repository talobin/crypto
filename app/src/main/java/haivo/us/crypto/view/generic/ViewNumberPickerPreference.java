package haivo.us.crypto.view.generic;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import haivo.us.crypto.R;
import net.simonvt.numberpicker.NumberPicker;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;

public class ViewNumberPickerPreference extends ViewDialogPreference {
    private OnValueChangedListener onValueSelectedListener;
    private int value;
    private String valueSufix;

    /* renamed from: haivo.us.crypto.view.generic.ViewNumberPickerPreference.1 */
    class C02171 implements AlertDialog.OnClickListener {
        final /* synthetic */ NumberPicker val$numberPicker;

        C02171(NumberPicker numberPicker) {
            this.val$numberPicker = numberPicker;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.val$numberPicker.clearFocus();
            ViewNumberPickerPreference.this.setValue(this.val$numberPicker.getValue());
            dialog.dismiss();
        }
    }

    public interface OnValueChangedListener {
        boolean onValueChanged(ViewNumberPickerPreference viewNumberPickerPreference, int i);
    }

    public ViewNumberPickerPreference(Context context) {
        super(context);
        this.value = -1;
    }

    public ViewNumberPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.value = -1;
    }

    @TargetApi(11)
    public ViewNumberPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.value = -1;
    }

    public String getEntry() {
        return String.valueOf(getValue()) + (this.valueSufix != null ? this.valueSufix : BuildConfig.VERSION_NAME);
    }

    public void setValue(int value) {
        if (getValue() != value && (this.onValueSelectedListener == null || this.onValueSelectedListener.onValueChanged(
            this,
            value))) {
            this.value = value;
        }
        setSummary(getEntry());
    }

    public int getValue() {
        return this.value;
    }

    public void setValueSufix(String valueSufix) {
        this.valueSufix = valueSufix;
    }

    protected void onPrepareDialog(Builder builder) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.number_picker_dialog, null);
        NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.pref_num_picker);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);
        numberPicker.setValue(getValue());
        builder.setPositiveButton(R.string.ok, new C02171(numberPicker));
        builder.setView(view);
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueSelectedListener) {
        this.onValueSelectedListener = onValueSelectedListener;
    }
}
