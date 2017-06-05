package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import haivo.us.crypto.util.ParseUtils;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class FoscEx extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Fosc-Ex";
    private static final String TTS_NAME = "Fosc Ex";
    private static final String URL = "http://www.fosc-ex.com/api-public-ticker";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.KNC, new String[]{Currency.KRW});
    }

    public FoscEx() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.vol = ParseUtils.getDoubleFromString(jsonObject, "volume");
        ticker.last = ParseUtils.getDoubleFromString(jsonObject, "last");
        ticker.timestamp = jsonObject.getLong("timestamp") * 1000;
    }

    protected String parseErrorFromJsonObject(int requestId, JSONObject jsonObject, CheckerInfo checkerInfo) throws
                                                                                                             Exception {
        return jsonObject.getString("error");
    }
}
