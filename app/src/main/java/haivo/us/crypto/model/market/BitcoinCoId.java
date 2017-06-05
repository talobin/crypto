package haivo.us.crypto.model.market;

import java.util.List;
import java.util.Locale;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;

public class BitcoinCoId extends Market {
    private static final String NAME = "Bitcoin.co.id";
    private static final String TTS_NAME = "Bitcoin co id";
    private static final String URL = "https://vip.bitcoin.co.id/api/%1$s/ticker/";
    private static final String URL_CURRENCY_PAIRS = "https://vip.bitcoin.co.id/api/summaries";

    public BitcoinCoId() {
        super(NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        String pairId = checkerInfo.getCurrencyPairId();
        if (pairId == null) {
            pairId = String.format("%1$s_%2$s",
                                   new Object[] {
                                       checkerInfo.getCurrencyBaseLowerCase(),
                                       checkerInfo.getCurrencyCounterLowerCase()
                                   });
        }
        return String.format(URL, new Object[] { pairId });
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject tickerJsonObject = jsonObject.getJSONObject("ticker");
        ticker.bid = tickerJsonObject.getDouble("buy");
        ticker.ask = tickerJsonObject.getDouble("sell");
        ticker.vol = tickerJsonObject.getDouble("vol_btc");
        ticker.high = tickerJsonObject.getDouble("high");
        ticker.low = tickerJsonObject.getDouble("low");
        ticker.last = tickerJsonObject.getDouble("last");
        ticker.timestamp = tickerJsonObject.getLong("server_time");
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray tickerNamesArray = jsonObject.getJSONObject("tickers").names();
        for (int i = 0; i < tickerNamesArray.length(); i++) {
            String pairId = tickerNamesArray.getString(i);
            if (pairId != null) {
                String[] currencies = pairId.split("_");
                if (currencies.length == 2) {
                    pairs.add(new CurrencyPairInfo(currencies[0].toUpperCase(Locale.ENGLISH),
                                                   currencies[1].toUpperCase(Locale.ENGLISH),
                                                   pairId));
                }
            }
        }
    }
}
