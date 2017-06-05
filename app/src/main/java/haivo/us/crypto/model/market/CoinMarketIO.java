package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class CoinMarketIO extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "CoinMarket.io";
    private static final String TTS_NAME = "Coin Market IO";
    private static final String URL = "https://coinmarket.io/ticker/%1$s%2$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.LEAF, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.USDE, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.DGB, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.KDC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.CON, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.NOBL, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.SMC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.VTC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.UTC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.KARM, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.RDD, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.RPD, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.ICN, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.PENG, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.MINT, new String[]{VirtualCurrency.BTC});
    }

    public CoinMarketIO() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.vol = jsonObject.getDouble("volume24");
        ticker.last = jsonObject.getDouble("last");
    }
}
