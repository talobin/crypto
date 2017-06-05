package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONArray;
import org.json.JSONObject;

public class BitcoinAverage extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "BitcoinAverage";
    private static final String TTS_NAME = "Bitcoin Average";
    private static final String URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://apiv2.bitcoinaverage.com/constants/symbols";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC,
                           new String[] {
                               Currency.AUD,
                               Currency.BRL,
                               Currency.CAD,
                               Currency.CHF,
                               Currency.CNY,
                               Currency.CZK,
                               Currency.EUR,
                               Currency.GBP,
                               Currency.ILS,
                               Currency.JPY,
                               Currency.NOK,
                               Currency.NZD,
                               Currency.PLN,
                               Currency.RUB,
                               Currency.SEK,
                               Currency.SGD,
                               Currency.USD,
                               Currency.ZAR
                           });
    }

    public BitcoinAverage() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        String pairId;
        if (checkerInfo.getCurrencyPairId() == null) {
            pairId = checkerInfo.getCurrencyBase() + checkerInfo.getCurrencyCounter();
        } else {
            pairId = checkerInfo.getCurrencyPairId();
        }
        return String.format(URL, new Object[] { pairId });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        ticker.bid = jsonObject.getDouble("bid");
        ticker.ask = jsonObject.getDouble("ask");
        ticker.vol = jsonObject.getDouble("volume");
        ticker.high = jsonObject.getDouble("high");
        ticker.low = jsonObject.getDouble("low");
        ticker.last = jsonObject.getDouble("last");
        ticker.timestamp = jsonObject.getLong("timestamp") * 1000;
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray pairsJsonArray = jsonObject.getJSONObject("global").getJSONArray("symbols");
        for (int i = 0; i < pairsJsonArray.length(); i++) {
            String pairId = pairsJsonArray.getString(i);
            pairs.add(new CurrencyPairInfo(pairId.substring(0, 3), pairId.substring(3), pairId));
        }
    }
}
