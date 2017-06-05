package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.util.ParseUtils;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class HitBtc extends Market {
    private static final String NAME = "HitBTC";
    private static final String TTS_NAME = "Hit BTC";
    private static final String URL = "https://api.hitbtc.com/api/1/public/%1$s/ticker";
    private static final String URL_CURRENCY_PAIRS = "https://api.hitbtc.com/api/1/public/symbols";

    public HitBtc() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyPairId()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = ParseUtils.getDoubleFromString(jsonObject, "bid");
        ticker.ask = ParseUtils.getDoubleFromString(jsonObject, "ask");
        ticker.vol = ParseUtils.getDoubleFromString(jsonObject, "volume");
        ticker.high = ParseUtils.getDoubleFromString(jsonObject, "high");
        ticker.low = ParseUtils.getDoubleFromString(jsonObject, "low");
        ticker.last = ParseUtils.getDoubleFromString(jsonObject, "last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray symbolsJsonArray = jsonObject.getJSONArray("symbols");
        for (int i = 0; i < symbolsJsonArray.length(); i++) {
            JSONObject pairJsonObject = symbolsJsonArray.getJSONObject(i);
            String currencyBase = pairJsonObject.getString("commodity");
            String currencyCounter = pairJsonObject.getString("currency");
            String currencyPairId = pairJsonObject.getString("symbol");
            if (!(currencyBase == null || currencyCounter == null || currencyPairId == null)) {
                pairs.add(new CurrencyPairInfo(currencyBase, currencyCounter, currencyPairId));
            }
        }
    }
}
