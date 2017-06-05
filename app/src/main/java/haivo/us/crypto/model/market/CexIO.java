package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONArray;
import org.json.JSONObject;

public class CexIO extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "CEX.IO";
    private static final String TTS_NAME = "CEX IO";
    private static final String URL = "https://cex.io/api/ticker/%1$s/%2$s";
    private static final String URL_CURRENCY_PAIRS = "https://cex.io/api/currency_limits";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC,
                           new String[] { Currency.USD, Currency.EUR, Currency.GBP, Currency.RUB });
        CURRENCY_PAIRS.put(VirtualCurrency.ETH, new String[] { Currency.USD, Currency.EUR, VirtualCurrency.BTC });
        CURRENCY_PAIRS.put(VirtualCurrency.GHS, new String[] { VirtualCurrency.BTC });
    }

    public CexIO() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        if (jsonObject.has("bid")) {
            ticker.bid = jsonObject.getDouble("bid");
        }
        if (jsonObject.has("ask")) {
            ticker.ask = jsonObject.getDouble("ask");
        }
        ticker.vol = jsonObject.getDouble("volume");
        ticker.high = jsonObject.getDouble("high");
        ticker.low = jsonObject.getDouble("low");
        ticker.last = jsonObject.getDouble("last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray pairsJsonArray = jsonObject.getJSONObject("data").getJSONArray("pairs");
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            JSONObject pairJsonObject = pairsJsonArray.getJSONObject(i);
            String currencyBase = pairJsonObject.getString("symbol1");
            String currencyCounter = pairJsonObject.getString("symbol2");
            if (!(currencyBase == null || currencyCounter == null)) {
                pairs.add(new CurrencyPairInfo(currencyBase, currencyCounter, null));
            }
        }
    }
}
