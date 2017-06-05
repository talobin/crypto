package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONObject;

public class BtcMarkets extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "BtcMarkets.net";
    private static final String TTS_NAME = "BTC Markets net";
    private static final String URL = "https://api.btcmarkets.net/market/%1$s/%2$s/tick";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.AUD });
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[] { VirtualCurrency.BTC, Currency.AUD });
        CURRENCY_PAIRS.put(VirtualCurrency.ETH, new String[] { VirtualCurrency.BTC, Currency.AUD });
    }

    public BtcMarkets() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        ticker.bid = jsonObject.getDouble("bestBid");
        ticker.ask = jsonObject.getDouble("bestAsk");
        ticker.last = jsonObject.getDouble("lastPrice");
        ticker.timestamp = jsonObject.getLong("timestamp");
    }
}
