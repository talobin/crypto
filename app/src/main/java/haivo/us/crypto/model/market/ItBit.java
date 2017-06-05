package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class ItBit extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "itBit";
    private static final String TTS_NAME = "It Bit";
    private static final String URL = "https://api.itbit.com/v1/markets/%1$s%2$s/ticker";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.USD, Currency.SGD, Currency.EUR});
    }

    public ItBit() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    private String fixCurrency(String currency) {
        if (VirtualCurrency.BTC.equals(currency)) {
            return VirtualCurrency.XBT;
        }
        return currency;
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{fixCurrency(checkerInfo.getCurrencyBase()), checkerInfo.getCurrencyCounter()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = jsonObject.getDouble("bid");
        ticker.ask = jsonObject.getDouble("ask");
        ticker.vol = jsonObject.getDouble("volume24h");
        ticker.high = jsonObject.getDouble("high24h");
        ticker.low = jsonObject.getDouble("low24h");
        ticker.last = jsonObject.getDouble("lastPrice");
    }

    protected String parseErrorFromJsonObject(int requestId, JSONObject jsonObject, CheckerInfo checkerInfo) throws
                                                                                                             Exception {
        return jsonObject.getString("message");
    }
}
