package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CryptoAltex extends Market {
    private static final String NAME = "CryptoAltex";
    private static final String TTS_NAME = "Crypto Altex";
    private static final String URL = "https://www.cryptoaltex.com/api/public_v2.php";

    public CryptoAltex() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject pairObject = jsonObject.getJSONObject(checkerInfo.getCurrencyPairId());
        ticker.vol = pairObject.getDouble("24_hours_volume");
        ticker.high = pairObject.getDouble("24_hours_price_high");
        ticker.low = pairObject.getDouble("24_hours_price_low");
        ticker.last = pairObject.getDouble("last_trade");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairsNamesArray = jsonObject.names();
        for (int i = 0; i < pairsNamesArray.length(); i++) {
            String pairName = pairsNamesArray.getString(i);
            String[] split = pairName.split("/");
            if (split != null && split.length >= 2) {
                pairs.add(new CurrencyPairInfo(split[0], split[1], pairName));
            }
        }
    }
}
