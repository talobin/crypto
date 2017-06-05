package haivo.us.crypto.view.generic;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import de.ankri.views.Switch;

public class ViewSwitchPreference extends ViewTwoStatePreference {
    public ViewSwitchPreference(Context context) {
        super(context);
    }

    public ViewSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public ViewSwitchPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected CompoundButton createCompoundButton() {
        return new Switch(getContext());
    }
}
