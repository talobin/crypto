package haivo.us.crypto.view.generic;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class ViewCheckBoxPreference extends ViewTwoStatePreference {
    public ViewCheckBoxPreference(Context context) {
        super(context);
    }

    public ViewCheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public ViewCheckBoxPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected CompoundButton createCompoundButton() {
        CheckBox checkBox = new CheckBox(getContext());
        checkBox.setClickable(false);
        return checkBox;
    }
}
