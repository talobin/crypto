package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import haivo.us.crypto.util.ParseUtils;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class Zaydo extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Zaydo";
    private static final String TTS_NAME = "Zaydo";
    private static final String URL = "http://chart.zyado.com/ticker.json";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.EUR});
    }

    public Zaydo() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = ParseUtils.getDoubleFromString(jsonObject, "bid");
        ticker.ask = ParseUtils.getDoubleFromString(jsonObject, "ask");
        ticker.vol = ParseUtils.getDoubleFromString(jsonObject, "volume");
        ticker.high = ParseUtils.getDoubleFromString(jsonObject, "high");
        ticker.low = ParseUtils.getDoubleFromString(jsonObject, "low");
        ticker.last = ParseUtils.getDoubleFromString(jsonObject, "last");
    }
}
