package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class Gemini extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Gemini";
    private static final String TTS_NAME = "Gemini";
    private static final String URL = "https://api.gemini.com/v1/book/%1$s%2$s?limit_asks=1&limit_bids=1";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.USD });
        CURRENCY_PAIRS.put(VirtualCurrency.ETH, new String[] { Currency.USD, VirtualCurrency.BTC });
    }

    public Gemini() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter());
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONArray bidsArray = jsonObject.getJSONArray("bids");
        if (bidsArray != null && bidsArray.length() > 0) {
            ticker.bid = bidsArray.getJSONObject(0).getDouble("price");
        }
        JSONArray asksArray = jsonObject.getJSONArray("asks");
        if (asksArray != null && asksArray.length() > 0) {
            ticker.ask = asksArray.getJSONObject(0).getDouble("price");
        }
        if (ticker.bid != -1.0d && ticker.ask != -1.0d) {
            ticker.last = (ticker.bid + ticker.ask) / 2.0d;
        } else if (ticker.bid != -1.0d) {
            ticker.last = ticker.bid;
        } else if (ticker.ask != -1.0d) {
            ticker.last = ticker.ask;
        } else {
            ticker.last = 0.0d;
        }
    }
}
