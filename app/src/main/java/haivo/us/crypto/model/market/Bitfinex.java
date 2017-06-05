package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONObject;
import haivo.us.crypto.util.ParseUtils;

public class Bitfinex extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Bitfinex";
    private static final String TTS_NAME = "Bitfinex";
    private static final String URL = "https://api.bitfinex.com/v1/pubticker/%1$s%2$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BFX, new String[] { VirtualCurrency.BTC, Currency.USD });
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.USD });
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[] { Currency.USD, VirtualCurrency.BTC });
        CURRENCY_PAIRS.put(VirtualCurrency.XMR, new String[] { VirtualCurrency.BTC, Currency.USD });
        CURRENCY_PAIRS.put(VirtualCurrency.ETC, new String[] { VirtualCurrency.BTC, Currency.USD });
        CURRENCY_PAIRS.put(VirtualCurrency.ETH, new String[] { VirtualCurrency.BTC, Currency.USD });
        CURRENCY_PAIRS.put(VirtualCurrency.RRT, new String[] { VirtualCurrency.BTC, Currency.USD });
    }

    public Bitfinex() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL,
                             new Object[] {
                                 checkerInfo.getCurrencyBaseLowerCase(),
                                 checkerInfo.getCurrencyCounterLowerCase()
                             });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        ticker.bid = ParseUtils.getDoubleFromString(jsonObject, "bid");
        ticker.ask = ParseUtils.getDoubleFromString(jsonObject, "ask");
        ticker.vol = ParseUtils.getDoubleFromString(jsonObject, "volume");
        ticker.high = ParseUtils.getDoubleFromString(jsonObject, "high");
        ticker.low = ParseUtils.getDoubleFromString(jsonObject, "low");
        ticker.last = ParseUtils.getDoubleFromString(jsonObject, "last_price");
        ticker.timestamp = (long) (jsonObject.getDouble("timestamp") * 1000.0d);
    }
}
