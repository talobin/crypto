package haivo.us.crypto.util;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build.VERSION;

public class AsyncTaskCompat {
    @TargetApi(11)
    public static <T> void execute(AsyncTask<T, ?, ?> asyncTask, T... params) {
        if (VERSION.SDK_INT >= 11) {
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        } else {
            asyncTask.execute(params);
        }
    }
}
