package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoinSwap extends Market {
    private static final String NAME = "Coin-Swap";
    private static final String TTS_NAME = "Coin Swap";
    private static final String URL = "https://api.coin-swap.net/market/stats/%1$s/%2$s";
    private static final String URL_CURRENCY_PAIRS = "http://api.coin-swap.net/market/summary";

    public CoinSwap() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = jsonObject.getDouble("bid");
        ticker.ask = jsonObject.getDouble("ask");
        ticker.vol = jsonObject.getDouble("dayvolume");
        ticker.high = jsonObject.getDouble("dayhigh");
        ticker.low = jsonObject.getDouble("daylow");
        ticker.last = jsonObject.getDouble("lastprice");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs) throws
                                                                                                          Exception {
        JSONArray marketsJsonArray = new JSONArray(responseString);
        for (int i = 0; i < marketsJsonArray.length(); i++) {
            JSONObject marketJsonObject = marketsJsonArray.getJSONObject(i);
            pairs.add(new CurrencyPairInfo(marketJsonObject.getString("symbol"), marketJsonObject.getString("exchange"), null));
        }
    }
}
