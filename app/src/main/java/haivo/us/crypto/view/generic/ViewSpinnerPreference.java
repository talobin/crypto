package haivo.us.crypto.view.generic;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.BaseAdapter;

public class ViewSpinnerPreference extends ViewDialogPreference {
    private BaseAdapter adapter;
    private CharSequence[] entries;
    private OnItemSelectedListener onItemSelectedListener;
    private int selection;

    /* renamed from: haivo.us.crypto.view.generic.ViewSpinnerPreference.1 */
    class C02181 implements DialogInterface.OnClickListener {
        C02181() {
        }

        public void onClick(DialogInterface dialog, int which) {
            ViewSpinnerPreference.this.setSelection(which);
            dialog.dismiss();
        }
    }

    public interface OnItemSelectedListener {
        boolean onItemSelected(ViewSpinnerPreference viewSpinnerPreference, int i);
    }

    public ViewSpinnerPreference(Context context) {
        super(context);
        this.selection = -1;
    }

    public ViewSpinnerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.selection = -1;
    }

    @TargetApi(11)
    public ViewSpinnerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.selection = -1;
    }

    public void setEntries(CharSequence[] entries) {
        setEntriesAndSelection(entries, 0);
    }

    public void setEntriesAndSelection(CharSequence[] entries, int selection) {
        this.entries = entries;
        setSelection(selection);
    }

    public void setEntriesAndSelection(BaseAdapter adapter, CharSequence[] entries, int selection) {
        this.adapter = adapter;
        setEntriesAndSelection(entries, selection);
    }

    public CharSequence[] getEntries() {
        return this.entries;
    }

    public CharSequence getEntry() {
        if (this.entries == null || this.selection < 0 || this.selection >= this.entries.length) {
            return null;
        }
        return this.entries[this.selection];
    }

    public void setSelection(int selection) {
        setSelection(selection, true);
    }

    public void setSelection(int selection, boolean notifyChange) {
        if (getSelection() != selection && (!notifyChange
            || this.onItemSelectedListener == null
            || this.onItemSelectedListener.onItemSelected(this, selection))) {
            this.selection = selection;
        }
        setSummary(getEntry());
    }

    public int getSelection() {
        return this.selection;
    }

    protected void onPrepareDialog(Builder builder) {
        DialogInterface.OnClickListener onClickListener = new C02181();
        if (this.adapter == null) {
            builder.setSingleChoiceItems(this.entries, this.selection, onClickListener);
        } else {
            builder.setSingleChoiceItems(this.adapter, this.selection, onClickListener);
        }
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }
}
