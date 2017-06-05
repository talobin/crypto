package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class FybSG extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "FYB-SG";
    private static final String TTS_NAME = "Fyb SG";
    private static final String URL = "https://www.fybsg.com/api/%1$s/ticker.json";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.SGD});
    }

    public FybSG() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyCounter()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = jsonObject.getDouble("bid");
        ticker.ask = jsonObject.getDouble("ask");
        ticker.last = ticker.ask;
    }
}
