package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.Currency;
import haivo.us.crypto.model.currency.VirtualCurrency;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class Vircurex extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Vircurex";
    private static final String TTS_NAME = "Vircurex";
    private static final String URL = "https://api.vircurex.com/api/get_info_for_1_currency.json?base=%1$s&alt=%2$s";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.BTC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.ANC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.DGC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.DOGE, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.DVC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.FRC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.FTC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.I0C, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.IXC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.NMC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.NVC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.NXT, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.PPC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.QRK, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.TRC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.VTC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.VTC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.WDC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.WDC, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.XPM, new String[]{Currency.USD, Currency.EUR, VirtualCurrency.BTC, VirtualCurrency.ANC, VirtualCurrency.DGC, VirtualCurrency.DOGE, VirtualCurrency.DVC, VirtualCurrency.FRC, VirtualCurrency.FTC, VirtualCurrency.I0C, VirtualCurrency.IXC, VirtualCurrency.LTC, VirtualCurrency.NMC, VirtualCurrency.NVC, VirtualCurrency.NXT, VirtualCurrency.PPC, VirtualCurrency.QRK, VirtualCurrency.TRC, VirtualCurrency.VTC, VirtualCurrency.WDC});
    }

    public Vircurex() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return String.format(URL, new Object[]{checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter()});
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        ticker.bid = jsonObject.getDouble("highest_bid");
        ticker.ask = jsonObject.getDouble("lowest_ask");
        ticker.vol = jsonObject.getDouble("volume");
        ticker.last = jsonObject.getDouble("last_trade");
    }
}
