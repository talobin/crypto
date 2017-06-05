package haivo.us.crypto.view;

import android.annotation.TargetApi;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import haivo.us.crypto.R;
import haivo.us.crypto.view.generic.ViewSpinnerPreference;

public class ViewCurrencySpinnerPreference extends ViewSpinnerPreference {
    private OnClickListener onSyncClickedListener;
    private boolean showSyncButton;

    /* renamed from: haivo.us.crypto.view.ViewCurrencySpinnerPreference.1 */
    class C02141 implements DialogInterface.OnClickListener {
        C02141() {
        }

        public void onClick(DialogInterface dialog, int which) {
            if (ViewCurrencySpinnerPreference.this.onSyncClickedListener != null) {
                ViewCurrencySpinnerPreference.this.onSyncClickedListener.onClick(ViewCurrencySpinnerPreference.this);
            }
        }
    }

    public ViewCurrencySpinnerPreference(Context context) {
        super(context);
    }

    public ViewCurrencySpinnerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(11)
    public ViewCurrencySpinnerPreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setShowSyncButton(boolean showSyncButton) {
        this.showSyncButton = showSyncButton;
    }

    protected void onPrepareDialog(Builder builder) {
        super.onPrepareDialog(builder);
        if (this.showSyncButton) {
            builder.setPositiveButton(R.string.checker_add_check_currency_dst_dialog_sync, new C02141());
        }
    }

    public void setOnSyncClickedListener(OnClickListener onSyncClickedListener) {
        this.onSyncClickedListener = onSyncClickedListener;
    }
}
