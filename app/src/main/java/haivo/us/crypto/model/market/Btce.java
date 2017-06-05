package haivo.us.crypto.model.market;

import haivo.us.crypto.config.Settings;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONArray;
import org.json.JSONObject;

public class Btce extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Btc-e";
    private static final String TTS_NAME = "Btc-e";
    private static final String URL = "https://%1$s/api/3/ticker/%2$s";
    private static final String URL_CURRENCY_PAIRS = "https://%1$s/api/3/info";
    private static final String URL_HOST_COM = "btc-e.com";
    private static final String URL_HOST_NZ = "btc-e.nz";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.USD, Currency.RUR, Currency.EUR });
        CURRENCY_PAIRS.put(VirtualCurrency.LTC,
                           new String[] { VirtualCurrency.BTC, Currency.USD, Currency.RUR, Currency.EUR });
        CURRENCY_PAIRS.put(VirtualCurrency.NMC, new String[] { VirtualCurrency.BTC, Currency.USD });
        CURRENCY_PAIRS.put(VirtualCurrency.NVC, new String[] { VirtualCurrency.BTC, Currency.USD });
        CURRENCY_PAIRS.put(Currency.USD, new String[] { Currency.RUR });
        CURRENCY_PAIRS.put(Currency.EUR, new String[] { Currency.USD, Currency.RUR });
        CURRENCY_PAIRS.put(VirtualCurrency.PPC, new String[] { VirtualCurrency.BTC, Currency.USD });
    }

    public Btce() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    private String detectHost() {
        if (Settings.userCountry == null || !Settings.userCountry.endsWith("RU")) {
            return URL_HOST_COM;
        }
        return URL_HOST_NZ;
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        String pairId = checkerInfo.getCurrencyPairId();
        if (checkerInfo.getCurrencyPairId() == null) {
            pairId = String.format("%1$s_%2$s",
                                   new Object[] {
                                       checkerInfo.getCurrencyBaseLowerCase(),
                                       checkerInfo.getCurrencyCounterLowerCase()
                                   });
        }
        return String.format(URL, new Object[] { detectHost(), pairId });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject tickerJsonObject = jsonObject.getJSONObject(jsonObject.names().getString(0));
        ticker.bid = tickerJsonObject.getDouble("sell");
        ticker.ask = tickerJsonObject.getDouble("buy");
        ticker.vol = tickerJsonObject.getDouble("vol");
        ticker.high = tickerJsonObject.getDouble("high");
        ticker.low = tickerJsonObject.getDouble("low");
        ticker.last = tickerJsonObject.getDouble("last");
        ticker.timestamp = tickerJsonObject.getLong("updated");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return String.format(URL_CURRENCY_PAIRS, new Object[] { detectHost() });
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray pairsNames = jsonObject.getJSONObject("pairs").names();
        for (int i = 0; i < pairsNames.length(); i++) {
            String pairId = pairsNames.getString(i);
            if (pairId != null) {
                String[] currencies = pairId.split("_");
                if (currencies.length == 2) {
                    pairs.add(new CurrencyPairInfo(currencies[0].toUpperCase(Locale.ENGLISH),
                                                   currencies[1].toUpperCase(Locale.ENGLISH),
                                                   pairId));
                }
            }
        }
    }
}
