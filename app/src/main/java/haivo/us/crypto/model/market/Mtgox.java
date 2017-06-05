package haivo.us.crypto.model.market;

import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class Mtgox extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Mtgox";
    private static final String TTS_NAME = "MT gox";
    private static final String URL = "https://data.mtgox.com/api/2/%1$s%2$s/money/ticker";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.USD, Currency.EUR, Currency.CAD, Currency.GBP, Currency.CHF, Currency.RUB, Currency.AUD, Currency.SEK, Currency.DKK, Currency.HKD, Currency.PLN, Currency.CNY, Currency.SGD, Currency.THB, Currency.NZD, Currency.JPY});
    }

    public Mtgox() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject dataObject = jsonObject.getJSONObject("data");
        ticker.bid = getPriceValueFromObject(dataObject, "buy");
        ticker.ask = getPriceValueFromObject(dataObject, "sell");
        ticker.vol = getPriceValueFromObject(dataObject, "vol");
        ticker.high = getPriceValueFromObject(dataObject, "high");
        ticker.low = getPriceValueFromObject(dataObject, "low");
        ticker.last = getPriceValueFromObject(dataObject, "last_local");
        ticker.timestamp = dataObject.getLong("now") / 1000;
    }

    private double getPriceValueFromObject(JSONObject jsonObject, String key) throws Exception {
        return jsonObject.getJSONObject(key).getDouble(MaindbContract.AlarmColumns.VALUE);
    }
}
