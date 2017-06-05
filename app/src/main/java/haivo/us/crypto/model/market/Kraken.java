package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Kraken extends Market {
    private static final String NAME = "Kraken";
    private static final String TTS_NAME = "Kraken";
    private static final String URL = "https://api.kraken.com/0/public/Ticker?pair=%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://api.kraken.com/0/public/AssetPairs";

    public Kraken() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        if (checkerInfo.getCurrencyPairId() != null) {
            return String.format(URL, new Object[]{checkerInfo.getCurrencyPairId()});
        }
        String currencyBase = fixCurrency(checkerInfo.getCurrencyBase());
        String currencyCounter = fixCurrency(checkerInfo.getCurrencyCounter());
        return String.format(URL, new Object[]{currencyBase + currencyCounter});
    }

    private String fixCurrency(String currency) {
        if (VirtualCurrency.BTC.equals(currency)) {
            return VirtualCurrency.XBT;
        }
        if (VirtualCurrency.VEN.equals(currency)) {
            return VirtualCurrency.XVN;
        }
        if (VirtualCurrency.DOGE.equals(currency)) {
            return VirtualCurrency.XDG;
        }
        return currency;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject resultObject = jsonObject.getJSONObject("result");
        JSONObject pairObject = resultObject.getJSONObject(resultObject.names().getString(0));
        ticker.bid = getDoubleFromJsonArrayObject(pairObject, "b");
        ticker.ask = getDoubleFromJsonArrayObject(pairObject, "a");
        ticker.vol = getDoubleFromJsonArrayObject(pairObject, "v");
        ticker.high = getDoubleFromJsonArrayObject(pairObject, "h");
        ticker.low = getDoubleFromJsonArrayObject(pairObject, "l");
        ticker.last = getDoubleFromJsonArrayObject(pairObject, "c");
    }

    private double getDoubleFromJsonArrayObject(JSONObject jsonObject, String arrayKey) throws Exception {
        JSONArray jsonArray = jsonObject.getJSONArray(arrayKey);
        return (jsonArray == null || jsonArray.length() <= 0) ? 0.0d : jsonArray.getDouble(0);
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray pairNames = result.names();
        for (int i = 0; i < pairNames.length(); i++) {
            String pairId = pairNames.getString(i);
            if (pairId != null && pairId.indexOf(46) == -1) {
                JSONObject pairJsonObject = result.getJSONObject(pairId);
                pairs.add(new CurrencyPairInfo(parseCurrency(pairJsonObject.getString("base")), parseCurrency(pairJsonObject.getString("quote")), pairId));
            }
        }
    }

    private String parseCurrency(String currency) {
        if (currency != null && currency.length() >= 2) {
            currency = currency.substring(1);
        }
        if (VirtualCurrency.XBT.equals(currency)) {
            return VirtualCurrency.BTC;
        }
        if (VirtualCurrency.XVN.equals(currency)) {
            return VirtualCurrency.VEN;
        }
        if (VirtualCurrency.XDG.equals(currency)) {
            return VirtualCurrency.DOGE;
        }
        return currency;
    }
}
