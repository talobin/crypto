package haivo.us.crypto.volley.generic;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class GzipVolleyStringRequest extends GzipVolleyRequest<String> {
    public GzipVolleyStringRequest(String url, Listener<String> listener, ErrorListener errorListener) {
        super(url, listener, errorListener);
    }

    protected String parseNetworkResponse(String responseString) throws Exception {
        return responseString;
    }
}
