package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Quoine extends Market {
    private static final String NAME = "Quoine";
    private static final String TTS_NAME = "Quoine";
    private static final String URL = "https://api.quoine.com/products/code/CASH/%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://api.quoine.com/products/";

    public Quoine() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyPairId()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = jsonObject.getDouble("market_bid");
        ticker.ask = jsonObject.getDouble("market_ask");
        ticker.vol = jsonObject.getDouble("volume_24h");
        ticker.high = jsonObject.getDouble("high_market_ask");
        ticker.low = jsonObject.getDouble("low_market_bid");
        ticker.last = jsonObject.getDouble("last_traded_price");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs) throws
                                                                                                          Exception {
        JSONArray pairsJsonArray = new JSONArray(responseString);
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            JSONObject pairJsonObject = pairsJsonArray.getJSONObject(i);
            if (VirtualCurrency.CASH.equals(pairJsonObject.getString("code"))) {
                String currencyCounter = pairJsonObject.getString("currency");
                String pairName = pairJsonObject.getString("currency_pair_code");
                if (!(pairName == null || currencyCounter == null || !pairName.endsWith(currencyCounter))) {
                    pairs.add(new CurrencyPairInfo(pairName.substring(0, pairName.length() - currencyCounter.length()), currencyCounter, pairName));
                }
            }
        }
    }
}
