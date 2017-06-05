package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Cryptopia extends Market {
    private static final String NAME = "Cryptopia";
    private static final String TTS_NAME = "Cryptopia";
    private static final String URL = "https://www.cryptopia.co.nz/api/GetMarket/%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://www.cryptopia.co.nz/api/GetTradePairs";

    public Cryptopia() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyPairId()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject dataJsonObject = jsonObject.getJSONObject("Data");
        ticker.bid = dataJsonObject.getDouble("BidPrice");
        ticker.ask = dataJsonObject.getDouble("AskPrice");
        ticker.vol = dataJsonObject.getDouble("Volume");
        ticker.high = dataJsonObject.getDouble("High");
        ticker.low = dataJsonObject.getDouble("Low");
        ticker.last = dataJsonObject.getDouble("LastPrice");
    }

    protected String parseErrorFromJsonObject(int requestId, JSONObject jsonObject, CheckerInfo checkerInfo) throws
                                                                                                             Exception {
        return jsonObject.getString("Message");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray dataJsonArray = jsonObject.getJSONArray("Data");
        for (int i = 0; i < dataJsonArray.length(); i++) {
            JSONObject pairJsonObject = dataJsonArray.getJSONObject(i);
            String currencyBase = pairJsonObject.getString("Symbol");
            String currencyCounter = pairJsonObject.getString("BaseSymbol");
            String pairId = pairJsonObject.getString("Id");
            if (!(currencyCounter == null || currencyCounter == null || pairId == null)) {
                pairs.add(new CurrencyPairInfo(currencyBase, currencyCounter, pairId));
            }
        }
    }
}
