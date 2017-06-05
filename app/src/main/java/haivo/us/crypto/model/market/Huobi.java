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

public class Huobi extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Huobi";
    private static final String TTS_NAME = "Huobi";
    private static final String URL_BTC = "http://api.huobi.com/staticmarket/ticker_btc_json.js";
    private static final String URL_BTC_USD = "http://api.huobi.com/usdmarket/ticker_btc_json.js";
    private static final String URL_LTC = "http://api.huobi.com/staticmarket/ticker_ltc_json.js";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.CNY, Currency.USD});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{Currency.CNY});
    }

    public Huobi() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        if (VirtualCurrency.LTC.equals(checkerInfo.getCurrencyBase())) {
            return URL_LTC;
        }
        if (Currency.USD.equals(checkerInfo.getCurrencyCounter())) {
            return URL_BTC_USD;
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
