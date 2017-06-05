package haivo.us.crypto.model.market;

import haivo.us.crypto.content.MaindbContract;
import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONObject;

public class Anxpro extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Anxpro";
    private static final String TTS_NAME = "ANX Pro";
    private static final String URL = "https://anxpro.com/api/2/%1$s%2$s/money/ticker";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC,
                           new String[] {
                               Currency.USD,
                               Currency.HKD,
                               Currency.EUR,
                               Currency.CAD,
                               Currency.AUD,
                               Currency.SGD,
                               Currency.JPY,
                               Currency.CHF,
                               Currency.GBP,
                               Currency.NZD
                           });
        CURRENCY_PAIRS.put(VirtualCurrency.DOGE,
                           new String[] {
                               VirtualCurrency.BTC,
                               Currency.USD,
                               Currency.HKD,
                               Currency.EUR,
                               Currency.CAD,
                               Currency.AUD,
                               Currency.SGD,
                               Currency.JPY,
                               Currency.CHF,
                               Currency.GBP,
                               Currency.NZD
                           });
        CURRENCY_PAIRS.put(VirtualCurrency.LTC,
                           new String[] {
                               VirtualCurrency.BTC,
                               Currency.USD,
                               Currency.HKD,
                               Currency.EUR,
                               Currency.CAD,
                               Currency.AUD,
                               Currency.SGD,
                               Currency.JPY,
                               Currency.CHF,
                               Currency.GBP,
                               Currency.NZD
                           });
        CURRENCY_PAIRS.put(VirtualCurrency.PPC,
                           new String[] {
                               VirtualCurrency.BTC,
                               VirtualCurrency.LTC,
                               Currency.USD,
                               Currency.HKD,
                               Currency.EUR,
                               Currency.CAD,
                               Currency.AUD,
                               Currency.SGD,
                               Currency.JPY,
                               Currency.CHF,
                               Currency.GBP,
                               Currency.NZD
                           });
        CURRENCY_PAIRS.put(VirtualCurrency.NMC,
                           new String[] {
                               VirtualCurrency.BTC,
                               VirtualCurrency.LTC,
                               Currency.USD,
                               Currency.HKD,
                               Currency.EUR,
                               Currency.CAD,
                               Currency.AUD,
                               Currency.SGD,
                               Currency.JPY,
                               Currency.CHF,
                               Currency.GBP,
                               Currency.NZD
                           });
    }

    public Anxpro() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject dataObject = jsonObject.getJSONObject("data");
        ticker.bid = getPriceValueFromObject(dataObject, "buy");
        ticker.ask = getPriceValueFromObject(dataObject, "sell");
        ticker.vol = getPriceValueFromObject(dataObject, "vol");
        ticker.high = getPriceValueFromObject(dataObject, "high");
        ticker.low = getPriceValueFromObject(dataObject, "low");
        ticker.last = getPriceValueFromObject(dataObject, "last");
        ticker.timestamp = dataObject.getLong("now") / 1000;
    }

    private double getPriceValueFromObject(JSONObject jsonObject, String key) throws Exception {
        return jsonObject.getJSONObject(key).getDouble(MaindbContract.AlarmColumns.VALUE);
    }
}
