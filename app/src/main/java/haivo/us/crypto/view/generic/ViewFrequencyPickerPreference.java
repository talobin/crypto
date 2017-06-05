package haivo.us.crypto.view.generic;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import haivo.us.crypto.R;
import haivo.us.crypto.preferences.FrequencyPickerDialogPreference;
import haivo.us.crypto.util.PreferencesUtils;
import net.simonvt.numberpicker.NumberPicker;

public class ViewFrequencyPickerPreference extends ViewDialogPreference {
    public static final long USE_GLOBAL_CHECK_FREQUENCY = -1;
    private long frequency;
    private String[] frequencyTypeEntries;
    private OnFrequencySelectedListener onFrequencySelectedListener;

    /* renamed from: haivo.us.crypto.view.generic.ViewFrequencyPickerPreference.3 */
    class C02153 implements OnCheckedChangeListener {
        final /* synthetic */ NumberPicker val$frequencyTypePicker;
        final /* synthetic */ NumberPicker val$frequencyValuePicker;

        C02153(NumberPicker numberPicker, NumberPicker numberPicker2) {
            this.val$frequencyTypePicker = numberPicker;
            this.val$frequencyValuePicker = numberPicker2;
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            boolean z;
            boolean z2 = true;
            ViewFrequencyPickerPreference.this.refreshFrequencyPickers(this.val$frequencyTypePicker,
                                                                       this.val$frequencyValuePicker,
                                                                       ViewFrequencyPickerPreference.USE_GLOBAL_CHECK_FREQUENCY);
            NumberPicker numberPicker = this.val$frequencyTypePicker;
            if (isChecked) {
                z = false;
            } else {
                z = true;
            }
            numberPicker.setEnabled(z);
            NumberPicker numberPicker2 = this.val$frequencyValuePicker;
            if (isChecked) {
                z2 = false;
            }
            numberPicker2.setEnabled(z2);
            this.val$frequencyTypePicker.clearFocus();
            this.val$frequencyValuePicker.clearFocus();
        }
    }

    /* renamed from: haivo.us.crypto.view.generic.ViewFrequencyPickerPreference.4 */
    class C02164 implements AlertDialog.OnClickListener {
        final /* synthetic */ NumberPicker val$frequencyTypePicker;
        final /* synthetic */ NumberPicker val$frequencyValuePicker;
        final /* synthetic */ CompoundButton val$useGlobalFrequencyCheckBox;

        C02164(NumberPicker numberPicker, NumberPicker numberPicker2, CompoundButton compoundButton) {
            this.val$frequencyTypePicker = numberPicker;
            this.val$frequencyValuePicker = numberPicker2;
            this.val$useGlobalFrequencyCheckBox = compoundButton;
        }

        public void onClick(DialogInterface dialog, int which) {
            this.val$frequencyTypePicker.clearFocus();
            this.val$frequencyValuePicker.clearFocus();
            ViewFrequencyPickerPreference.this.setFrequency(this.val$useGlobalFrequencyCheckBox.isChecked()
                                                            ? ViewFrequencyPickerPreference.USE_GLOBAL_CHECK_FREQUENCY
                                                            : ViewFrequencyPickerPreference.this.getFrequencyMillis(this.val$frequencyTypePicker
                                                                                                                        .getValue(),
                                                                                                                    this.val$frequencyValuePicker
                                                                                                                        .getValue()));
            dialog.dismiss();
        }
    }

    public interface OnFrequencySelectedListener {
        boolean onFrequencySelected(ViewFrequencyPickerPreference viewFrequencyPickerPreference, long j);
    }

    /* renamed from: haivo.us.crypto.view.generic.ViewFrequencyPickerPreference.1 */
    class C03451 implements NumberPicker.OnValueChangeListener {
        final /* synthetic */ NumberPicker val$frequencyValuePicker;

        C03451(NumberPicker numberPicker) {
            this.val$frequencyValuePicker = numberPicker;
        }

        public void onValueChange(NumberPicker np, int oldVal, int newVal) {
            this.val$frequencyValuePicker.setMinValue(FrequencyPickerDialogPreference.FREQUENCY_VALUE_MIN[newVal]);
            this.val$frequencyValuePicker.setMaxValue(FrequencyPickerDialogPreference.FREQUENCY_VALUE_MAX[newVal]);
        }
    }

    /* renamed from: haivo.us.crypto.view.generic.ViewFrequencyPickerPreference.2 */
    class C03462 implements NumberPicker.OnValueChangeListener {
        C03462() {
        }

        public void onValueChange(NumberPicker np, int oldVal, int newVal) {
        }
    }

    public ViewFrequencyPickerPreference(Context context) {
        super(context);
    }

    public ViewFrequencyPickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public ViewFrequencyPickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        setFrequency(FrequencyPickerDialogPreference.DEFAULT_FREQUENCY_MILLIS);
    }

    protected void init(Context context, AttributeSet attrs) {
        this.frequencyTypeEntries = new String[4];
        for (int i = 0; i < FrequencyPickerDialogPreference.FREQUENCY_TYPE_NAMES.length; i++) {
            this.frequencyTypeEntries[i] =
                getContext().getString(FrequencyPickerDialogPreference.FREQUENCY_TYPE_NAMES[i]);
        }
        super.init(context, attrs);
    }

    public void setFrequency(long frequency) {
        if (frequency >= 0 && frequency < FrequencyPickerDialogPreference.MIN_FREQUENCY_MILLIS) {
            frequency = FrequencyPickerDialogPreference.MIN_FREQUENCY_MILLIS;
        }
        if (this.frequency != frequency && (this.onFrequencySelectedListener == null || this.onFrequencySelectedListener
            .onFrequencySelected(this, frequency))) {
            this.frequency = frequency;
        }
        setSummary(getEntry());
    }

    public long getFrequency() {
        return this.frequency;
    }

    public CharSequence getEntry() {
        long frequencyToBeDisplayed = this.frequency;
        if (this.frequency <= USE_GLOBAL_CHECK_FREQUENCY) {
            frequencyToBeDisplayed = PreferencesUtils.getCheckGlobalFrequency(getContext());
        }
        int frequencyTypeId = parseFrequencyTypeId(frequencyToBeDisplayed);
        int frequencyValue = parseFrequencyValue(frequencyToBeDisplayed, frequencyTypeId);
        String frequencyString = getContext().getResources()
                                             .getQuantityString(FrequencyPickerDialogPreference.FREQUENCY_TYPE_PLURALS[frequencyTypeId],
                                                                frequencyValue,
                                                                new Object[] {
                                                                    Integer.valueOf(frequencyValue)
                                                                });
        if (this.frequency > USE_GLOBAL_CHECK_FREQUENCY) {
            return frequencyString;
        }
        return getResources().getString(R.string.checker_add_check_frequency_use_global_summary,
                                        new Object[] { frequencyString });
    }

    protected void onPrepareDialog(Builder builder) {
        boolean z = false;
        View dialogLayout = LayoutInflater.from(getContext()).inflate(R.layout.frequency_picker_dialog, null);
        NumberPicker frequencyTypePicker = (NumberPicker) dialogLayout.findViewById(R.id.frequencyTypePicker);
        NumberPicker frequencyValuePicker = (NumberPicker) dialogLayout.findViewById(R.id.frequencyValuePicker);
        CompoundButton useGlobalFrequencyCheckBox =
            (CompoundButton) dialogLayout.findViewById(R.id.useGlobalFrequencyCheckBox);
        frequencyTypePicker.setMinValue(0);
        frequencyTypePicker.setMaxValue(this.frequencyTypeEntries.length - 1);
        frequencyTypePicker.setDisplayedValues(this.frequencyTypeEntries);
        frequencyTypePicker.setOnValueChangedListener(new C03451(frequencyValuePicker));
        frequencyValuePicker.setOnValueChangedListener(new C03462());
        useGlobalFrequencyCheckBox.setOnCheckedChangeListener(new C02153(frequencyTypePicker, frequencyValuePicker));
        if (this.frequency <= USE_GLOBAL_CHECK_FREQUENCY) {
            z = true;
        }
        useGlobalFrequencyCheckBox.setChecked(z);
        refreshFrequencyPickers(frequencyTypePicker, frequencyValuePicker, this.frequency);
        builder.setPositiveButton(R.string.ok,
                                  new C02164(frequencyTypePicker, frequencyValuePicker, useGlobalFrequencyCheckBox));
        builder.setView(dialogLayout);
    }

    private void refreshFrequencyPickers(NumberPicker frequencyTypePicker,
                                         NumberPicker frequencyValuePicker,
                                         long frequency) {
        long frequencyToBeDisplayed = frequency;
        if (frequency <= USE_GLOBAL_CHECK_FREQUENCY) {
            frequencyToBeDisplayed = PreferencesUtils.getCheckGlobalFrequency(getContext());
        }
        int frequencyTypeId = parseFrequencyTypeId(frequencyToBeDisplayed);
        frequencyTypePicker.setValue(frequencyTypeId);
        frequencyValuePicker.setMinValue(FrequencyPickerDialogPreference.FREQUENCY_VALUE_MIN[frequencyTypePicker.getValue()]);
        frequencyValuePicker.setMaxValue(FrequencyPickerDialogPreference.FREQUENCY_VALUE_MAX[frequencyTypePicker.getValue()]);
        frequencyValuePicker.setValue(parseFrequencyValue(frequencyToBeDisplayed, frequencyTypeId));
    }

    private long getFrequencyMillis(int frequencyTypeId, int frequencyValue) {
        return FrequencyPickerDialogPreference.FREQUENCY_TYPE_MULTIPLAYERS[frequencyTypeId] * ((long) frequencyValue);
    }

    private int parseFrequencyTypeId(long frequencyMillis) {
        for (int i = FrequencyPickerDialogPreference.FREQUENCY_TYPE_MULTIPLAYERS.length - 1; i >= 0; i--) {
            if (frequencyMillis
                >= FrequencyPickerDialogPreference.FREQUENCY_TYPE_MULTIPLAYERS[i]
                * ((long) FrequencyPickerDialogPreference.FREQUENCY_VALUE_MIN[1])) {
                return i;
            }
        }
        return 0;
    }

    private int parseFrequencyValue(long frequencyMillis, int frequencyTypeId) {
        return (int) (frequencyMillis / FrequencyPickerDialogPreference.FREQUENCY_TYPE_MULTIPLAYERS[frequencyTypeId]);
    }

    public void setOnFrequencySelectedListener(OnFrequencySelectedListener onFrequencySelectedListener) {
        this.onFrequencySelectedListener = onFrequencySelectedListener;
    }
}
