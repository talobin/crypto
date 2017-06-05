package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Livecoin extends Market {
    private static final String NAME = "Livecoin";
    private static final String TTS_NAME = "Live coin";
    private static final String URL = "https://api.livecoin.net/exchange/ticker?currencyPair=%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://api.livecoin.net/exchange/ticker";

    public Livecoin() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyPairId()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        if (!jsonObject.isNull("best_bid")) {
            ticker.bid = jsonObject.getDouble("best_bid");
        }
        if (!jsonObject.isNull("best_ask")) {
            ticker.ask = jsonObject.getDouble("best_ask");
        }
        if (!jsonObject.isNull("volume")) {
            ticker.vol = jsonObject.getDouble("volume");
        }
        if (!jsonObject.isNull("high")) {
            ticker.high = jsonObject.getDouble("high");
        }
        if (!jsonObject.isNull("low")) {
            ticker.low = jsonObject.getDouble("low");
        }
        ticker.last = jsonObject.getDouble("last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs) throws
                                                                                                          Exception {
        JSONArray tickerArray = new JSONArray(responseString);
        for (int i = 0; i < tickerArray.length(); i++) {
            String symbol = tickerArray.getJSONObject(i).getString("symbol");
            String[] currencyNames = symbol.split("/");
            if (currencyNames != null && currencyNames.length >= 2) {
                pairs.add(new CurrencyPairInfo(currencyNames[0], currencyNames[1], symbol));
            }
        }
    }
}
