package haivo.us.crypto.volley;

import android.text.TextUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.RequestFuture;
import haivo.us.crypto.content.CheckerRecord;
import java.util.ArrayList;
import java.util.Iterator;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.util.CheckerRecordHelper;
import haivo.us.crypto.util.MarketsConfigUtils;
import haivo.us.crypto.volley.generic.GenericVolleyAsyncTask;
import haivo.us.crypto.volley.generic.GzipVolleyStringRequest;

public class CheckerVolleyAsyncTask extends GenericVolleyAsyncTask<Ticker> implements CheckerRecordRequestIfc {
    private static final ArrayList<CheckerVolleyAsyncTask> CHECKERS_TASKS;
    protected final CheckerInfo checkerInfo;
    private final CheckerRecord checkerRecord;

    static {
        CHECKERS_TASKS = new ArrayList();
    }

    public CheckerVolleyAsyncTask(RequestQueue requestQueue,
                                  CheckerRecord checkerRecord,
                                  Listener<Ticker> listener,
                                  ErrorListener errorListener) {
        super(requestQueue, listener, errorListener);
        this.checkerRecord = checkerRecord;
        this.checkerInfo = CheckerRecordHelper.createCheckerInfo(checkerRecord);
    }

    public long getCheckerRecordId() {
        return this.checkerRecord != null ? this.checkerRecord.getId() : -1;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        if (CHECKERS_TASKS != null) {
            CHECKERS_TASKS.add(this);
        }
    }

    protected Object doNetworkInBackground() throws Exception {
        Market market = MarketsConfigUtils.getMarketByKey(this.checkerRecord.getMarketKey());
        String responseString = null;
        Ticker ticker = null;
        RequestFuture<String> future = RequestFuture.newFuture();
        String url = market.getUrl(0, this.checkerInfo);
        if (!(isCancelled() || TextUtils.isEmpty(url))) {
            GzipVolleyStringRequest request = new GzipVolleyStringRequest(url, future, future);
            request.setRetryPolicy(new DefaultRetryPolicy(8000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            this.requestQueue.add(request);
            responseString = (String) future.get();
        }
        try {
            ticker = market.parseTickerMain(0, responseString, new Ticker(), this.checkerInfo);
        } catch (Throwable th) {
        }
        if (ticker != null) {
            if (ticker.last > -1.0d) {
                int numOfRequests = market.getNumOfRequests(this.checkerInfo);
                if (isCancelled() || numOfRequests <= 1) {
                    return ticker;
                }
                for (int requestId = 1; requestId < numOfRequests; requestId++) {
                    try {
                        String nextUrl = market.getUrl(requestId, this.checkerInfo);
                        if (!(isCancelled() || TextUtils.isEmpty(nextUrl))) {
                            RequestFuture<String> nextFuture = RequestFuture.newFuture();
                            this.requestQueue.add(new GzipVolleyStringRequest(nextUrl, nextFuture, nextFuture));
                            market.parseTickerMain(requestId, (String) nextFuture.get(), ticker, this.checkerInfo);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                return ticker;
            }
        }
        try {
            return new CheckerErrorParsedError(market.parseErrorMain(0, responseString, this.checkerInfo));
        } catch (Throwable e2) {
            return new ParseError(e2);
        }
    }

    protected void onPostExecute(Object result) {
        if (CHECKERS_TASKS != null) {
            CHECKERS_TASKS.remove(this);
        }
        super.onPostExecute(result);
    }

    protected void onCancelled() {
        if (CHECKERS_TASKS != null) {
            CHECKERS_TASKS.remove(this);
        }
        super.onCancelled();
    }

    public static void cancelCheckingForCheckerRecord(long checkerRecordId) {
        if (CHECKERS_TASKS != null && checkerRecordId > 0) {
            Iterator it = CHECKERS_TASKS.iterator();
            while (it.hasNext()) {
                CheckerVolleyAsyncTask checkerVolleyAsyncTask = (CheckerVolleyAsyncTask) it.next();
                if (checkerVolleyAsyncTask != null && checkerVolleyAsyncTask.getCheckerRecordId() == checkerRecordId) {
                    checkerVolleyAsyncTask.cancel(true);
                }
            }
        }
    }
}
