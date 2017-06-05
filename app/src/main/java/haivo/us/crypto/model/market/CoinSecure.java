package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class CoinSecure extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "CoinSecure";
    private static final String TTS_NAME = "Coin Secure";
    private static final String URL = "https://api.coinsecure.in/v0/noauth/newticker";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.INR});
    }

    public CoinSecure() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = parsePrice(jsonObject.getDouble("bid"));
        ticker.ask = parsePrice(jsonObject.getDouble("ask"));
        ticker.vol = jsonObject.getDouble("coinvolume") / 1.0E8d;
        ticker.high = parsePrice(jsonObject.getDouble("high"));
        ticker.low = parsePrice(jsonObject.getDouble("low"));
        ticker.last = parsePrice(jsonObject.getDouble("lastprice"));
    }

    private double parsePrice(double price) {
        return price / 100.0d;
    }
}
