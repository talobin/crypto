package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Bleutrade extends Market {
    private static final String NAME = "Bleutrade";
    private static final String TTS_NAME = "Bleutrade";
    private static final String URL = "https://bleutrade.com/api/v2/public/getticker?market=%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://bleutrade.com/api/v2/public/getmarkets";

    public Bleutrade() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyPairId() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject resultsJsonObject;
        JSONObject resultObject = (JSONObject) jsonObject.get("result");
        resultsJsonObject = resultObject;
        ticker.bid = resultsJsonObject.getDouble("Bid");
        ticker.ask = resultsJsonObject.getDouble("Ask");
        ticker.last = resultsJsonObject.getDouble("Last");
    }

    protected String parseErrorFromJsonObject(int requestId, JSONObject jsonObject, CheckerInfo checkerInfo)
        throws Exception {
        return jsonObject.getString("message");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray resultsJsonArray = jsonObject.getJSONArray("result");
        for (int i = 0; i < resultsJsonArray.length(); i++) {
            JSONObject pairJsonObject = resultsJsonArray.getJSONObject(i);
            String pairId = pairJsonObject.getString("MarketName");
            String currencyBase = pairJsonObject.getString("MarketCurrency");
            String currencyCounter = pairJsonObject.getString("BaseCurrency");
            if (!(pairId == null || currencyBase == null || currencyCounter == null)) {
                pairs.add(new CurrencyPairInfo(currencyBase, currencyCounter, pairId));
            }
        }
    }
}
