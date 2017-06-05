package haivo.us.crypto.view.generic;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import haivo.us.crypto.R;

public abstract class ViewDialogPreference extends ViewPreference {
    private AlertDialog dialog;
    private CharSequence dialogTitle;

    public abstract CharSequence getEntry();

    protected abstract void onPrepareDialog(Builder builder);

    public ViewDialogPreference(Context context) {
        super(context);
    }

    public ViewDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public ViewDialogPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewDialogPreference);
            setDialogTitle(a.getString(R.styleable.ViewDialogPreference_dialogTitle));
            a.recycle();
        }
    }

    public void setDialogTitle(CharSequence dialogTitle) {
        if (TextUtils.isEmpty(dialogTitle)) {
            dialogTitle = getTitle();
        }
        this.dialogTitle = dialogTitle;
    }

    public CharSequence getDialogTitle() {
        return this.dialogTitle;
    }

    public void onClick(View v) {
        Builder builder = new Builder(getContext());
        builder.setInverseBackgroundForced(true);
        builder.setTitle(this.dialogTitle);
        builder.setNegativeButton(R.string.cancel, null);
        onPrepareDialog(builder);
        this.dialog = builder.create();
        onPreDialogShow(this.dialog);
        this.dialog.show();
    }

    protected AlertDialog getDialog() {
        return this.dialog;
    }

    protected void onPreDialogShow(AlertDialog dialog) {
    }
}
