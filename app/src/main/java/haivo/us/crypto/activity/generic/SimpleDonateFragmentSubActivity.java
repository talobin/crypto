package haivo.us.crypto.activity.generic;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import haivo.us.crypto.fragment.CheckersListFragment;
import haivo.us.crypto.fragment.MarketPickerListFragment;
import haivo.us.crypto.R;
import haivo.us.crypto.model.currency.VirtualCurrency;
import haivo.us.crypto.util.PreferencesUtils;
import haivo.us.crypto.util.Utils;

public abstract class SimpleDonateFragmentSubActivity<T extends Fragment> extends SimpleFragmentSubActivity<T> {
    private static final String EXTRA_SHOW_DONATE_DIALOG = "show_donate_dialog";
    private static final int REQ_DONATE_BTC = 10001;
    private static final int REQ_DONATE_DOGE = 10002;
    private static final int REQ_DONATE_LTC = 10003;
    protected static final String TAG;

    /* renamed from: haivo.us.crypto.activity.generic.SimpleDonateFragmentSubActivity.1 */
    class C01531 implements OnClickListener {
        C01531() {
        }

        public void onClick(DialogInterface dialog, int which) {
            SimpleDonateFragmentSubActivity.this.getIntent()
                                                .removeExtra(SimpleDonateFragmentSubActivity.EXTRA_SHOW_DONATE_DIALOG);
            switch (which) {
                case MarketPickerListFragment.SORT_MODE_ALPHABETICALLY /*0*/:
                    SimpleDonateFragmentSubActivity.this.makeDonationBtc(SimpleDonateFragmentSubActivity.this);
                    break;
                case CheckersListFragment.SORT_MODE_CURRENCY /*1*/:
                    SimpleDonateFragmentSubActivity.this.makeDonationDoge(SimpleDonateFragmentSubActivity.this);
                    break;
                case CheckersListFragment.SORT_MODE_EXCHANGE /*2*/:
                    SimpleDonateFragmentSubActivity.this.makeDonationLtc(SimpleDonateFragmentSubActivity.this);
                    break;
                default:
            }
        }
    }

    /* renamed from: haivo.us.crypto.activity.generic.SimpleDonateFragmentSubActivity.2 */
    class C01542 implements OnCancelListener {
        C01542() {
        }

        public void onCancel(DialogInterface dialog) {
            SimpleDonateFragmentSubActivity.this.getIntent()
                                                .removeExtra(SimpleDonateFragmentSubActivity.EXTRA_SHOW_DONATE_DIALOG);
        }
    }

    /* renamed from: haivo.us.crypto.activity.generic.SimpleDonateFragmentSubActivity.3 */
    class C01553 implements OnClickListener {
        C01553() {
        }

        public void onClick(DialogInterface dialog, int which) {
            SimpleDonateFragmentSubActivity.this.getIntent()
                                                .removeExtra(SimpleDonateFragmentSubActivity.EXTRA_SHOW_DONATE_DIALOG);
        }
    }

    /* renamed from: haivo.us.crypto.activity.generic.SimpleDonateFragmentSubActivity.4 */
    class C01564 implements OnClickListener {
        final /* synthetic */ Activity val$context;
        final /* synthetic */ String val$walletPackageName;

        C01564(Activity activity, String str) {
            this.val$context = activity;
            this.val$walletPackageName = str;
        }

        public void onClick(DialogInterface dialog, int which) {
            Utils.goToGooglePlay(this.val$context, this.val$walletPackageName);
        }
    }

    static {
        TAG = SimpleDonateFragmentSubActivity.class.getSimpleName();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra(EXTRA_SHOW_DONATE_DIALOG, false)) {
            showDonateDialog();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected static void startSimpleDonateFragmentActivity(Context context, Class<?> clazz, boolean showDonateDialog) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra(EXTRA_SHOW_DONATE_DIALOG, showDonateDialog);
        context.startActivity(intent);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQ_DONATE_BTC || requestCode == REQ_DONATE_DOGE || requestCode == REQ_DONATE_LTC)
            && resultCode == -1) {
            onDonateSuccess(true);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showDonateDialog() {
        new Builder(this).setTitle(R.string.settings_about_donate_title)
                         .setNegativeButton(R.string.cancel, new C01553())
                         .setOnCancelListener(new C01542())
                         .setItems(getResources().getStringArray(R.array.settings_about_donate_options), new C01531())
                         .show();
    }

    private final void makeDonationBtc(Activity context) {
        makeDonationVirtual(context,
                            VirtualCurrency.BTC,
                            REQ_DONATE_BTC,
                            "bitcoin:1DXzE9NgozeYpeDdsaja1AzxAorTwzonjT?amount=0.02",
                            "de.schildbach.wallet",
                            "Bitcoin");
    }

    private final void makeDonationDoge(Activity context) {
        makeDonationVirtual(context,
                            VirtualCurrency.DOGE,
                            REQ_DONATE_DOGE,
                            "dogecoin:D81kyZ49E132enb7ct7RcPGpjgsrN7bsd7?amount=45000",
                            "de.langerhans.wallet",
                            "Dogecoin");
    }

    private final void makeDonationLtc(Activity context) {
        makeDonationVirtual(context,
                            VirtualCurrency.LTC,
                            REQ_DONATE_LTC,
                            "litecoin:LhEvJtu1tBcaqtZHf1b3DyXyHBm3Wn8yfT?amount=1.8",
                            "de.schildbach.wallet_ltc",
                            "Litecoin");
    }

    private final void makeDonationVirtual(Activity context,
                                           String virtualCurrency,
                                           int reqCode,
                                           String uriString,
                                           String walletPackageName,
                                           String walletName) {
        Intent donateIntent = new Intent("android.intent.action.VIEW", Uri.parse(uriString));
        if (Utils.isIntentAvailable(context, donateIntent)) {
            context.startActivityForResult(donateIntent, reqCode);
            return;
        }
        new Builder(context).setTitle(R.string.settings_about_donate_fail_virtual_alert_title)
                            .setMessage(getString(R.string.settings_about_donate_fail_virtual_alert_text,
                                                  new Object[] { virtualCurrency, walletName }))
                            .setNegativeButton(R.string.cancel, null)
                            .setPositiveButton(R.string.ok, new C01564(context, walletPackageName))
                            .show();
    }

    protected void onDonateSuccess(boolean showSuccessToast) {
        if (showSuccessToast) {
            Utils.showToast((Context) this, (int) R.string.settings_about_donate_success, true);
        }
        PreferencesUtils.setDonationMade(this);
    }
}
