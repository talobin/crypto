package haivo.us.crypto.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import haivo.us.crypto.R;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.content.CheckerRecord;
import haivo.us.crypto.model.CurrencySubunit;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.util.CurrencyUtils;
import haivo.us.crypto.util.FormatUtilsBase;
import haivo.us.crypto.util.TickerUtils;
import haivo.us.crypto.util.Utils;
import haivo.us.crypto.view.generic.ViewDialogPreference;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;

public class ViewAlarmCheckPointPreference extends ViewDialogPreference {
    private AlarmRecord alarmRecord;
    private CheckerRecord checkerRecord;
    private OnCheckpointChangedListener onCheckpointChangedListener;
    private CurrencySubunit subunit;
    private String sufix;

    /* renamed from: haivo.us.crypto.view.ViewAlarmCheckPointPreference.1 */
    class C02061 implements TextWatcher {
        int selectionEnd;
        int selectionStart;
        final /* synthetic */ EditText val$valueView;

        C02061(EditText editText) {
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

    /* renamed from: haivo.us.crypto.view.ViewAlarmCheckPointPreference.2 */
    class C02072 implements Runnable {
        final /* synthetic */ EditText val$valueView;

        C02072(EditText editText) {
            this.val$valueView = editText;
        }

        public void run() {
            Utils.showKeyboard(ViewAlarmCheckPointPreference.this.getContext(), this.val$valueView);
        }
    }

    /* renamed from: haivo.us.crypto.view.ViewAlarmCheckPointPreference.3 */
    class C02083 implements OnClickListener {
        C02083() {
        }

        public void onClick(View v) {
            ViewAlarmCheckPointPreference.this.resetCheckpointToLastCheck();
            AlertDialog dialog = ViewAlarmCheckPointPreference.this.getDialog();
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    /* renamed from: haivo.us.crypto.view.ViewAlarmCheckPointPreference.4 */
    class C02094 implements DialogInterface.OnClickListener {
        final /* synthetic */ EditText val$valueView;

        C02094(EditText editText) {
            this.val$valueView = editText;
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.hideKeyboard(ViewAlarmCheckPointPreference.this.getContext(), this.val$valueView);
            try {
                double newValue = Double.parseDouble(this.val$valueView.getText().toString().replace(',', '.'));
                if (ViewAlarmCheckPointPreference.this.subunit != null) {
                    newValue /= (double) ViewAlarmCheckPointPreference.this.subunit.subunitToUnit;
                }
                ViewAlarmCheckPointPreference.this.setValue(newValue);
            } catch (Exception e) {
                e.printStackTrace();
                ViewAlarmCheckPointPreference.this.setValue(-1.0d);
            }
        }
    }

    /* renamed from: haivo.us.crypto.view.ViewAlarmCheckPointPreference.5 */
    class C02105 implements DialogInterface.OnClickListener {
        C02105() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ViewAlarmCheckPointPreference.this.setValue(-1.0d);
        }
    }

    public interface OnCheckpointChangedListener {
        boolean onCheckpointChanged(ViewAlarmCheckPointPreference viewAlarmCheckPointPreference, Ticker ticker);
    }

    public ViewAlarmCheckPointPreference(Context context) {
        super(context);
    }

    public ViewAlarmCheckPointPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public ViewAlarmCheckPointPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public String getEntry() {
        Ticker lastCheckPointTicker = TickerUtils.fromJson(this.alarmRecord.getLastCheckPointTicker());
        if (lastCheckPointTicker != null) {
            return FormatUtilsBase.formatPriceWithCurrency(lastCheckPointTicker.last, this.subunit)
                + " - "
                + FormatUtilsBase.formatDateTime(getContext(), lastCheckPointTicker.timestamp);
        }
        return getContext().getString(R.string.checker_add_alarm_checkpoint_empty_text);
    }

    public void setCheckerAndAlarmRecord(CheckerRecord checkerRecord, AlarmRecord alarmRecord) {
        this.checkerRecord = checkerRecord;
        this.subunit =
            CurrencyUtils.getCurrencySubunit(checkerRecord.getCurrencyDst(), checkerRecord.getCurrencySubunitDst());
        this.alarmRecord = alarmRecord;
        setSummary(getEntry());
    }

    public void setSufix(String sufix) {
        this.sufix = sufix;
    }

    private void setValue(double d) {
        boolean bl;
        Ticker ticker = TickerUtils.fromJson(this.alarmRecord.getLastCheckPointTicker());
        if (d <= 0.0) {
            bl = ticker != null;
            ticker = null;
        } else if (ticker != null) {
            bl = ticker.last != d;
        } else {
            ticker = new Ticker();
            bl = true;
        }
        if (bl) {
            if (ticker != null) {
                ticker.last = d;
                ticker.timestamp = System.currentTimeMillis();
            }
            if (this.onCheckpointChangedListener == null || this.onCheckpointChangedListener.onCheckpointChanged(this,
                                                                                                                 ticker)) {
                // empty if block
            }
        }
        this.setSummary(this.getEntry());
    }

    private void resetCheckpointToLastCheck() {
        if (TextUtils.isEmpty((CharSequence) this.alarmRecord.getLastCheckPointTicker())
            && !TextUtils.isEmpty((CharSequence) this.checkerRecord.getLastCheckTicker())
            || !TextUtils.isEmpty((CharSequence) this.alarmRecord.getLastCheckPointTicker())
            && !this.alarmRecord.getLastCheckPointTicker().equals(this.checkerRecord.getLastCheckTicker())) {
            Ticker ticker = TickerUtils.fromJson(this.checkerRecord.getLastCheckTicker());
            if (this.onCheckpointChangedListener == null || this.onCheckpointChangedListener.onCheckpointChanged(this,
                                                                                                                 ticker)) {
                // empty if block
            }
        }
        this.setSummary(this.getEntry());
    }

    protected void onPrepareDialog(Builder builder) {
        int i = 0;
        View view = LayoutInflater.from(getContext()).inflate(R.layout.alarm_checkpoint_dialog, null);
        EditText valueView = (EditText) view.findViewById(R.id.valueView);
        valueView.addTextChangedListener(new C02061(valueView));
        TextView sufixView = (TextView) view.findViewById(R.id.sufixView);
        TextView resetCheckpointButton = (TextView) view.findViewById(R.id.resetCheckpointButton);
        Ticker lastCheckPointTicker = TickerUtils.fromJson(this.alarmRecord.getLastCheckPointTicker());
        if (lastCheckPointTicker != null) {
            valueView.setText(FormatUtilsBase.formatPriceValueForSubunit(lastCheckPointTicker.last,
                                                                         this.subunit,
                                                                         false,
                                                                         true).replace(',', '.'));
        } else {
            valueView.setText(BuildConfig.VERSION_NAME);
        }
        if (TextUtils.isEmpty(this.checkerRecord.getLastCheckTicker())) {
            i = 8;
        }
        resetCheckpointButton.setVisibility(i);
        Utils.setSelectionAfterLastLetter(valueView);
        sufixView.setText(this.sufix);
        valueView.post(new C02072(valueView));
        resetCheckpointButton.setOnClickListener(new C02083());
        builder.setPositiveButton(R.string.ok, new C02094(valueView));
        builder.setNeutralButton(R.string.cancel, null);
        builder.setNegativeButton(R.string.generic_remove, new C02105());
        builder.setView(view);
    }

    public void setOnCheckpointChangedListener(OnCheckpointChangedListener onCheckpointChangedListener) {
        this.onCheckpointChangedListener = onCheckpointChangedListener;
    }
}
