package haivo.us.crypto.model.market;

import java.util.List;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;

public class BitTrex extends Market {
    private static final String NAME = "BitTrex";
    private static final String TTS_NAME = "Bit Trex";
    private static final String URL = "https://bittrex.com/api/v1.1/public/getticker?market=%1$s-%2$s";
    private static final String URL_CURRENCY_PAIRS = "https://bittrex.com/api/v1.1/public/getmarkets";

    public BitTrex() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyCounter(), checkerInfo.getCurrencyBase() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject resultJsonObject = jsonObject.getJSONObject("result");
        ticker.bid = resultJsonObject.getDouble("Bid");
        ticker.ask = resultJsonObject.getDouble("Ask");
        ticker.last = resultJsonObject.getDouble("Last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray resultJsonArray = jsonObject.getJSONArray("result");
        for (int i = 0; i < resultJsonArray.length(); i++) {
            JSONObject marketJsonObject = resultJsonArray.getJSONObject(i);
            pairs.add(new CurrencyPairInfo(marketJsonObject.getString("MarketCurrency"),
                                           marketJsonObject.getString("BaseCurrency"),
                                           marketJsonObject.getString("MarketName")));
        }
    }
}
