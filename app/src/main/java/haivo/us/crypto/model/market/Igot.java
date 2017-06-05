package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class Igot extends Market {
    private static final String NAME = "igot";
    private static final String TTS_NAME = "igot";
    private static final String URL = "https://www.igot.com/api/v1/stats/buy";
    private static final String URL_CURRENCY_PAIRS = "https://www.igot.com/api/v1/stats/buy";

    public Igot() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject pairJsonObject = jsonObject.getJSONObject(checkerInfo.getCurrencyPairId());
        ticker.high = pairJsonObject.getDouble("highest_today");
        ticker.low = pairJsonObject.getDouble("lowest_today");
        ticker.last = pairJsonObject.getDouble("current_rate");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairsJsonArray = jsonObject.names();
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            String currencyCounter = pairsJsonArray.getString(i);
            if (currencyCounter != null) {
                pairs.add(new CurrencyPairInfo(VirtualCurrency.BTC, currencyCounter.toUpperCase(Locale.ENGLISH), currencyCounter));
            }
        }
    }
}
