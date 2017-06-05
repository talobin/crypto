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

public class BitX extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "BitX.co";
    private static final String TTS_NAME = "Bit X";
    private static final String URL = "https://api.mybitx.com/api/1/ticker?pair=%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://api.mybitx.com/api/1/tickers";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC,
                           new String[] {
                               Currency.IDR,
                               Currency.SGD,
                               VirtualCurrency.MYR,
                               Currency.NGN,
                               Currency.ZAR
                           });
    }

    public BitX() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        String pairString;
        if (checkerInfo.getCurrencyPairId() == null) {
            pairString = String.format("%1$s%2$s",
                                       new Object[] {
                                           fixCurrency(checkerInfo.getCurrencyBase()),
                                           fixCurrency(checkerInfo.getCurrencyCounter())
                                       });
        } else {
            pairString = checkerInfo.getCurrencyPairId();
        }
        return String.format(URL, new Object[] { pairString });
    }

    private String fixCurrency(String currency) {
        if (VirtualCurrency.BTC.equals(currency)) {
            return VirtualCurrency.XBT;
        }
        if (VirtualCurrency.XBT.equals(currency)) {
            return VirtualCurrency.BTC;
        }
        return currency;
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        ticker.bid = jsonObject.getDouble("bid");
        ticker.ask = jsonObject.getDouble("ask");
        ticker.vol = jsonObject.getDouble("rolling_24_hour_volume");
        ticker.last = jsonObject.getDouble("last_trade");
        ticker.timestamp = jsonObject.getLong("timestamp");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray dataJsonArray = jsonObject.getJSONArray("tickers");
        for (int i = 0; i < dataJsonArray.length(); i++) {
            String currencyPair = dataJsonArray.getJSONObject(i).getString("pair");
            if (currencyPair != null) {
                try {
                    pairs.add(new CurrencyPairInfo(fixCurrency(currencyPair.substring(0, 3)),
                                                   fixCurrency(currencyPair.substring(3)),
                                                   currencyPair));
                } catch (Exception e) {
                }
            }
        }
    }
}
