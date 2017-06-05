package haivo.us.crypto.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import haivo.us.crypto.R;
import haivo.us.crypto.view.generic.ViewSpinnerPreference;

public class ViewAlarmTypeSpinnerPreference extends ViewSpinnerPreference {
    private CharSequence[] alarmTypeHints;

    public ViewAlarmTypeSpinnerPreference(Context context) {
        super(context);
    }

    public ViewAlarmTypeSpinnerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public ViewAlarmTypeSpinnerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init(Context context, AttributeSet attrs) {
        this.alarmTypeHints = getResources().getStringArray(R.array.checker_add_alarm_type_hints);
        super.init(context, attrs);
        setEntries(getResources().getStringArray(R.array.checker_add_alarm_types));
    }

    public CharSequence getEntry() {
        CharSequence alarmTypeHint = null;
        if (this.alarmTypeHints != null && getSelection() >= 0 && getSelection() < this.alarmTypeHints.length) {
            alarmTypeHint = this.alarmTypeHints[getSelection()];
        }
        if (TextUtils.isEmpty(alarmTypeHint)) {
            return super.getEntry();
        }
        return super.getEntry() + "\n\n" + alarmTypeHint;
    }
}
