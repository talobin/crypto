package haivo.us.crypto.model.market;

import haivo.us.crypto.content.AbstractMaindbContentProvider;
import haivo.us.crypto.fragment.CheckersListFragment;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.FuturesMarket;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class OKCoinFutures extends FuturesMarket {
    private static final int[] CONTRACT_TYPES;
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "OKCoin Futures";
    private static final String TTS_NAME = "OK Coin Futures";
    private static final String URL = "https://www.okcoin.com/api/v1/future_ticker.do?symbol=%1$s_%2$s&contract_type=%3$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.USD});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{Currency.USD});
        CONTRACT_TYPES = new int[]{0, 1, 4};
    }

    public OKCoinFutures() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS, CONTRACT_TYPES);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo, int contractType) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBaseLowerCase(), checkerInfo.getCurrencyCounterLowerCase(), getContractTypeString(contractType)});
    }

    private String getContractTypeString(int contractType) {
        switch (contractType) {
            case CheckersListFragment.SORT_MODE_CURRENCY /*1*/:
                return "next_week";
            case AbstractMaindbContentProvider.NUM_URI_MATCHERS /*4*/:
                return "quarter";
            default:
                return "this_week";
        }
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject tickerJsonObject = jsonObject.getJSONObject("ticker");
        ticker.bid = tickerJsonObject.getDouble("buy");
        ticker.ask = tickerJsonObject.getDouble("sell");
        ticker.vol = tickerJsonObject.getDouble("vol");
        ticker.high = tickerJsonObject.getDouble("high");
        ticker.low = tickerJsonObject.getDouble("low");
        ticker.last = tickerJsonObject.getDouble("last");
    }
}
