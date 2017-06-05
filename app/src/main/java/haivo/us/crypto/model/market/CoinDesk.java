package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CoinDesk extends Market {
    private static final String NAME = "CoinDesk";
    private static final String TTS_NAME = "Coin Desk";
    private static final String URL = "https://api.coindesk.com/v1/bpi/currentprice.json";
    private static final String URL_CURRENCY_PAIRS = "https://api.coindesk.com/v1/bpi/currentprice.json";

    public CoinDesk() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.last = jsonObject.getJSONObject("bpi").getJSONObject(checkerInfo.getCurrencyCounter()).getDouble("rate_float");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs) throws
                                                                                                                        Exception {
        JSONArray currencyCounterNames = jsonObject.getJSONObject("bpi").names();
        for (int i = 0; i < currencyCounterNames.length(); i++) {
            pairs.add(new CurrencyPairInfo(VirtualCurrency.BTC, currencyCounterNames.getString(i), null));
        }
    }
}
