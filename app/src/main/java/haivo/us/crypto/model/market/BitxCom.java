package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONObject;

public class BitxCom extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "CoinsBank";
    private static final String TTS_NAME = "CoinsBank";
    private static final String URL = "https://coinsbank.com/api/public/ticker?pair=%1$s%2$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.EUR, Currency.GBP, Currency.USD });
        CURRENCY_PAIRS.put(VirtualCurrency.LTC,
                           new String[] { VirtualCurrency.BTC, Currency.EUR, Currency.GBP, Currency.USD });
        CURRENCY_PAIRS.put(VirtualCurrency.GHS,
                           new String[] {
                               VirtualCurrency.BTC,
                               Currency.EUR,
                               Currency.GBP,
                               VirtualCurrency.LTC,
                               Currency.USD
                           });
        CURRENCY_PAIRS.put(Currency.EUR, new String[] { Currency.GBP, Currency.USD });
        CURRENCY_PAIRS.put(Currency.GBP, new String[] { Currency.USD });
    }

    public BitxCom() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
        ticker.bid = dataJsonObject.getDouble("buy");
        ticker.ask = dataJsonObject.getDouble("sell");
        ticker.vol = dataJsonObject.getDouble("volume");
        ticker.high = dataJsonObject.getDouble("high");
        ticker.low = dataJsonObject.getDouble("low");
        ticker.last = dataJsonObject.getDouble("last");
    }
}
