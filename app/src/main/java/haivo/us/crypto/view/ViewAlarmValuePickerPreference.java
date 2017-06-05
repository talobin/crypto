package haivo.us.crypto.view;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.R;
import haivo.us.crypto.model.CurrencySubunit;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;
import haivo.us.crypto.util.AlarmRecordHelper;
import haivo.us.crypto.util.CurrencyUtils;
import haivo.us.crypto.util.Utils;
import haivo.us.crypto.view.generic.ViewDialogPreference;

public class ViewAlarmValuePickerPreference extends ViewDialogPreference {
    private AlarmRecord alarmRecord;
    private OnValueChangedListener onValueSelectedListener;
    private String prefix;
    private CurrencySubunit subunit;
    private String sufix;
    private double value;

    /* renamed from: haivo.us.crypto.view.ViewAlarmValuePickerPreference.1 */
    class C02111 implements TextWatcher {
        int selectionEnd;
        int selectionStart;
        final /* synthetic */ EditText val$valueView;

        C02111(EditText editText) {
            this.val$valueView = editText;
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            this.selectionStart = this.val$valueView.getSelectionStart();
            this.selectionEnd = this.val$valueView.getSelectionEnd();
            this.val$valueView.removeTextChangedListener(this);
            if (s.toString().contains(",")) {
                this.val$valueView.setText(s.toString().replace(',', '.'));
            }
            this.val$valueView.addTextChangedListener(this);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void afterTextChanged(Editable s) {
            this.val$valueView.setSelection(this.selectionStart, this.selectionEnd);
        }
    }

    /* renamed from: haivo.us.crypto.view.ViewAlarmValuePickerPreference.2 */
    class C02122 implements Runnable {
        final /* synthetic */ EditText val$valueView;

        C02122(EditText editText) {
            this.val$valueView = editText;
        }

        public void run() {
            Utils.showKeyboard(ViewAlarmValuePickerPreference.this.getContext(), this.val$valueView);
        }
    }

    /* renamed from: haivo.us.crypto.view.ViewAlarmValuePickerPreference.3 */
    class C02133 implements DialogInterface.OnClickListener {
        final /* synthetic */ EditText val$valueView;

        C02133(EditText editText) {
            this.val$valueView = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.hideKeyboard(ViewAlarmValuePickerPreference.this.getContext(), this.val$valueView);
            double newValue = 1.0d;
            try {
                newValue = AlarmRecordHelper.parseEnteredValueForAlarmType(ViewAlarmValuePickerPreference.this.subunit,
                                                                           ViewAlarmValuePickerPreference.this.alarmRecord,
                                                                           Double.parseDouble(this.val$valueView.getText()
                                                                                                                .toString()
                                                                                                                .replace(
                                                                                                                    ',',
                                                                                                                    '.')));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ViewAlarmValuePickerPreference.this.setValue(newValue);
            dialog.dismiss();
        }
    }

    public interface OnValueChangedListener {
        boolean onValueChanged(ViewAlarmValuePickerPreference viewAlarmValuePickerPreference, double d);
    }

    public ViewAlarmValuePickerPreference(Context context) {
        super(context);
        this.value = -1.0d;
    }

    public ViewAlarmValuePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.value = -1.0d;
    }

    @TargetApi(11)
    public ViewAlarmValuePickerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.value = -1.0d;
    }

    public String getEntry() {
        String prefixString = this.prefix != null ? this.prefix : BuildConfig.VERSION_NAME;
        String sufixString = this.sufix != null ? this.sufix : BuildConfig.VERSION_NAME;
        return getContext().getString(R.string.checker_add_alarm_type_value_format, new Object[] {
            prefixString, AlarmRecordHelper.getValueForAlarmType(this.subunit, this.alarmRecord), sufixString
        });
    }

    public void setCheckerAndAlarmRecord(CheckerRecord checkerRecord, AlarmRecord alarmRecord) {
        this.subunit =
            CurrencyUtils.getCurrencySubunit(checkerRecord.getCurrencyDst(), checkerRecord.getCurrencySubunitDst());
        this.alarmRecord = alarmRecord;
    }

    public void setValue(double value) {
        if (getValue() != value && (this.onValueSelectedListener == null || this.onValueSelectedListener.onValueChanged(
            this,
            value))) {
            this.value = value;
        }
        setSummary(getEntry());
    }

    public double getValue() {
        return this.value;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setSufix(String sufix) {
        this.sufix = sufix;
    }

    protected void onPrepareDialog(Builder builder) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.alarm_value_picker_dialog, null);
        TextView prefixView = (TextView) view.findViewById(R.id.prefixView);
        EditText valueView = (EditText) view.findViewById(R.id.valueView);
        valueView.addTextChangedListener(new C02111(valueView));
        TextView sufixView = (TextView) view.findViewById(R.id.sufixView);
        prefixView.setText(this.prefix);
        valueView.setText(AlarmRecordHelper.getValueForAlarmType(this.subunit, this.alarmRecord).replace(',', '.'));
        Utils.setSelectionAfterLastLetter(valueView);
        sufixView.setText(this.sufix);
        valueView.post(new C02122(valueView));
        builder.setPositiveButton(R.string.ok, new C02133(valueView));
        builder.setView(view);
    }

    public void setOnValueChangedListener(OnValueChangedListener onValueSelectedListener) {
        this.onValueSelectedListener = onValueSelectedListener;
    }
}
