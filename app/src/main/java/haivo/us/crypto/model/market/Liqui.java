package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class Liqui extends Market {
    private static final String NAME = "Liqui.io";
    private static final String TTS_NAME = "Liqui";
    private static final String URL = " https://api.liqui.io/api/3/ticker/%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://api.liqui.io/api/3/info";

    public Liqui() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        String pairId = checkerInfo.getCurrencyPairId();
        if (checkerInfo.getCurrencyPairId() == null) {
            pairId = String.format("%1$s_%2$s", new Object[]{checkerInfo.getCurrencyBaseLowerCase(), checkerInfo.getCurrencyCounterLowerCase()});
        }
        return String.format(URL, new Object[]{pairId});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject tickerJsonObject = jsonObject.getJSONObject(jsonObject.names().getString(0));
        ticker.bid = tickerJsonObject.getDouble("sell");
        ticker.ask = tickerJsonObject.getDouble("buy");
        ticker.vol = tickerJsonObject.getDouble("vol");
        ticker.high = tickerJsonObject.getDouble("high");
        ticker.low = tickerJsonObject.getDouble("low");
        ticker.last = tickerJsonObject.getDouble("last");
        ticker.timestamp = tickerJsonObject.getLong("updated");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairsNames = jsonObject.getJSONObject("pairs").names();
        for (int i = 0; i < pairsNames.length(); i++) {
            String pairId = pairsNames.getString(i);
            if (pairId != null) {
                String[] currencies = pairId.split("_");
                if (currencies.length == 2) {
                    pairs.add(new CurrencyPairInfo(currencies[0].toUpperCase(Locale.ENGLISH), currencies[1].toUpperCase(
                        Locale.ENGLISH), pairId));
                }
            }
        }
    }
}
