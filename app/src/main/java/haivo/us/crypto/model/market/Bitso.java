package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONObject;
import haivo.us.crypto.util.ParseUtils;

public class Bitso extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Bitso";
    private static final String TTS_NAME = "Bitso";
    private static final String URL = "https://api.bitso.com/public/info";

    static {
        CURRENCY_PAIRS = new LinkedHashMap(1);
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { VirtualCurrency.MXN });
        CURRENCY_PAIRS.put(VirtualCurrency.ETH, new String[] { VirtualCurrency.MXN });
    }

    public Bitso() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject pairJsonObject = jsonObject.getJSONObject(checkerInfo.getCurrencyBaseLowerCase()
                                                                 + "_"
                                                                 + checkerInfo.getCurrencyCounterLowerCase());
        ticker.bid = ParseUtils.getDoubleFromString(pairJsonObject, "buy");
        ticker.ask = ParseUtils.getDoubleFromString(pairJsonObject, "sell");
        ticker.vol = ParseUtils.getDoubleFromString(pairJsonObject, "volume");
        ticker.last = ParseUtils.getDoubleFromString(pairJsonObject, "rate");
    }
}
