package haivo.us.crypto.volley;

import android.content.Context;
import android.text.TextUtils;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.RequestFuture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.CurrencyPairsListWithDate;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.util.CurrencyPairsMapHelper;
import haivo.us.crypto.util.MarketCurrencyPairsStore;
import haivo.us.crypto.volley.generic.GenericVolleyAsyncTask;
import haivo.us.crypto.volley.generic.GzipVolleyStringRequest;

public class DynamicCurrencyPairsVolleyAsyncTask extends GenericVolleyAsyncTask<CurrencyPairsMapHelper> {
    private final Context context;
    private final Market market;

    public DynamicCurrencyPairsVolleyAsyncTask(RequestQueue requestQueue,
                                               Context context,
                                               Market market,
                                               Listener<CurrencyPairsMapHelper> listener,
                                               ErrorListener errorListener) {
        super(requestQueue, listener, errorListener);
        this.context = context;
        this.market = market;
    }

    protected Object doNetworkInBackground() throws Exception {
        List<CurrencyPairInfo> pairs = new ArrayList();
        int numOfRequests = this.market.getCurrencyPairsNumOfRequests();
        if (!isCancelled()) {
            List<CurrencyPairInfo> nextPairs = new ArrayList();
            for (int requestId = 0; requestId < numOfRequests; requestId++) {
                try {
                    String nextUrl = this.market.getCurrencyPairsUrl(requestId);
                    if (!(isCancelled() || TextUtils.isEmpty(nextUrl))) {
                        RequestFuture<String> future = RequestFuture.newFuture();
                        GzipVolleyStringRequest request = new GzipVolleyStringRequest(nextUrl, future, future);
                        if (requestId == 0) {
                            request.setRetryPolicy(new DefaultRetryPolicy(8000,
                                                                          1,
                                                                          DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        }
                        this.requestQueue.add(request);
                        String responseString = (String) future.get();
                        nextPairs.clear();
                        try {
                            this.market.parseCurrencyPairsMain(requestId, responseString, nextPairs);
                        } catch (Throwable e) {
                            if (requestId == 0) {
                                return new ParseError(e);
                            }
                        }
                        pairs.addAll(nextPairs);
                    }
                } catch (Exception e2) {
                    if (requestId == 0) {
                        throw e2;
                    }
                }
            }
        }
        Collections.sort(pairs);
        CurrencyPairsListWithDate currencyPairsListWithDate = new CurrencyPairsListWithDate();
        currencyPairsListWithDate.date = System.currentTimeMillis();
        currencyPairsListWithDate.pairs = pairs;
        if (pairs != null && pairs.size() > 0) {
            MarketCurrencyPairsStore.savePairsForMarket(this.context, this.market.key, currencyPairsListWithDate);
        }
        return new CurrencyPairsMapHelper(currencyPairsListWithDate);
    }
}
