package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class Paymium extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Paymium";
    private static final String TTS_NAME = "Paymium";
    private static final String URL = "https://paymium.com/api/v1/data/eur/ticker";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.EUR});
    }

    public Paymium() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = jsonObject.getDouble("bid");
        ticker.ask = jsonObject.getDouble("ask");
        ticker.vol = jsonObject.getDouble("volume");
        ticker.high = jsonObject.getDouble("high");
        ticker.low = jsonObject.getDouble("low");
        ticker.last = jsonObject.getDouble("price");
    }

    protected String parseErrorFromJsonObject(int requestId, JSONObject jsonObject, CheckerInfo checkerInfo) throws
                                                                                                             Exception {
        StringBuilder stringBuilder = new StringBuilder();
        JSONArray errorsArray = jsonObject.getJSONArray("errors");
        for (int i = 0; i < errorsArray.length(); i++) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(errorsArray.getString(i));
        }
        return stringBuilder.toString();
    }
}
