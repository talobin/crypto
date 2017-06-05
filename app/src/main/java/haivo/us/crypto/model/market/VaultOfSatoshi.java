package haivo.us.crypto.model.market;

import haivo.us.crypto.content.MaindbContract;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class VaultOfSatoshi extends Market {
    private static final String NAME = "VaultOfSatoshi";
    private static final String TTS_NAME = "Vault Of Satoshi";
    private static final String URL = "https://api.vaultofsatoshi.com/public/ticker?order_currency=%1$s&payment_currency=%2$s";
    private static final String URL_CURRENCY_PAIRS = "https://api.vaultofsatoshi.com/public/currency";

    public VaultOfSatoshi() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBaseLowerCase(), checkerInfo.getCurrencyCounterLowerCase()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject dataObject = jsonObject.getJSONObject("data");
        ticker.vol = getDoubleFromMtgoxFormatObject(dataObject, "volume_1day");
        ticker.high = getDoubleFromMtgoxFormatObject(dataObject, "max_price");
        ticker.low = getDoubleFromMtgoxFormatObject(dataObject, "min_price");
        ticker.last = getDoubleFromMtgoxFormatObject(dataObject, "closing_price");
        ticker.timestamp = dataObject.getLong("date");
    }

    private double getDoubleFromMtgoxFormatObject(JSONObject jsonObject, String key) throws Exception {
        return jsonObject.getJSONObject(key).getDouble(MaindbContract.AlarmColumns.VALUE);
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        int i;
        JSONArray dataJsonArray = jsonObject.getJSONArray("data");
        ArrayList<String> virtualCurrencies = new ArrayList();
        ArrayList<String> currencies = new ArrayList();
        for (i = 0; i < dataJsonArray.length(); i++) {
            JSONObject currencyJsonObject = dataJsonArray.getJSONObject(i);
            if (currencyJsonObject.getInt("virtual") != 0) {
                virtualCurrencies.add(currencyJsonObject.getString("code"));
            } else {
                currencies.add(currencyJsonObject.getString("code"));
            }
        }
        int virtualCurrenciesCount = virtualCurrencies.size();
        int currenciesCount = currencies.size();
        for (i = 0; i < virtualCurrenciesCount; i++) {
            int j;
            for (j = 0; j < currenciesCount; j++) {
                pairs.add(new CurrencyPairInfo((String) virtualCurrencies.get(i), (String) currencies.get(j), null));
            }
            for (j = 0; j < virtualCurrenciesCount; j++) {
                if (i != j) {
                    pairs.add(new CurrencyPairInfo((String) virtualCurrencies.get(i), (String) virtualCurrencies.get(j), null));
                }
            }
        }
    }
}
