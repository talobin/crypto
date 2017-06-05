package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Poloniex extends Market {
    private static final String NAME = "Poloniex";
    private static final String TTS_NAME = "Poloniex";
    private static final String URL = "https://poloniex.com/public?command=returnTicker";

    public Poloniex() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject pairJsonObject = jsonObject.getJSONObject(checkerInfo.getCurrencyCounter() + "_" + checkerInfo.getCurrencyBase());
        ticker.bid = pairJsonObject.getDouble("highestBid");
        ticker.ask = pairJsonObject.getDouble("lowestAsk");
        ticker.vol = pairJsonObject.getDouble("baseVolume");
        ticker.last = pairJsonObject.getDouble("last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairNames = jsonObject.names();
        for (int i = 0; i < pairNames.length(); i++) {
            String pairId = pairNames.getString(i);
            if (pairId != null) {
                String[] currencies = pairId.split("_");
                if (currencies.length == 2) {
                    pairs.add(new CurrencyPairInfo(currencies[1], currencies[0], pairId));
                }
            }
        }
    }
}
