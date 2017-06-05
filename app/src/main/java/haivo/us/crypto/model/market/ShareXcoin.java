package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ShareXcoin extends Market {
    private static final String NAME = "ShareXcoin";
    private static final String TTS_NAME = "Share X coin";
    private static final String URL = "https://sharexcoin.com/public_api/v1/market/%1$s_%2$s/summary";
    private static final String URL_CURRENCY_PAIRS = "https://sharexcoin.com/public_api/v1/market/summary";

    public ShareXcoin() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = jsonObject.getDouble("bid");
        ticker.ask = jsonObject.getDouble("ask");
        ticker.vol = jsonObject.getDouble("volume");
        ticker.high = jsonObject.getDouble("high");
        ticker.low = jsonObject.getDouble("low");
        ticker.last = jsonObject.getDouble("last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray marketsJsonArray = jsonObject.getJSONArray("markets");
        for (int i = 0; i < marketsJsonArray.length(); i++) {
            JSONObject marketJsonObject = marketsJsonArray.getJSONObject(i);
            pairs.add(new CurrencyPairInfo(marketJsonObject.getString("coin1"), marketJsonObject.getString("coin2"), marketJsonObject.getString("market_id")));
        }
    }
}
