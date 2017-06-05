package haivo.us.crypto.model.market;

import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import haivo.us.crypto.model.currency.VirtualCurrency;
import haivo.us.crypto.R;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.json.JSONObject;

public class Coinse extends Market {
    private static final HashMap<String, CharSequence[]> CURRENCY_PAIRS;
    private static final String NAME = "Coins-E";
    private static final String TTS_NAME = "Coins-E";
    private static final String URL = "https://www.coins-e.com/api/v2/markets/data/";

    static {
        CURRENCY_PAIRS = new LinkedHashMap();
        CURRENCY_PAIRS.put(VirtualCurrency.ALP, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.AMC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.ANC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.ARG, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.BET, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.BQC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.BTG, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.CGB, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.CIN, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.CMC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.COL, new String[]{VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.CRC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.CSC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.DEM, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.DGC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.DMD, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.DOGE, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.DTC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.ELC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.ELP, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.EMD, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.EZC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.FLO, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.FRK, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.FTC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.GDC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.GLC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.GLX, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.HYC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.IFC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.KGC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.LBW, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.LTC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.MEC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.NAN, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.NET, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.NIB, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.NRB, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.NUC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.NVC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.ORB, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.PPC, new String[]{VirtualCurrency.BTC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.PTS, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.PWC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.PXC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.QRK, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC, VirtualCurrency.XPM});
        CURRENCY_PAIRS.put(VirtualCurrency.RCH, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.REC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.RED, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.SBC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.SPT, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.TAG, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.TRC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.UNO, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.VLC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.WDC, new String[]{VirtualCurrency.BTC});
        CURRENCY_PAIRS.put(VirtualCurrency.XNC, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.XPM, new String[]{VirtualCurrency.BTC, VirtualCurrency.LTC});
        CURRENCY_PAIRS.put(VirtualCurrency.ZET, new String[]{VirtualCurrency.BTC});
    }

    public Coinse() {
        super(TTS_NAME, TTS_NAME, CURRENCY_PAIRS);
    }

    public int getCautionResId() {
        return R.string.market_caution_much_data;
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        return URL;
    }

    protected void parseTickerFromJsonObject(int requestId, JSONObject jsonObject, Ticker ticker, CheckerInfo checkerInfo) throws
                                                                                                                           Exception {
        JSONObject marketStatObject = jsonObject.getJSONObject("markets").getJSONObject(checkerInfo.getCurrencyBase() + "_" + checkerInfo.getCurrencyCounter()).getJSONObject("marketstat");
        JSONObject inner24hObject = marketStatObject.getJSONObject("24h");
        ticker.bid = marketStatObject.getDouble("bid");
        ticker.ask = marketStatObject.getDouble("ask");
        ticker.vol = inner24hObject.getDouble("volume");
        ticker.high = inner24hObject.getDouble("h");
        ticker.low = inner24hObject.getDouble("l");
        ticker.last = marketStatObject.getDouble("ltp");
    }
}
