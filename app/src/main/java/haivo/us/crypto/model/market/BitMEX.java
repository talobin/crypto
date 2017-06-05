package haivo.us.crypto.model.market;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;

public class BitMEX extends Market {
    private static final SimpleDateFormat ISO_DATE_FORMAT;
    private static final String NAME = "BitMEX";
    private static final String TTS_NAME = "BitMEX";
    private static final String URL =
        "https://www.bitmex.com/api/v1/instrument?symbol=%1$s&columns=bidPrice,askPrice,turnover24h,highPrice,lowPrice,lastPrice";
    private static final String URL_CURRENCY_PAIRS =
        "https://www.bitmex.com/api/v1/instrument?columns=rootSymbol,typ&filter={\"state\":\"Open\"}";

    static {
        ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
        ISO_DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    public BitMEX() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyPairId() });
    }

    protected void parseTicker(int requestId, String responseString, Ticker ticker, CheckerInfo checkerInfo)
        throws Exception {
        parseTickerFromJsonObject(requestId, new JSONArray(responseString).getJSONObject(0), ticker, checkerInfo);
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        ticker.bid = jsonObject.getDouble("bidPrice");
        ticker.ask = jsonObject.getDouble("askPrice");
        ticker.vol = jsonObject.getDouble("turnover24h") / 1.0E8d;
        if (!jsonObject.isNull("highPrice")) {
            ticker.high = jsonObject.getDouble("highPrice");
        }
        if (!jsonObject.isNull("lowPrice")) {
            ticker.low = jsonObject.getDouble("lowPrice");
        }
        ticker.last = jsonObject.getDouble("lastPrice");
        ticker.timestamp = ISO_DATE_FORMAT.parse(jsonObject.getString("timestamp")).getTime();
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairs(int requestId, String responseString, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray instruments = new JSONArray(responseString);
        for (int i = 0; i < instruments.length(); i++) {
            parseCurrencyPairsFromJsonObject(requestId, instruments.getJSONObject(i), pairs);
        }
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        String base = jsonObject.getString("rootSymbol");
        String id = jsonObject.getString("symbol");
        String quote = id.substring(id.indexOf(base) + base.length());
        if (jsonObject.getString("typ").equals("FFICSX")) {
            quote = base;
            base = "BINARY";
        }
        pairs.add(new CurrencyPairInfo(base, quote, id));
    }
}
