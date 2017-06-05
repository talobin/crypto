package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class Fxbtc extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "FxBtc";
    private static final String TTS_NAME = "FxBtc";
    private static final String URL = "https://www.fxbtc.com/jport?op=query&type=ticker&symbol=%1$s_%2$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.CNY});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{Currency.CNY, VirtualCurrency.BTC});
    }

    public Fxbtc() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBaseLowerCase(), checkerInfo.getCurrencyCounterLowerCase()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject tickerObject = jsonObject.getJSONObject("ticker");
        ticker.bid = tickerObject.getDouble("bid");
        ticker.ask = tickerObject.getDouble("ask");
        ticker.vol = tickerObject.getDouble("vol");
        ticker.high = tickerObject.getDouble("high");
        ticker.low = tickerObject.getDouble("low");
        ticker.last = tickerObject.getDouble("last_rate");
    }
}
