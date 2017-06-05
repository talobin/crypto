package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class MintPal extends Market {
    private static final String NAME = "MintPal";
    private static final String TTS_NAME = "Mint Pal";
    private static final String URL = "https://api.mintpal.com/market/stats/%1$s/%2$s/";
    private static final String URL_CURRENCY_PAIRS = "https://api.mintpal.com/market/summary/";

    public MintPal() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter()});
    }

    protected void parseTicker(int requestId, String responseString, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                             Exception {
        parseTickerFromJsonObject(requestId, new JSONArray(responseString).getJSONObject(0), ticker, checkerInfo);
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.vol = jsonObject.getDouble("24hvol");
        ticker.high = jsonObject.getDouble("24hhigh");
        ticker.low = jsonObject.getDouble("24hlow");
        ticker.last = jsonObject.getDouble("last_price");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs) throws
                                                                                                          Exception {
        JSONArray jsonArray = new JSONArray(responseString);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject marketObject = jsonArray.getJSONObject(i);
            pairs.add(new CurrencyPairInfo(marketObject.getString("code"), marketObject.getString("exchange"), null));
        }
    }
}
