package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONObject;

public class BitMarket24PL extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "BitMarket24.pl";
    private static final String TTS_NAME = "Bit Market 24";
    private static final String URL = "https://bitmarket24.pl/api/%1$s_%2$s/status.json";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.PLN });
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[] { VirtualCurrency.BTC, Currency.PLN });
    }

    public BitMarket24PL() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        if (!jsonObject.isNull("bid")) {
            ticker.bid = jsonObject.getDouble("bid");
        }
        if (!jsonObject.isNull("ask")) {
            ticker.ask = jsonObject.getDouble("ask");
        }
        if (!jsonObject.isNull("volume")) {
            ticker.vol = jsonObject.getDouble("volume");
        }
        if (!jsonObject.isNull("high")) {
            ticker.high = jsonObject.getDouble("high");
        }
        if (!jsonObject.isNull("low")) {
            ticker.low = jsonObject.getDouble("low");
        }
        ticker.last = jsonObject.getDouble("last");
    }
}
