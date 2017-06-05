package haivo.us.crypto.model.market;

import java.util.List;
import java.util.Locale;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;

public class Bter extends Market {
    private static final String NAME = "Bter";
    private static final String TTS_NAME = "B ter";
    private static final String URL = "https://data.bter.com/api/1/ticker/%1$s_%2$s";
    private static final String URL_CURRENCY_PAIRS = "https://data.bter.com/api/1/pairs";

    public Bter() {
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
        ticker.bid = jsonObject.getDouble("buy");
        ticker.ask = jsonObject.getDouble("sell");
        ticker.vol = jsonObject.getDouble("vol_" + checkerInfo.getCurrencyBaseLowerCase());
        ticker.high = jsonObject.getDouble("high");
        ticker.low = jsonObject.getDouble("low");
        ticker.last = jsonObject.getDouble("last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray jsonArray = new JSONArray(responseString);
        for (int i = 0; i < jsonArray.length(); i++) {
            String pairId = jsonArray.getString(i);
            if (pairId != null) {
                String[] currencies = pairId.split("_");
                if (currencies.length == 2) {
                    pairs.add(new CurrencyPairInfo(currencies[0].toUpperCase(Locale.ENGLISH),
                                                   currencies[1].toUpperCase(Locale.ENGLISH),
                                                   pairId));
                }
            }
        }
    }
}
