package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.R;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CryptoRush extends Market {
    public static final String NAME = "CryptoRush";
    public static final String TTS_NAME = "Crypto Rush";
    public static final String URL = "https://cryptorush.in/marketdata/all.json";
    private static final String URL_CURRENCY_PAIRS = "https://cryptorush.in/marketdata/all.json";

    public CryptoRush() {
        super(NAME, TTS_NAME, null);
    }

    public int getCautionResId() {
        return R.string.market_caution_much_data;
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL_CURRENCY_PAIRS, new Object[]{checkerInfo.getCurrencyBaseLowerCase(), checkerInfo.getCurrencyCounterLowerCase()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject pairJsonObject = jsonObject.getJSONObject(checkerInfo.getCurrencyBase() + "/" + checkerInfo.getCurrencyCounter());
        ticker.bid = pairJsonObject.getDouble("current_bid");
        ticker.ask = pairJsonObject.getDouble("current_ask");
        ticker.vol = pairJsonObject.getDouble("volume_base_24h");
        ticker.high = pairJsonObject.getDouble("highest_24h");
        ticker.low = pairJsonObject.getDouble("lowest_24h");
        ticker.last = pairJsonObject.getDouble("last_trade");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairNames = jsonObject.names();
        for (int i = 0; i < pairNames.length(); i++) {
            String pairId = pairNames.getString(i);
            if (pairId != null) {
                String[] currencies = pairId.split("/");
                if (currencies.length == 2) {
                    pairs.add(new CurrencyPairInfo(currencies[0], currencies[1], pairId));
                }
            }
        }
    }
}
