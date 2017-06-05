package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.util.ParseUtils;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Uphold extends Market {
    private static final String NAME = "Uphold";
    private static final String TTS_NAME = "Uphold";
    private static final String URL = "https://api.uphold.com/v0/ticker/%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://api.uphold.com/v0/ticker";

    public Uphold() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyPairId()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = ParseUtils.getDoubleFromString(jsonObject, "bid");
        ticker.ask = ParseUtils.getDoubleFromString(jsonObject, "ask");
        ticker.last = (ticker.bid + ticker.ask) / 2.0d;
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs) throws
                                                                                                          Exception {
        JSONArray pairsJsonArray = new JSONArray(responseString);
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            JSONObject pairJsonObject = pairsJsonArray.getJSONObject(i);
            String pairId = pairJsonObject.getString("pair");
            String currencyCounter = pairJsonObject.getString("currency");
            if (!(currencyCounter == null || pairId == null || !pairId.endsWith(currencyCounter))) {
                String currencyBase = pairId.substring(0, pairId.length() - currencyCounter.length());
                if (!currencyCounter.equals(currencyBase)) {
                    pairs.add(new CurrencyPairInfo(currencyBase, currencyCounter, pairId));
                    pairs.add(new CurrencyPairInfo(currencyCounter, currencyBase, currencyCounter + currencyBase));
                }
            }
        }
    }
}
