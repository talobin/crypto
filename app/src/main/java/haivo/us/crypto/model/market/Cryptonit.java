package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.util.ParseUtils;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class Cryptonit extends Market {
    private static final String NAME = "Cryptonit";
    private static final String TTS_NAME = "Cryptonit";
    private static final String URL = "https://cryptonit.net/apiv2/rest/public/ccorder.json?bid_currency=%1$s&ask_currency=%2$s&ticker";
    private static final String URL_CURRENCY_PAIRS = "https://cryptonit.net/apiv2/rest/public/pairs.json";

    public Cryptonit() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyCounterLowerCase(), checkerInfo.getCurrencyBaseLowerCase()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject rateJsonObject = jsonObject.getJSONObject("rate");
        JSONObject volumeJsonObject = jsonObject.getJSONObject("volume");
        ticker.bid = ParseUtils.getDoubleFromString(rateJsonObject, "bid");
        ticker.ask = ParseUtils.getDoubleFromString(rateJsonObject, "ask");
        if (volumeJsonObject.has(checkerInfo.getCurrencyBase())) {
            ticker.vol = ParseUtils.getDoubleFromString(volumeJsonObject, checkerInfo.getCurrencyBase());
        }
        ticker.high = ParseUtils.getDoubleFromString(rateJsonObject, "high");
        ticker.low = ParseUtils.getDoubleFromString(rateJsonObject, "low");
        ticker.last = ParseUtils.getDoubleFromString(rateJsonObject, "last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs) throws
                                                                                                          Exception {
        JSONArray pairsJsonArray = new JSONArray(responseString);
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            JSONArray currenciesJsonArray = pairsJsonArray.getJSONArray(i);
            if (currenciesJsonArray.length() == 2) {
                String currencyBase = currenciesJsonArray.getString(1);
                String currencyCounter = currenciesJsonArray.getString(0);
                if (!(currencyBase == null || currencyCounter == null)) {
                    pairs.add(new CurrencyPairInfo(currencyBase.toUpperCase(Locale.US), currencyCounter.toUpperCase(
                        Locale.US), null));
                }
            }
        }
    }
}
