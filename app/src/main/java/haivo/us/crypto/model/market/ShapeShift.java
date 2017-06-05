package haivo.us.crypto.model.market;

import java.util.ArrayList;
import java.util.List;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;
import haivo.us.crypto.util.ParseUtils;

public class ShapeShift extends Market {
    private static final String NAME = "ShapeShift";
    private static final String TTS_NAME = "Shape Shift";
    private static final String URL = "https://shapeshift.io/rate/%1$s_%2$s";
    private static final String URL_CURRENCY_PAIRS = "https://shapeshift.io/getcoins";

    public ShapeShift() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        ticker.last = ParseUtils.getDoubleFromString(jsonObject, "rate");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        int i;
        JSONArray jsonCoinNames = jsonObject.names();
        List<String> availableCoinNames = new ArrayList(jsonCoinNames.length());
        for (i = 0; i < jsonCoinNames.length(); i++) {
            JSONObject coinJsonObject = jsonObject.getJSONObject(jsonCoinNames.getString(i));
            if ("available".equals(coinJsonObject.optString("status"))) {
                availableCoinNames.add(coinJsonObject.getString("symbol"));
            }
        }
        int coinesCount = availableCoinNames.size();
        for (i = 0; i < coinesCount; i++) {
            for (int j = 0; j < coinesCount; j++) {
                if (i != j) {
                    pairs.add(new CurrencyPairInfo((String) availableCoinNames.get(i),
                                                   (String) availableCoinNames.get(j),
                                                   null));
                }
            }
        }
    }
}
