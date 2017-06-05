package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.currency.VirtualCurrency;
import haivo.us.crypto.R;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Unknown extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "UNKNOWN";
    private static final String TTS_NAME = "UNKNOWN";
    private static final String URL = "";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{VirtualCurrency.BTC});
    }

    public Unknown() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public int getCautionResId() {
        return R.string.market_caution_unknown;
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }
}
