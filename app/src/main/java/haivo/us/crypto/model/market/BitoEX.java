package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONArray;
import uk.co.senab.actionbarpulltorefresh.library.BuildConfig;

public class BitoEX extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "BitoEX";
    private static final String TTS_NAME = "BitoEX";
    private static final String URL = "https://www.bitoex.com/sync/dashboard/%1$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.TWD });
    }

    public BitoEX() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { Long.valueOf(System.currentTimeMillis()) });
    }

    protected void parseTicker(int requestId, String responseString, Ticker ticker, CheckerInfo checkerInfo)
        throws Exception {
        JSONArray jsonArray = new JSONArray(responseString);
        ticker.ask = Double.parseDouble(jsonArray.getString(0).replaceAll(",", BuildConfig.VERSION_NAME));
        ticker.bid = Double.parseDouble(jsonArray.getString(1).replaceAll(",", BuildConfig.VERSION_NAME));
        ticker.last = ticker.ask;
        ticker.timestamp = Long.valueOf(jsonArray.getString(2)).longValue();
    }
}
