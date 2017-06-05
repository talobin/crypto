package haivo.us.crypto.model.market;

import java.util.List;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;

public class Basebit extends Market {
    private static final String NAME = "Basebit";
    private static final String TTS_NAME = "Base Bit";
    private static final String URL = "http://www.basebit.com.br/quote-%1$s";
    private static final String URL_CURRENCY_PAIRS = "http://www.basebit.com.br/listpairs";

    public Basebit() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyPairId() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject resultJsonObject = jsonObject.getJSONObject("result");
        ticker.vol = resultJsonObject.getDouble("volume24h");
        ticker.high = resultJsonObject.getDouble("high");
        ticker.low = resultJsonObject.getDouble("low");
        ticker.last = resultJsonObject.getDouble("last");
    }

    protected String parseErrorFromJsonObject(int requestId, JSONObject jsonObject, CheckerInfo checkerInfo)
        throws Exception {
        return jsonObject.getString("errorMessage");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray pairsJsonArray = new JSONArray(responseString);
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            String pairName = pairsJsonArray.getString(i);
            String[] currencyNames = pairName.split("_");
            if (currencyNames != null && currencyNames.length >= 2) {
                pairs.add(new CurrencyPairInfo(currencyNames[0], currencyNames[1], pairName));
            }
        }
    }
}
