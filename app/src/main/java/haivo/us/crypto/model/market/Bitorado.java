package haivo.us.crypto.model.market;

import java.util.List;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;

public class Bitorado extends Market {
    private static final String NAME = "Bitorado";
    private static final String TTS_NAME = "Bitorado";
    private static final String URL = "https://www.bitorado.com/api/market/%1$s-%2$s/ticker";
    private static final String URL_CURRENCY_PAIRS = "https://www.bitorado.com/api/ticker";

    public Bitorado() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject resultObject = jsonObject.getJSONObject("result");
        ticker.bid = resultObject.optDouble("buy", -1.0d);
        ticker.ask = resultObject.optDouble("sell", -1.0d);
        ticker.vol = resultObject.optDouble("vol", -1.0d);
        ticker.high = resultObject.optDouble("high", -1.0d);
        ticker.low = resultObject.optDouble("low", -1.0d);
        ticker.last = resultObject.optDouble("last", 0.0d);
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray pairNames = jsonObject.getJSONObject("result").getJSONObject("markets").names();
        for (int i = 0; i < pairNames.length(); i++) {
            String pairId = pairNames.getString(i);
            if (pairId != null) {
                String[] currencies = pairId.split("-");
                if (currencies.length == 2) {
                    pairs.add(new CurrencyPairInfo(currencies[0], currencies[1], pairId));
                }
            }
        }
    }
}
