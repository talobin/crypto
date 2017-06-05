package haivo.us.crypto.model.market;

import java.util.List;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONArray;
import org.json.JSONObject;

public class BitcoinVenezuela extends Market {
    private static final String NAME = "BitcoinVenezuela";
    private static final String TTS_NAME = "Bitcoin Venezuela";
    private static final String URL = "http://api.bitcoinvenezuela.com/?html=no&currency=%1$s&amount=1&to=%2$s";
    private static final String URL_CURRENCY_PAIRS = "http://api.bitcoinvenezuela.com/";

    public BitcoinVenezuela() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTicker(int requestId, String responseString, Ticker ticker, CheckerInfo checkerInfo)
        throws Exception {
        ticker.last = Double.parseDouble(responseString.trim());
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        parseCurrencyPairsFromCurrencyBase(VirtualCurrency.BTC, jsonObject, pairs);
        parseCurrencyPairsFromCurrencyBase(VirtualCurrency.LTC, jsonObject, pairs);
        parseCurrencyPairsFromCurrencyBase(VirtualCurrency.MSC, jsonObject, pairs);
    }

    private void parseCurrencyPairsFromCurrencyBase(String currencyBase,
                                                    JSONObject jsonObject,
                                                    List<CurrencyPairInfo> pairs) throws Exception {
        if (jsonObject.has(currencyBase)) {
            JSONArray counterCurrencyNames = jsonObject.getJSONObject(currencyBase).names();
            for (int i = 0; i < counterCurrencyNames.length(); i++) {
                pairs.add(new CurrencyPairInfo(currencyBase, counterCurrencyNames.getString(i), null));
            }
        }
    }
}
