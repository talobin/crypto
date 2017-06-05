package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class Virtex extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "CaVirtEx";
    private static final String TTS_NAME = "CaVirtEx";
    private static final String URL = "https://cavirtex.com/api2/ticker.json";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.CAD, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{Currency.CAD});
    }

    public Virtex() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject pairJsonObject = jsonObject.getJSONObject("ticker").getJSONObject(checkerInfo.getCurrencyBase() + checkerInfo.getCurrencyCounter());
        if (!pairJsonObject.isNull("buy")) {
            ticker.bid = pairJsonObject.getDouble("buy");
        }
        if (!pairJsonObject.isNull("sell")) {
            ticker.ask = pairJsonObject.getDouble("sell");
        }
        if (!pairJsonObject.isNull("volume")) {
            ticker.vol = pairJsonObject.getDouble("volume");
        }
        if (!pairJsonObject.isNull("high")) {
            ticker.high = pairJsonObject.getDouble("high");
        }
        if (!pairJsonObject.isNull("low")) {
            ticker.low = pairJsonObject.getDouble("low");
        }
        ticker.last = pairJsonObject.getDouble("last");
    }
}
