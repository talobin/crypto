package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class Okcoin extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "OKCoin";
    private static final String TTS_NAME = "OK Coin";
    private static final String URL_CNY = "https://www.okcoin.cn/api/v1/ticker.do?symbol=%1$s_%2$s";
    private static final String URL_USD = "https://www.okcoin.com/api/v1/ticker.do?symbol=%1$s_%2$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.CNY, Currency.USD});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{Currency.CNY, Currency.USD});
        CURRENCY_PAIRS.put(VirtualCurrency.ETH, new String[]{Currency.CNY, Currency.USD});
    }

    public Okcoin() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        if (Currency.USD.equals(checkerInfo.getCurrencyCounter())) {
            return String.format(URL_USD, new Object[]{checkerInfo.getCurrencyBaseLowerCase(), checkerInfo.getCurrencyCounterLowerCase()});
        }
        return String.format(URL_CNY, new Object[]{checkerInfo.getCurrencyBaseLowerCase(), checkerInfo.getCurrencyCounterLowerCase()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject tickerJsonObject = jsonObject.getJSONObject("ticker");
        ticker.bid = tickerJsonObject.getDouble("buy");
        ticker.ask = tickerJsonObject.getDouble("sell");
        ticker.vol = tickerJsonObject.getDouble("vol");
        ticker.high = tickerJsonObject.getDouble("high");
        ticker.low = tickerJsonObject.getDouble("low");
        ticker.last = tickerJsonObject.getDouble("last");
    }
}
