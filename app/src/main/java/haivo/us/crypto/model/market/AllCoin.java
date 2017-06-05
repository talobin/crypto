package haivo.us.crypto.model.market;

import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.R;
import java.util.List;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;
import haivo.us.crypto.util.ParseUtils;

public class AllCoin extends Market {
    private static final String NAME = "AllCoin";
    private static final String TTS_NAME = "All Coin";
    private static final String URL = "https://www.allcoin.com/api2/pair/%1$s_%2$s";
    private static final String URL_CURRENCY_PAIRS = "https://www.allcoin.com/api2/pairs";

    public AllCoin() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
        ticker.bid = ParseUtils.getDoubleFromString(dataJsonObject, "top_bid");
        ticker.ask = ParseUtils.getDoubleFromString(dataJsonObject, "top_ask");
        ticker.vol = ParseUtils.getDoubleFromString(dataJsonObject, "volume_24h_" + checkerInfo.getCurrencyBase());
        ticker.high = ParseUtils.getDoubleFromString(dataJsonObject, "max_24h_price");
        ticker.low = ParseUtils.getDoubleFromString(dataJsonObject, "min_24h_price");
        ticker.last = ParseUtils.getDoubleFromString(dataJsonObject, "trade_price");
    }

    protected String parseErrorFromJsonObject(int requestId, JSONObject jsonObject, CheckerInfo checkerInfo)
        throws Exception {
        return jsonObject.getString("error_info");
    }

    public int getCautionResId() {
        return R.string.market_caution_allcoin;
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
        JSONArray pairsJsonArray = dataJsonObject.names();
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            String pairName = pairsJsonArray.getString(i);
            JSONObject marketJsonObject = dataJsonObject.getJSONObject(pairName);
            pairs.add(new CurrencyPairInfo(marketJsonObject.getString(MaindbContract.AlarmColumns.TYPE),
                                           marketJsonObject.getString("exchange"),
                                           pairName));
        }
    }
}
