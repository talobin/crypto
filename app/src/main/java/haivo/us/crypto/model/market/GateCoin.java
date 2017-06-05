package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class GateCoin extends Market {
    private static final String NAME = "GateCoin";
    private static final String TTS_NAME = "Gate Coin";
    private static final String URL = "https://api.gatecoin.com/Public/LiveTickers";

    public GateCoin() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONArray pairNames = jsonObject.getJSONArray("tickers");
        String userCurrencyPairChoice = checkerInfo.getCurrencyBase().concat(checkerInfo.getCurrencyCounter());
        for (int i = 0; i < pairNames.length(); i++) {
            JSONObject tickerDetails = pairNames.getJSONObject(i);
            if (tickerDetails.getString("currencyPair").equals(userCurrencyPairChoice)) {
                ticker.bid = tickerDetails.getDouble("bid");
                ticker.ask = tickerDetails.getDouble("ask");
                ticker.vol = tickerDetails.getDouble("volume");
                ticker.high = tickerDetails.getDouble("high");
                ticker.low = tickerDetails.getDouble("low");
                ticker.last = tickerDetails.getDouble("last");
                ticker.timestamp = tickerDetails.getLong("createDateTime");
                return;
            }
        }
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairNames = jsonObject.getJSONArray("tickers");
        for (int i = 0; i < pairNames.length(); i++) {
            String pairId = pairNames.getJSONObject(i).getString("currencyPair");
            if (pairId != null) {
                String[] currencies = new String[]{pairId.substring(0, 3), pairId.substring(3, 6)};
                if (currencies.length == 2) {
                    pairs.add(new CurrencyPairInfo(currencies[0], currencies[1], pairId));
                }
            }
        }
    }
}
