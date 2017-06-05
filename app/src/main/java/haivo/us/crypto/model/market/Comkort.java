package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Comkort extends Market {
    private static final String NAME = "Comkort";
    private static final String TTS_NAME = "Comkort";
    private static final String URL = "https://api.comkort.com/v1/public/market/summary?market_alias=%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://api.comkort.com/v1/public/market/list";

    public Comkort() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyPairId()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject marketsJsonObject = jsonObject.getJSONObject("markets");
        JSONObject marketJsonObject = marketsJsonObject.getJSONObject(marketsJsonObject.names().getString(0));
        ticker.bid = getFirstOrderFrom(marketJsonObject, "buy_orders");
        ticker.ask = getFirstOrderFrom(marketJsonObject, "sell_orders");
        ticker.vol = marketJsonObject.getDouble("volume");
        ticker.high = marketJsonObject.getDouble("high");
        ticker.low = marketJsonObject.getDouble("low");
        ticker.last = marketJsonObject.getDouble("last_price");
    }

    private double getFirstOrderFrom(JSONObject marketJsonObject, String arrayName) throws Exception {
        JSONArray ordersJsonArray = marketJsonObject.getJSONArray(arrayName);
        if (ordersJsonArray.length() > 0) {
            return ordersJsonArray.getJSONObject(0).getDouble("price");
        }
        return -1.0d;
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray marketsJsonArray = jsonObject.getJSONArray("markets");
        for (int i = 0; i < marketsJsonArray.length(); i++) {
            JSONObject marketJsonObject = marketsJsonArray.getJSONObject(i);
            pairs.add(new CurrencyPairInfo(marketJsonObject.getString("item"), marketJsonObject.getString("price_currency"), marketJsonObject.getString("alias")));
        }
    }
}
