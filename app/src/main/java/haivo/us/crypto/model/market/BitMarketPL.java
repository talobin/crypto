package haivo.us.crypto.model.market;

import java.util.HashMap;
import java.util.LinkedHashMap;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import org.json.JSONObject;

public class BitMarketPL extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "BitMarket.pl";
    private static final String SYMBOL_SWAP = "SWAP";
    private static final String TTS_NAME = "Bit Market";
    private static final String URL = "https://www.bitmarket.pl/json/%1$s%2$s/ticker.json";
    private static final String URL_SWAP = "https://bitmarket.pl/json/swap%1$s/swap.json";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[] { Currency.EUR, Currency.PLN, SYMBOL_SWAP });
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[] { VirtualCurrency.BTC, Currency.PLN, SYMBOL_SWAP });
        CURRENCY_PAIRS.put(VirtualCurrency.ETH, new String[] { SYMBOL_SWAP });
    }

    public BitMarketPL() {
        super(NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    private boolean isSwap(CheckerInfo checkerInfo) {
        return SYMBOL_SWAP.equals(checkerInfo.getCurrencyCounter());
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        if (isSwap(checkerInfo)) {
            return String.format(URL_SWAP, new Object[] { checkerInfo.getCurrencyBase() });
        }
        return String.format(URL, new Object[] { checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter() });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        if (isSwap(checkerInfo)) {
            ticker.last = jsonObject.getDouble("cutoff");
            return;
        }
        ticker.bid = jsonObject.getDouble("bid");
        ticker.ask = jsonObject.getDouble("ask");
        ticker.vol = jsonObject.getDouble("volume");
        ticker.high = jsonObject.getDouble("high");
        ticker.low = jsonObject.getDouble("low");
        ticker.last = jsonObject.getDouble("last");
    }
}
