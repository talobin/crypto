package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class Koinim extends Market {
    public static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    public static final String NAME = "Koinim";
    public static final String TTS_NAME = "Koinim";
    public static final String URL_BTC = "https://koinim.com/ticker/";
    public static final String URL_LTC = "https://koinim.com/ticker/ltc/";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.TRY});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{Currency.TRY});
    }

    public Koinim() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        if (VirtualCurrency.LTC.equals(checkerInfo.getCurrencyBase())) {
            return URL_LTC;
        }
        return URL_BTC;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = jsonObject.getDouble("buy");
        ticker.ask = jsonObject.getDouble("sell");
        ticker.vol = jsonObject.getDouble("volume");
        ticker.high = jsonObject.getDouble("high");
        ticker.low = jsonObject.getDouble("low");
        ticker.last = jsonObject.getDouble("last_order");
    }
}
