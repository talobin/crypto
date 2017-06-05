package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONObject;

public class Campbx extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "CampBX";
    private static final String TTS_NAME = "Camp BX";
    private static final String URL = "http://campbx.com/api/xticker.php";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.USD });
    }

    public Campbx() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        ticker.bid = jsonObject.getDouble("Best Bid");
        ticker.ask = jsonObject.getDouble("Best Ask");
        ticker.last = jsonObject.getDouble("Last Trade");
    }
}
