package haivo.us.crypto.volley.generic;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import haivo.us.crypto.mechanoid.net.Response;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;

public abstract class GzipVolleyRequest<T> extends Request<T> {
    private static final int MAX_REDIRECTION_COUNT = 3;
    private final Map<String, String> headers;
    private final Listener<T> listener;
    private int redirectionCount;
    private String redirectionUrl;
    private RequestQueue requestQueue;

    protected abstract T parseNetworkResponse(String str) throws Exception;

    public GzipVolleyRequest(String url, Listener<T> listener, ErrorListener errorListener) {
        super(0, url, errorListener);
        this.redirectionUrl = null;
        this.listener = listener;
        this.headers = new HashMap();
        this.headers.put("Accept-Encoding", "gzip");
        this.headers.put("User-Agent", "Crypto Kit (gzip)");
    }

    public RequestQueue getRequestQueue() {
        return this.requestQueue;
    }

    public String getUrl() {
        if (this.redirectionUrl != null) {
            return this.redirectionUrl;
        }
        return super.getUrl();
    }

    public Map<String, String> getHeaders() throws AuthFailureError {
        return this.headers != null ? this.headers : super.getHeaders();
    }

    public Request<?> setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
        return super.setRequestQueue(requestQueue);
    }

    public void deliverError(VolleyError error) {
        if (!(error == null || error.networkResponse == null)) {
            int statusCode = error.networkResponse.statusCode;
            if (statusCode == Response.HTTP_MOVED_PERM || statusCode == Response.HTTP_MOVED_TEMP) {
                String location = (String) error.networkResponse.headers.get("Location");
                if (location != null && this.redirectionCount < MAX_REDIRECTION_COUNT) {
                    this.redirectionCount++;
                    this.redirectionUrl = location;
                    this.requestQueue.add(this);
                    return;
                }
            }
        }
        super.deliverError(error);
    }

    protected void deliverResponse(T response) {
        this.listener.onResponse(response);
    }

    protected com.android.volley.Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseString = BuildConfig.VERSION_NAME;
            String encoding = (String) response.headers.get("Content-Encoding");
            if (encoding == null || !encoding.contains("gzip")) {
                responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            } else {
                responseString = decodeGZip(response.data);
            }
            return com.android.volley.Response.success(parseNetworkResponse(responseString),
                                                       HttpHeaderParser.parseCacheHeaders(response));
        } catch (Throwable e) {
            return com.android.volley.Response.error(new ParseError(e));
        }
    }

    private String decodeGZip(byte[] data) throws Throwable {
        GZIPInputStream gzis = null;
        Exception e;
        String responseString = BuildConfig.VERSION_NAME;
        BufferedReader in = null;
        InputStreamReader reader2 = null;
        try {
            ByteArrayInputStream bais2 = new ByteArrayInputStream(data);
            try {
                gzis = new GZIPInputStream(bais2);

                reader2 = new InputStreamReader(gzis);
                BufferedReader in2 = new BufferedReader(reader2);
                while (true) {
                    String readed = in2.readLine();
                    if (readed == null) {
                        break;
                    }
                    responseString = responseString + readed + "\n";
                }
                if (bais2 != null) {
                    try {
                        bais2.close();
                    } catch (Exception e5) {
                    }
                }
                if (gzis != null) {
                    gzis.close();
                }
                if (reader2 != null) {
                    reader2.close();
                }
                if (in2 != null) {
                    in2.close();
                }
            } catch (Exception e2) {
                try {
                    throw e2;
                } catch (Throwable th2) {
                }
            } catch (Throwable th3) {
                if (bais2 != null) {
                    try {
                        bais2.close();
                    } catch (Exception e3) {
                        throw th3;
                    }
                }

                if (reader2 != null) {
                    reader2.close();
                }
                if (in != null) {
                    in.close();
                }

                if (gzis != null) {
                    gzis.close();
                }

                throw th3;
            }
        } catch (Exception e8) {
            e = e8;
            throw e;
        }
        return responseString;
    }
}
