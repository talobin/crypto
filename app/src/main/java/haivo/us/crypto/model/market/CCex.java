package haivo.us.crypto.model.market;

import java.util.List;
import java.util.Locale;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;

public class CCex extends Market {
    public static final String NAME = "C-CEX";
    public static final String TTS_NAME = "C-Cex";
    public static final String URL = "https://c-cex.com/t/%1$s-%2$s.json";
    public static final String URL_CURRENCY_PAIRS = "https://c-cex.com/t/pairs.json";

    public CCex() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL,
                             new Object[] {
                                 checkerInfo.getCurrencyBaseLowerCase(),
                                 checkerInfo.getCurrencyCounterLowerCase()
                             });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject tickerObject = jsonObject.getJSONObject("ticker");
        ticker.bid = tickerObject.getDouble("buy");
        ticker.ask = tickerObject.getDouble("sell");
        ticker.high = tickerObject.getDouble("high");
        ticker.low = tickerObject.getDouble("low");
        ticker.last = tickerObject.getDouble("lastprice");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray pairsJsonArray = jsonObject.getJSONArray("pairs");
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            String pair = pairsJsonArray.getString(i);
            if (pair != null) {
                String[] currencies = pair.split("-", 2);
                if (!(currencies.length != 2 || currencies[0] == null || currencies[1] == null)) {
                    pairs.add(new CurrencyPairInfo(currencies[0].toUpperCase(Locale.US),
                                                   currencies[1].toUpperCase(Locale.US),
                                                   pair));
                }
            }
        }
    }
}
