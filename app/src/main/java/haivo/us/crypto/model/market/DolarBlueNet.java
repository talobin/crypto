package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class DolarBlueNet extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "DolarBlue.net";
    private static final String TTS_NAME = "Dolar Blue";
    private static final String URL = "http://dolar.bitplanet.info/api.php";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(Currency.USD, new String[]{Currency.ARS});
    }

    public DolarBlueNet() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.ask = jsonObject.getDouble("venta");
        ticker.bid = jsonObject.getDouble("compra");
        ticker.last = ticker.ask;
    }
}
