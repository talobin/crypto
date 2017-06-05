package haivo.us.crypto.mechanoid;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import haivo.us.crypto.mechanoid.ops.OpsInitializer;

public class Mechanoid {
    private static Mechanoid sInstance;
    Context mApplicationContext;

    public static Context getApplicationContext() {
        return get().mApplicationContext;
    }

    public static ContentResolver getContentResolver() {
        return get().mApplicationContext.getContentResolver();
    }

    public static ComponentName startService(Intent intent) {
        return get().mApplicationContext.startService(intent);
    }

    public static Mechanoid get() {
        if (sInstance != null) {
            return sInstance;
        }
        throw new MechanoidNotInitializedException();
    }

    private Mechanoid(Context context) {
        this.mApplicationContext = context.getApplicationContext();
    }

    public static void init(Context context) {
        if (sInstance == null) {
            sInstance = new Mechanoid(context);
            OpsInitializer.init();
        }
    }
}
