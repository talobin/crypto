package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class LocalBitcoins extends Market {
    private static final String NAME = "LocalBitcoins";
    private static final String TTS_NAME = "Local Bitcoins";
    private static final String URL = "https://localbitcoins.com/bitcoinaverage/ticker-all-currencies/";
    private static final String URL_CURRENCY_PAIRS = "https://localbitcoins.com/bitcoinaverage/ticker-all-currencies/";

    public LocalBitcoins() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject pairJsonObject = jsonObject.getJSONObject(checkerInfo.getCurrencyPairId());
        ticker.vol = pairJsonObject.getDouble("volume_btc");
        ticker.last = pairJsonObject.getJSONObject("rates").getDouble("last");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairsJsonArray = jsonObject.names();
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            String currencyCounter = pairsJsonArray.getString(i);
            if (currencyCounter != null) {
                pairs.add(new CurrencyPairInfo(VirtualCurrency.BTC, currencyCounter, currencyCounter));
            }
        }
    }
}
