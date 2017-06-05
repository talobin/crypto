package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class CoinMateIO extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "CoinMate.io";
    private static final String TTS_NAME = "Coin Mate";
    private static final String URL = "https://coinmate.io/api/ticker?currencyPair=%1$s_%2$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.EUR, Currency.CZK});
    }

    public CoinMateIO() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
        ticker.bid = dataJsonObject.getDouble("bid");
        ticker.ask = dataJsonObject.getDouble("ask");
        ticker.vol = dataJsonObject.getDouble("amount");
        ticker.high = dataJsonObject.getDouble("high");
        ticker.low = dataJsonObject.getDouble("low");
        ticker.last = dataJsonObject.getDouble("last");
    }

    protected String parseErrorFromJsonObject(int requestId, JSONObject jsonObject, CheckerInfo checkerInfo) throws
                                                                                                             Exception {
        return jsonObject.getString("errorMessage");
    }
}
