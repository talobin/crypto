package haivo.us.crypto;

import android.app.Application;
import haivo.us.crypto.model.market.SwissCex;
import haivo.us.crypto.util.SoundFilesManager;
import haivo.us.crypto.volley.UserCountryDetectionAsyncTask;
import haivo.us.crypto.mechanoid.Mechanoid;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        SwissCex.API_KEY = "8grbc7eab79imc2r9co6fb0q5";
        Mechanoid.init(this);
        SoundFilesManager.installRingtonesIfNeeded(this);
        UserCountryDetectionAsyncTask.setupUserCountry(this);
    }
}
