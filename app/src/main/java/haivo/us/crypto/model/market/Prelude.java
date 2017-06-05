package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;

public class Prelude extends Market {
    private static final String NAME = "Prelude";
    private static final String TTS_NAME = "Prelude";
    private static final String URL_1 = "https://api.prelude.io/pairings/%1$s";
    private static final String URL_2_BTC = "https://api.prelude.io/statistics/%1$s";
    private static final String URL_2_USD = "https://api.prelude.io/statistics-usd/%1$s";
    private static final String URL_CURRENCY_PAIRS_BTC = "https://api.prelude.io/pairings/btc";
    private static final String URL_CURRENCY_PAIRS_USD = "https://api.prelude.io/pairings/usd";

    public Prelude() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        if (requestId == 0) {
            return String.format(URL_1, new Object[]{checkerInfo.getCurrencyCounterLowerCase()});
        } else if (Currency.USD.equals(checkerInfo.getCurrencyCounter())) {
            return String.format(URL_2_USD, new Object[]{checkerInfo.getCurrencyBase()});
        } else {
            return String.format(URL_2_BTC, new Object[]{checkerInfo.getCurrencyBase()});
        }
    }

    public int getNumOfRequests(CheckerInfo checkerInfo) {
        return 2;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        if (requestId == 0) {
            JSONArray pairings = jsonObject.getJSONArray("pairings");
            for (int i = 0; i < pairings.length(); i++) {
                JSONObject pairing = pairings.getJSONObject(i);
                if (checkerInfo.getCurrencyBase().equals(pairing.getString("pair"))) {
                    ticker.last = numberFormat.parse(pairing.getJSONObject("last_trade").getString("rate")).doubleValue();
                    return;
                }
            }
            return;
        }
        JSONObject statistics = jsonObject.getJSONObject("statistics");
        ticker.vol = numberFormat.parse(statistics.getString("volume")).doubleValue();
        ticker.high = numberFormat.parse(statistics.getString("high")).doubleValue();
        ticker.low = numberFormat.parse(statistics.getString("low")).doubleValue();
    }

    public int getCurrencyPairsNumOfRequests() {
        return 2;
    }

    public String getCurrencyPairsUrl(int requestId) {
        if (requestId == 1) {
            return URL_CURRENCY_PAIRS_USD;
        }
        return URL_CURRENCY_PAIRS_BTC;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray pairingsArray = jsonObject.getJSONArray("pairings");
        String currencyCounter = jsonObject.getString("from");
        for (int i = 0; i < pairingsArray.length(); i++) {
            pairs.add(new CurrencyPairInfo(pairingsArray.getJSONObject(i).getString("pair"), currencyCounter, null));
        }
    }
}
