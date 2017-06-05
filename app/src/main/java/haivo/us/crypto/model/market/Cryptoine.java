package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class Cryptoine extends Market {
    private static final String NAME = "Cryptoine";
    private static final String TTS_NAME = "Cryptoine";
    private static final String URL = "https://cryptoine.com/api/1/ticker/%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://cryptoine.com/api/1/markets";

    public Cryptoine() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyPairId()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = getDoubleOrZero(jsonObject, "buy");
        ticker.ask = getDoubleOrZero(jsonObject, "sell");
        ticker.vol = getDoubleOrZero(jsonObject, "vol_exchange");
        ticker.high = getDoubleOrZero(jsonObject, "high");
        ticker.low = getDoubleOrZero(jsonObject, "low");
        ticker.last = getDoubleOrZero(jsonObject, "last");
    }

    private double getDoubleOrZero(JSONObject jsonObject, String name) throws Exception {
        if (jsonObject.isNull(name)) {
            return 0.0d;
        }
        return jsonObject.getDouble(name);
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairNames = jsonObject.getJSONObject("data").names();
        for (int i = 0; i < pairNames.length(); i++) {
            String pairId = pairNames.getString(i);
            if (pairId != null) {
                String[] currencies = pairId.split("_");
                if (currencies.length == 2) {
                    pairs.add(new CurrencyPairInfo(currencies[0].toUpperCase(Locale.ENGLISH), currencies[1].toUpperCase(
                        Locale.ENGLISH), pairId));
                }
            }
        }
    }
}
