package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class CoinJar extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "CoinJar";
    private static final String TTS_NAME = "Coin Jar";
    private static final String URL = "https://coinjar-data.herokuapp.com/fair_rate.json";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.USD, Currency.AUD, Currency.NZD, Currency.CAD, Currency.EUR, Currency.GBP, Currency.SGD, Currency.HKD, Currency.CHF, Currency.JPY});
    }

    public CoinJar() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        String currencyCounter = checkerInfo.getCurrencyCounter();
        ticker.bid = getPriceFromJsonObject(jsonObject, "bid", currencyCounter);
        ticker.ask = getPriceFromJsonObject(jsonObject, "ask", currencyCounter);
        ticker.last = getPriceFromJsonObject(jsonObject, "spot", currencyCounter);
    }

    private double getPriceFromJsonObject(JSONObject jsonObject, String innerObjectName, String currencyCounter) throws
                                                                                                                 Exception {
        return jsonObject.getJSONObject(innerObjectName).getDouble(currencyCounter);
    }
}
