package haivo.us.crypto.view.generic;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import haivo.us.crypto.R;

public abstract class ViewTwoStatePreference extends ViewPreference {
    CompoundButton compoundButton;
    private boolean isChecked;
    private OnCheckChangedListener onCheckChangedListener;

    /* renamed from: haivo.us.crypto.view.generic.ViewTwoStatePreference.1 */
    class C02191 implements OnCheckedChangeListener {
        C02191() {
        }

        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            ViewTwoStatePreference.this.setChecked(isChecked);
        }
    }

    public interface OnCheckChangedListener {
        boolean onCheckChanged(ViewTwoStatePreference viewTwoStatePreference, boolean z);
    }

    protected abstract CompoundButton createCompoundButton();

    public ViewTwoStatePreference(Context context) {
        super(context);
    }

    public ViewTwoStatePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public ViewTwoStatePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.compoundButton = createCompoundButton();
        this.compoundButton.setFocusable(false);
        this.compoundButton.setDuplicateParentStateEnabled(true);
        this.compoundButton.setOnCheckedChangeListener(new C02191());
        setWidget(this.compoundButton);
        setChecked(this.isChecked);
    }

    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPreference);
            this.isChecked = a.getBoolean(R.styleable.ViewPreference_isChecked, false);
            a.recycle();
        }
    }

    public void setChecked(boolean checked) {
        if (isChecked() != checked) {
            if (this.onCheckChangedListener == null || this.onCheckChangedListener.onCheckChanged(this, checked)) {
                this.isChecked = checked;
                this.compoundButton.setChecked(checked);
            }
        }
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void toggle() {
        setChecked(!isChecked());
    }

    public void onClick(View v) {
        toggle();
    }

    public void setOnCheckChangedListener(OnCheckChangedListener onCheckChangedListener) {
        this.onCheckChangedListener = onCheckChangedListener;
    }
}
