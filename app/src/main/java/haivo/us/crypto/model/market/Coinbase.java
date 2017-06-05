package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Coinbase extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Coinbase";
    private static final String TTS_NAME = "Coinbase";
    private static final String URL = "https://api.gdax.com/products/%1$s-%2$s/ticker";
    private static final String URL_CURRENCY_PAIRS = "https://api.gdax.com/products/";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.USD, Currency.EUR, Currency.GBP});
    }

    public Coinbase() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = jsonObject.getDouble("bid");
        ticker.ask = jsonObject.getDouble("ask");
        ticker.vol = jsonObject.getDouble("volume");
        ticker.last = jsonObject.getDouble("price");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs) throws
                                                                                                          Exception {
        JSONArray jsonArray = new JSONArray(responseString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject pairJsonObject = jsonArray.getJSONObject(i);
            pairs.add(new CurrencyPairInfo(pairJsonObject.getString("base_currency"), pairJsonObject.getString("quote_currency"), pairJsonObject.getString("id")));
        }
    }
}
