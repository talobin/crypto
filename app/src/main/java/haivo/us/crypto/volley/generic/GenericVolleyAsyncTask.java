package haivo.us.crypto.volley.generic;

import android.os.AsyncTask;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import java.util.concurrent.ExecutionException;
import haivo.us.crypto.volley.UnknownVolleyError;

public abstract class GenericVolleyAsyncTask<T> extends AsyncTask<Void, Void, Object> {
    private final Response.ErrorListener errorListener;
    private final Response.Listener<T> listener;
    protected final RequestQueue requestQueue;

    public GenericVolleyAsyncTask(RequestQueue requestQueue,
                                  Response.Listener<T> listener,
                                  Response.ErrorListener errorListener) {
        this.requestQueue = requestQueue;
        this.listener = listener;
        this.errorListener = errorListener;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected final /* varargs */ Object doInBackground(Void... object) {
        try {
            return this.doNetworkInBackground();
        } catch (ExecutionException var2_3) {
            if (var2_3.getCause() == null) return var2_3;
            return var2_3.getCause();
        } catch (Throwable var1_2) {
            return var1_2;
        }
    }

    protected abstract Object doNetworkInBackground() throws Exception;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);
        if (this.isCancelled() || object == null) {
            return;
        }
        if (object instanceof Throwable) {
            if (object instanceof VolleyError) {
                this.errorListener.onErrorResponse((VolleyError) object);
                return;
            }
            this.errorListener.onErrorResponse(new UnknownVolleyError((Throwable) object));
            return;
        }
        try {
            this.listener.onResponse((T) object);
            return;
        } catch (Throwable var1_2) {
            var1_2.printStackTrace();
            return;
        }
    }
}
