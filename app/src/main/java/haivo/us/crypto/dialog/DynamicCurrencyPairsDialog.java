package haivo.us.crypto.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import haivo.us.crypto.R;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.util.AsyncTaskCompat;
import haivo.us.crypto.util.CheckErrorsUtils;
import haivo.us.crypto.util.CurrencyPairsMapHelper;
import haivo.us.crypto.util.FormatUtilsBase;
import haivo.us.crypto.util.HttpsHelper;
import haivo.us.crypto.volley.DynamicCurrencyPairsVolleyAsyncTask;

public abstract class DynamicCurrencyPairsDialog extends AlertDialog implements OnDismissListener {
    private CurrencyPairsMapHelper currencyPairsMapHelper;
    private DynamicCurrencyPairsVolleyAsyncTask dynamicCurrencyPairsVolleyAsyncTask;
    private final Market market;
    @BindView(R.id.refreshImageView) View refreshImageView;
    @BindView(R.id.refreshImageWrapper) View refreshImageWrapper;
    private final RequestQueue requestQueue;
    private ValueAnimator rotateAnim;
    @BindView(R.id.statusView) TextView statusView;

    /* renamed from: haivo.us.crypto.dialog.DynamicCurrencyPairsDialog.1 */
    class C01711 implements View.OnClickListener {
        C01711() {
        }

        public void onClick(View v) {
            DynamicCurrencyPairsDialog.this.startRefreshing();
        }
    }

    /* renamed from: haivo.us.crypto.dialog.DynamicCurrencyPairsDialog.2 */
    class C03242 implements Listener<CurrencyPairsMapHelper> {
        C03242() {
        }

        public void onResponse(CurrencyPairsMapHelper currencyPairsMapHelper) {
            DynamicCurrencyPairsDialog.this.dynamicCurrencyPairsVolleyAsyncTask = null;
            DynamicCurrencyPairsDialog.this.currencyPairsMapHelper = currencyPairsMapHelper;
            DynamicCurrencyPairsDialog.this.refreshStatusView(null);
            DynamicCurrencyPairsDialog.this.stopRefreshingAnim();
            DynamicCurrencyPairsDialog.this.onPairsUpdated(DynamicCurrencyPairsDialog.this.market,
                                                           currencyPairsMapHelper);
        }
    }

    /* renamed from: haivo.us.crypto.dialog.DynamicCurrencyPairsDialog.3 */
    class C03253 implements ErrorListener {
        C03253() {
        }

        public void onErrorResponse(VolleyError error) {
            DynamicCurrencyPairsDialog.this.dynamicCurrencyPairsVolleyAsyncTask = null;
            error.printStackTrace();
            DynamicCurrencyPairsDialog.this.refreshStatusView(CheckErrorsUtils.parseVolleyErrorMsg(
                DynamicCurrencyPairsDialog.this.getContext(),
                error));
            DynamicCurrencyPairsDialog.this.stopRefreshingAnim();
        }
    }

    public abstract void onPairsUpdated(Market market, CurrencyPairsMapHelper currencyPairsMapHelper);

    @SuppressLint({ "InflateParams" })
    protected DynamicCurrencyPairsDialog(Context context,
                                         Market market,
                                         CurrencyPairsMapHelper currencyPairsMapHelper) {
        super(context);
        setInverseBackgroundForced(true);
        this.requestQueue = HttpsHelper.newRequestQueue(context);
        this.market = market;
        this.currencyPairsMapHelper = currencyPairsMapHelper;
        setTitle(R.string.checker_add_dynamic_currency_pairs_dialog_title);
        setOnDismissListener(this);
        setButton(-3, context.getString(R.string.ok), (DialogInterface.OnClickListener) null);
        View view = LayoutInflater.from(context).inflate(R.layout.dynamic_currency_pairs_dialog, null);
        ButterKnife.bind((Object) this, view);
        this.refreshImageWrapper.setOnClickListener(new C01711());
        refreshStatusView(null);
        setView(view);
    }

    public void onDismiss(DialogInterface dialog) {
        this.requestQueue.cancelAll((Object) this);
        if (this.dynamicCurrencyPairsVolleyAsyncTask != null) {
            this.dynamicCurrencyPairsVolleyAsyncTask.cancel(true);
        }
        this.currencyPairsMapHelper = null;
    }

    private void startRefreshing() {
        setCancelable(false);
        startRefreshingAnim();
        this.dynamicCurrencyPairsVolleyAsyncTask = new DynamicCurrencyPairsVolleyAsyncTask(this.requestQueue,
                                                                                           getContext(),
                                                                                           this.market,
                                                                                           new C03242(),
                                                                                           new C03253());
        AsyncTaskCompat.execute(this.dynamicCurrencyPairsVolleyAsyncTask, new Void[0]);
    }

    private void refreshStatusView(String errorMsg) {
        String dateString;
        if (this.currencyPairsMapHelper == null || this.currencyPairsMapHelper.getDate() <= 0) {
            dateString = getContext().getString(R.string.checker_add_dynamic_currency_pairs_dialog_last_sync_never);
        } else {
            dateString = FormatUtilsBase.formatSameDayTimeOrDate(getContext(), this.currencyPairsMapHelper.getDate());
        }
        this.statusView.setText(getContext().getString(R.string.checker_add_dynamic_currency_pairs_dialog_last_sync,
                                                       new Object[] { dateString }));
        if (this.currencyPairsMapHelper != null && this.currencyPairsMapHelper.getPairsCount() > 0) {
            this.statusView.append("\n"
                                       + getContext().getString(R.string.checker_add_dynamic_currency_pairs_dialog_pairs,
                                                                new Object[] {
                                                                    Integer.valueOf(this.currencyPairsMapHelper.getPairsCount())
                                                                }));
        }
        if (errorMsg != null) {
            this.statusView.append("\n" + CheckErrorsUtils.formatError(getContext(), errorMsg));
        }
    }

    public void startRefreshingAnim() {
        setCancelable(false);
        this.refreshImageWrapper.setEnabled(false);
        this.rotateAnim = ObjectAnimator.ofFloat(this.refreshImageView, "rotation", 0.0f, 360.0f);
        this.rotateAnim.setDuration(750);
        this.rotateAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        this.rotateAnim.setRepeatCount(-1);
        this.rotateAnim.setRepeatMode(1);
        this.rotateAnim.start();
    }

    public void stopRefreshingAnim() {
        setCancelable(true);
        this.refreshImageWrapper.setEnabled(true);
        if (this.rotateAnim != null) {
            this.rotateAnim.cancel();
            this.rotateAnim = null;
            ViewHelper.setRotation(this.refreshImageView, 0.0f);
        }
    }
}
