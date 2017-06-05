package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class TheRock extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "TheRock";
    private static final String TTS_NAME = "The Rock";
    private static final String URL = "https://www.therocktrading.com/api/ticker/%1$s%2$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.EUR, Currency.USD, VirtualCurrency.XRP});
        CURRENCY_PAIRS.put(Currency.EUR, new String[]{VirtualCurrency.DOGE, VirtualCurrency.XRP});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{VirtualCurrency.BTC, Currency.EUR, Currency.USD});
        CURRENCY_PAIRS.put(VirtualCurrency.NMC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.PPC, new String[]{Currency.EUR});
        CURRENCY_PAIRS.put(Currency.USD, new String[]{VirtualCurrency.XRP});
    }

    public TheRock() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{fixCurrency(checkerInfo.getCurrencyBase()), fixCurrency(checkerInfo.getCurrencyCounter())});
    }

    private String fixCurrency(String currency) {
        if (VirtualCurrency.DOGE.equals(currency)) {
            return VirtualCurrency.DOG;
        }
        return currency;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject tickerObject = jsonObject.getJSONArray("result").getJSONObject(0);
        ticker.bid = tickerObject.getDouble("bid");
        ticker.ask = tickerObject.getDouble("ask");
        ticker.vol = tickerObject.getDouble("volume");
        ticker.high = tickerObject.getDouble("high");
        ticker.low = tickerObject.getDouble("low");
        ticker.last = tickerObject.getDouble("last");
    }
}
