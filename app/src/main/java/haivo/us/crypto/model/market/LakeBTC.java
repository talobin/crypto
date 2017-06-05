package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class LakeBTC extends Market {
    private static final String NAME = "LakeBTC";
    private static final String TTS_NAME = "Lake BTC";
    private static final String URL = "https://api.lakebtc.com/api_v2/ticker";
    private static final String URL_CURRENCY_PAIRS = "https://api.lakebtc.com/api_v2/ticker";

    public LakeBTC() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        String pairId;
        if (checkerInfo.getCurrencyPairId() == null) {
            pairId = checkerInfo.getCurrencyBaseLowerCase() + checkerInfo.getCurrencyCounterLowerCase();
        } else {
            pairId = checkerInfo.getCurrencyPairId();
        }
        JSONObject pairJsonObject = jsonObject.getJSONObject(pairId);
        ticker.bid = pairJsonObject.getDouble("bid");
        ticker.ask = pairJsonObject.getDouble("ask");
        ticker.vol = pairJsonObject.getDouble("volume");
        ticker.high = pairJsonObject.getDouble("high");
        ticker.low = pairJsonObject.getDouble("low");
        ticker.last = pairJsonObject.getDouble("last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairsJsonArray = jsonObject.names();
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            String pairId = pairsJsonArray.getString(i);
            pairs.add(new CurrencyPairInfo(pairId.substring(0, 3).toUpperCase(Locale.ENGLISH), pairId.substring(3).toUpperCase(
                Locale.ENGLISH), pairId));
        }
    }
}
