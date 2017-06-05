package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONObject;

public class Bitcurex extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Bitcurex";
    private static final String TTS_NAME = "Bitcurex";
    private static final String URL = "https://bitcurex.com/api/%1$s/ticker.json";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.PLN, Currency.EUR });
    }

    public Bitcurex() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyCounterLowerCase() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        ticker.bid = parsePrice(jsonObject.getDouble("best_ask"));
        ticker.ask = parsePrice(jsonObject.getDouble("best_bid"));
        ticker.vol = parseBTC(jsonObject.getDouble("total_volume"));
        ticker.high = parsePrice(jsonObject.getDouble("highest_tx_price"));
        ticker.low = parsePrice(jsonObject.getDouble("lowest_tx_price"));
        ticker.last = parsePrice(jsonObject.getDouble("last_tx_price"));
    }

    private double parsePrice(double price) {
        return price / 10000.0d;
    }

    private double parseBTC(double satoshi) {
        return satoshi / 1.0E8d;
    }
}
