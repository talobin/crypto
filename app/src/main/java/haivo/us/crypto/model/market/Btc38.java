package haivo.us.crypto.model.market;

import java.util.List;
import java.util.Locale;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONArray;
import org.json.JSONObject;

public class Btc38 extends Market {
    private static final String NAME = "Btc38";
    private static final String TTS_NAME = "BTC 38";
    private static final String URL = "http://api.btc38.com/v1/ticker.php?c=%1$s&mk_type=%2$s";
    private static final String URL_CURRENCY_PAIRS = "http://api.btc38.com/v1/ticker.php?c=all&mk_type=%1$s";

    public Btc38() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL,
                             new Object[] {
                                 checkerInfo.getCurrencyBaseLowerCase(),
                                 checkerInfo.getCurrencyCounterLowerCase()
                             });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject tickerJsonObject = jsonObject.getJSONObject("ticker");
        ticker.bid = tickerJsonObject.getDouble("buy");
        ticker.ask = tickerJsonObject.getDouble("sell");
        ticker.vol = tickerJsonObject.getDouble("vol");
        ticker.high = tickerJsonObject.getDouble("high");
        ticker.low = tickerJsonObject.getDouble("low");
        ticker.last = tickerJsonObject.getDouble("last");
    }

    public int getCurrencyPairsNumOfRequests() {
        return 2;
    }

    public String getCurrencyCounter(int requestId) {
        return requestId == 0 ? Currency.CNY : VirtualCurrency.BTC;
    }

    public String getCurrencyPairsUrl(int requestId) {
        return String.format(URL_CURRENCY_PAIRS,
                             new Object[] { getCurrencyCounter(requestId).toLowerCase(Locale.ENGLISH) });
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        String currencyCounter = getCurrencyCounter(requestId);
        JSONArray currencyBaseList = jsonObject.names();
        for (int i = 0; i < currencyBaseList.length(); i++) {
            pairs.add(new CurrencyPairInfo(currencyBaseList.getString(i).toUpperCase(Locale.ENGLISH),
                                           currencyCounter,
                                           null));
        }
    }
}
