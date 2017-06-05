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

public class SevenNineSix extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "796";
    private static final String TTS_NAME = "796";
    private static final String URL_BTC = "http://api.796.com/v3/futures/ticker.html?type=weekly";
    private static final String URL_LTC = "http://api.796.com/v3/futures/ticker.html?type=ltc";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.USD});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{Currency.USD});
    }

    public SevenNineSix() {
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
        JSONObject tickerJsonObject = jsonObject.getJSONObject("ticker");
        ticker.bid = ParseUtils.getDoubleFromString(tickerJsonObject, "buy");
        ticker.ask = ParseUtils.getDoubleFromString(tickerJsonObject, "sell");
        ticker.vol = ParseUtils.getDoubleFromString(tickerJsonObject, "vol");
        ticker.high = ParseUtils.getDoubleFromString(tickerJsonObject, "high");
        ticker.low = ParseUtils.getDoubleFromString(tickerJsonObject, "low");
        ticker.last = ParseUtils.getDoubleFromString(tickerJsonObject, "last");
    }
}
