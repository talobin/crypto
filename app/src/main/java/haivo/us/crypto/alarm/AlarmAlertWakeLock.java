package haivo.us.crypto.alarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

class AlarmAlertWakeLock {
    private static final String TAG = "AlarmAlertWakeLock";
    private static WakeLock sCpuWakeLock;

    AlarmAlertWakeLock() {
    }

    static WakeLock createPartialWakeLock(Context context) {
        return ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(1, TAG);
    }

    static void acquireCpuWakeLock(Context context) {
        if (sCpuWakeLock == null) {
            sCpuWakeLock = createPartialWakeLock(context);
            sCpuWakeLock.acquire();
        }
    }

    @SuppressLint({ "Wakelock" })
    static void acquireScreenCpuWakeLock(Context context) {
        if (sCpuWakeLock == null) {
            sCpuWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(805306369, TAG);
            sCpuWakeLock.acquire();
        }
    }

    static void releaseCpuLock() {
        if (sCpuWakeLock != null) {
            sCpuWakeLock.release();
            sCpuWakeLock = null;
        }
    }
}
