package haivo.us.crypto.volley;

import android.content.Context;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.RequestFuture;
import java.util.Locale;
import org.json.JSONObject;
import haivo.us.crypto.util.AsyncTaskCompat;
import haivo.us.crypto.util.HttpsHelper;
import haivo.us.crypto.util.PreferencesUtils;
import haivo.us.crypto.volley.generic.GzipVolleyStringRequest;

public class UserCountryDetectionAsyncTask extends AsyncTask<Void, Void, Void> {
    private final Context appContext;
    private final RequestQueue requestQueue;

    private UserCountryDetectionAsyncTask(Context context) {
        this.appContext = context.getApplicationContext();
        this.requestQueue = HttpsHelper.newRequestQueue(context);
    }

    protected Void doInBackground(Void... params) {
        updateUserRegion();
        return null;
    }

    public boolean updateUserRegion() {
        String userCountry = getCountryCodeFromTelephonyNetwork();
        if (TextUtils.isEmpty(userCountry)) {
            userCountry = getCountryCodeFromInternet();
        }
        if (TextUtils.isEmpty(userCountry)) {
            userCountry = getCountryCodeSystemLocale();
        }
        if (TextUtils.isEmpty(userCountry)) {
            return false;
        }
        PreferencesUtils.setUserCountry(this.appContext, userCountry);
        return true;
    }

    private String getCountryCodeFromTelephonyNetwork() {
        String str = null;
        try {
            TelephonyManager telephonyManager =
                (TelephonyManager) this.appContext.getSystemService(Context.TELEPHONY_SERVICE);
            if (telephonyManager != null) {
                String countryIso = telephonyManager.getNetworkCountryIso();
                if (countryIso != null) {
                    str = countryIso.toUpperCase(Locale.ENGLISH);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    private String getCountryCodeFromInternet() {
        try {
            RequestFuture<String> future = RequestFuture.newFuture();
            GzipVolleyStringRequest request = new GzipVolleyStringRequest("http://ip-api.com/json", future, future);
            request.setRetryPolicy(new DefaultRetryPolicy(8000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            this.requestQueue.add(request);
            String responseString = (String) future.get();
            if (responseString != null) {
                return new JSONObject(responseString).getString("countryCode");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getCountryCodeSystemLocale() {
        try {
            String country;
            Locale locale = this.appContext.getResources().getConfiguration().locale;
            if (locale != null) {
                country = locale.getCountry();
            } else {
                country = null;
            }
            if (TextUtils.isEmpty(country)) {
                country = Locale.getDefault().getCountry();
            }
            if (TextUtils.isEmpty(country)) {
                return null;
            }
            return country;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void setupUserCountry(Context context) {
        if (TextUtils.isEmpty(PreferencesUtils.getUserCountry(context))) {
            AsyncTaskCompat.execute(new UserCountryDetectionAsyncTask(context), new Void[0]);
        }
    }
}
