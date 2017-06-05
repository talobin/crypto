package haivo.us.crypto.model.market;

import android.support.v4.media.TransportMediator;
import java.util.HashMap;
import java.util.List;
import haivo.us.crypto.model.CheckerInfo;
import haivo.us.crypto.model.CurrencyPairInfo;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.Ticker;
import org.json.JSONArray;
import org.json.JSONObject;

public class Cryptsy extends Market {
    private static final HashMap<String, Integer> CURRENCY_PAIRS_IDS;
    private static final String NAME = "Cryptsy";
    private static final String TTS_NAME = "Cryptsy";
    private static final String URL = "https://cryptsy.com/api/v2/markets/%1$s";
    private static final String URL_CURRENCY_PAIRS = "https://cryptsy.com/api/v2/markets";

    static {
        CURRENCY_PAIRS_IDS = new HashMap(151);
        CURRENCY_PAIRS_IDS.put("42_BTC", Integer.valueOf(141));
        CURRENCY_PAIRS_IDS.put("ADT_LTC", Integer.valueOf(94));
        CURRENCY_PAIRS_IDS.put("ADT_XPM", Integer.valueOf(113));
        CURRENCY_PAIRS_IDS.put("ALF_BTC", Integer.valueOf(57));
        CURRENCY_PAIRS_IDS.put("AMC_BTC", Integer.valueOf(43));
        CURRENCY_PAIRS_IDS.put("ANC_BTC", Integer.valueOf(66));
        CURRENCY_PAIRS_IDS.put("ANC_LTC", Integer.valueOf(121));
        CURRENCY_PAIRS_IDS.put("ARG_BTC", Integer.valueOf(48));
        CURRENCY_PAIRS_IDS.put("ASC_LTC", Integer.valueOf(111));
        CURRENCY_PAIRS_IDS.put("ASC_XPM", Integer.valueOf(112));
        CURRENCY_PAIRS_IDS.put("AUR_BTC", Integer.valueOf(160));
        CURRENCY_PAIRS_IDS.put("AUR_LTC", Integer.valueOf(161));
        CURRENCY_PAIRS_IDS.put("BCX_BTC", Integer.valueOf(142));
        CURRENCY_PAIRS_IDS.put("BEN_BTC", Integer.valueOf(157));
        CURRENCY_PAIRS_IDS.put("BET_BTC", Integer.valueOf(129));
        CURRENCY_PAIRS_IDS.put("BQC_BTC", Integer.valueOf(10));
        CURRENCY_PAIRS_IDS.put("BTB_BTC", Integer.valueOf(23));
        CURRENCY_PAIRS_IDS.put("BTE_BTC", Integer.valueOf(49));
        CURRENCY_PAIRS_IDS.put("BTG_BTC", Integer.valueOf(50));
        CURRENCY_PAIRS_IDS.put("BUK_BTC", Integer.valueOf(102));
        CURRENCY_PAIRS_IDS.put("CACH_BTC", Integer.valueOf(154));
        CURRENCY_PAIRS_IDS.put("CAP_BTC", Integer.valueOf(53));
        CURRENCY_PAIRS_IDS.put("CASH_BTC", Integer.valueOf(150));
        CURRENCY_PAIRS_IDS.put("CAT_BTC", Integer.valueOf(136));
        CURRENCY_PAIRS_IDS.put("CGB_BTC", Integer.valueOf(70));
        CURRENCY_PAIRS_IDS.put("CGB_LTC", Integer.valueOf(123));
        CURRENCY_PAIRS_IDS.put("CLR_BTC", Integer.valueOf(95));
        CURRENCY_PAIRS_IDS.put("CMC_BTC", Integer.valueOf(74));
        CURRENCY_PAIRS_IDS.put("CNC_BTC", Integer.valueOf(8));
        CURRENCY_PAIRS_IDS.put("CNC_LTC", Integer.valueOf(17));
        CURRENCY_PAIRS_IDS.put("COL_LTC", Integer.valueOf(109));
        CURRENCY_PAIRS_IDS.put("COL_XPM", Integer.valueOf(110));
        CURRENCY_PAIRS_IDS.put("CPR_LTC", Integer.valueOf(91));
        CURRENCY_PAIRS_IDS.put("CRC_BTC", Integer.valueOf(58));
        CURRENCY_PAIRS_IDS.put("CSC_BTC", Integer.valueOf(68));
        CURRENCY_PAIRS_IDS.put("DBL_LTC", Integer.valueOf(46));
        CURRENCY_PAIRS_IDS.put("DEM_BTC", Integer.valueOf(131));
        CURRENCY_PAIRS_IDS.put("DGB_BTC", Integer.valueOf(167));
        CURRENCY_PAIRS_IDS.put("DGC_BTC", Integer.valueOf(26));
        CURRENCY_PAIRS_IDS.put("DGC_LTC", Integer.valueOf(96));
        CURRENCY_PAIRS_IDS.put("DMD_BTC", Integer.valueOf(72));
        CURRENCY_PAIRS_IDS.put("DOGE_BTC", Integer.valueOf(132));
        CURRENCY_PAIRS_IDS.put("DOGE_LTC", Integer.valueOf(135));
        CURRENCY_PAIRS_IDS.put("DRK_BTC", Integer.valueOf(155));
        CURRENCY_PAIRS_IDS.put("DVC_BTC", Integer.valueOf(40));
        CURRENCY_PAIRS_IDS.put("DVC_LTC", Integer.valueOf(52));
        CURRENCY_PAIRS_IDS.put("DVC_XPM", Integer.valueOf(122));
        CURRENCY_PAIRS_IDS.put("EAC_BTC", Integer.valueOf(139));
        CURRENCY_PAIRS_IDS.put("ELC_BTC", Integer.valueOf(12));
        CURRENCY_PAIRS_IDS.put("ELP_LTC", Integer.valueOf(93));
        CURRENCY_PAIRS_IDS.put("EMD_BTC", Integer.valueOf(69));
        CURRENCY_PAIRS_IDS.put("EZC_BTC", Integer.valueOf(47));
        CURRENCY_PAIRS_IDS.put("EZC_LTC", Integer.valueOf(55));
        CURRENCY_PAIRS_IDS.put("FFC_BTC", Integer.valueOf(138));
        CURRENCY_PAIRS_IDS.put("FLAP_BTC", Integer.valueOf(165));
        CURRENCY_PAIRS_IDS.put("FLO_LTC", Integer.valueOf(61));
        CURRENCY_PAIRS_IDS.put("FRC_BTC", Integer.valueOf(39));
        CURRENCY_PAIRS_IDS.put("FRK_BTC", Integer.valueOf(33));
        CURRENCY_PAIRS_IDS.put("FST_BTC", Integer.valueOf(44));
        CURRENCY_PAIRS_IDS.put("FST_LTC", Integer.valueOf(124));
        CURRENCY_PAIRS_IDS.put("FTC_BTC", Integer.valueOf(5));
        CURRENCY_PAIRS_IDS.put("GDC_BTC", Integer.valueOf(82));
        CURRENCY_PAIRS_IDS.put("GLC_BTC", Integer.valueOf(76));
        CURRENCY_PAIRS_IDS.put("GLD_BTC", Integer.valueOf(30));
        CURRENCY_PAIRS_IDS.put("GLD_LTC", Integer.valueOf(36));
        CURRENCY_PAIRS_IDS.put("GLX_BTC", Integer.valueOf(78));
        CURRENCY_PAIRS_IDS.put("GME_LTC", Integer.valueOf(84));
        CURRENCY_PAIRS_IDS.put("HBN_BTC", Integer.valueOf(80));
        CURRENCY_PAIRS_IDS.put("IFC_BTC", Integer.valueOf(59));
        CURRENCY_PAIRS_IDS.put("IFC_LTC", Integer.valueOf(60));
        CURRENCY_PAIRS_IDS.put("IFC_XPM", Integer.valueOf(105));
        CURRENCY_PAIRS_IDS.put("IXC_BTC", Integer.valueOf(38));
        CURRENCY_PAIRS_IDS.put("JKC_BTC", Integer.valueOf(25));
        CURRENCY_PAIRS_IDS.put("JKC_LTC", Integer.valueOf(35));
        CURRENCY_PAIRS_IDS.put("KGC_BTC", Integer.valueOf(65));
        CURRENCY_PAIRS_IDS.put("LEAF_BTC", Integer.valueOf(148));
        CURRENCY_PAIRS_IDS.put("LK7_BTC", Integer.valueOf(116));
        CURRENCY_PAIRS_IDS.put("LKY_BTC", Integer.valueOf(34));
        CURRENCY_PAIRS_IDS.put("LOT_BTC", Integer.valueOf(137));
        CURRENCY_PAIRS_IDS.put("LTC_BTC", Integer.valueOf(3));
        CURRENCY_PAIRS_IDS.put("MAX_BTC", Integer.valueOf(152));
        CURRENCY_PAIRS_IDS.put("MEC_BTC", Integer.valueOf(45));
        CURRENCY_PAIRS_IDS.put("MEC_LTC", Integer.valueOf(100));
        CURRENCY_PAIRS_IDS.put("MEM_LTC", Integer.valueOf(56));
        CURRENCY_PAIRS_IDS.put("MEOW_BTC", Integer.valueOf(149));
        CURRENCY_PAIRS_IDS.put("MINT_BTC", Integer.valueOf(156));
        CURRENCY_PAIRS_IDS.put("MNC_BTC", Integer.valueOf(7));
        CURRENCY_PAIRS_IDS.put("MOON_LTC", Integer.valueOf(145));
        CURRENCY_PAIRS_IDS.put("MST_LTC", Integer.valueOf(62));
        CURRENCY_PAIRS_IDS.put("MZC_BTC", Integer.valueOf(164));
        CURRENCY_PAIRS_IDS.put("NAN_BTC", Integer.valueOf(64));
        CURRENCY_PAIRS_IDS.put("NBL_BTC", Integer.valueOf(32));
        CURRENCY_PAIRS_IDS.put("NEC_BTC", Integer.valueOf(90));
        CURRENCY_PAIRS_IDS.put("NET_BTC", Integer.valueOf(134));
        CURRENCY_PAIRS_IDS.put("NET_LTC", Integer.valueOf(108));
        CURRENCY_PAIRS_IDS.put("NET_XPM", Integer.valueOf(104));
        CURRENCY_PAIRS_IDS.put("NMC_BTC", Integer.valueOf(29));
        CURRENCY_PAIRS_IDS.put("NRB_BTC", Integer.valueOf(54));
        CURRENCY_PAIRS_IDS.put("NVC_BTC", Integer.valueOf(13));
        CURRENCY_PAIRS_IDS.put("NXT_BTC", Integer.valueOf(159));
        CURRENCY_PAIRS_IDS.put("NXT_LTC", Integer.valueOf(162));
        CURRENCY_PAIRS_IDS.put("ORB_BTC", Integer.valueOf(75));
        CURRENCY_PAIRS_IDS.put("OSC_BTC", Integer.valueOf(144));
        CURRENCY_PAIRS_IDS.put("PHS_BTC", Integer.valueOf(86));
        CURRENCY_PAIRS_IDS.put("POINTS_BTC", Integer.valueOf(120));
        CURRENCY_PAIRS_IDS.put("PPC_BTC", Integer.valueOf(28));
        CURRENCY_PAIRS_IDS.put("PPC_LTC", Integer.valueOf(125));
        CURRENCY_PAIRS_IDS.put("PTS_BTC", Integer.valueOf(119));
        CURRENCY_PAIRS_IDS.put("PXC_BTC", Integer.valueOf(31));
        CURRENCY_PAIRS_IDS.put("PXC_LTC", Integer.valueOf(101));
        CURRENCY_PAIRS_IDS.put("PYC_BTC", Integer.valueOf(92));
        CURRENCY_PAIRS_IDS.put("QRK_BTC", Integer.valueOf(71));
        CURRENCY_PAIRS_IDS.put("QRK_LTC", Integer.valueOf(TransportMediator.KEYCODE_MEDIA_PLAY));
        CURRENCY_PAIRS_IDS.put("RDD_BTC", Integer.valueOf(169));
        CURRENCY_PAIRS_IDS.put("RED_LTC", Integer.valueOf(87));
        CURRENCY_PAIRS_IDS.put("RPC_BTC", Integer.valueOf(143));
        CURRENCY_PAIRS_IDS.put("RYC_BTC", Integer.valueOf(9));
        CURRENCY_PAIRS_IDS.put("RYC_LTC", Integer.valueOf(37));
        CURRENCY_PAIRS_IDS.put("SAT_BTC", Integer.valueOf(168));
        CURRENCY_PAIRS_IDS.put("SBC_BTC", Integer.valueOf(51));
        CURRENCY_PAIRS_IDS.put("SBC_LTC", Integer.valueOf(TransportMediator.FLAG_KEY_MEDIA_NEXT));
        CURRENCY_PAIRS_IDS.put("SMC_BTC", Integer.valueOf(158));
        CURRENCY_PAIRS_IDS.put("SPT_BTC", Integer.valueOf(81));
        CURRENCY_PAIRS_IDS.put("SRC_BTC", Integer.valueOf(88));
        CURRENCY_PAIRS_IDS.put("STR_BTC", Integer.valueOf(83));
        CURRENCY_PAIRS_IDS.put("SXC_BTC", Integer.valueOf(153));
        CURRENCY_PAIRS_IDS.put("SXC_LTC", Integer.valueOf(98));
        CURRENCY_PAIRS_IDS.put("TAG_BTC", Integer.valueOf(117));
        CURRENCY_PAIRS_IDS.put("TAK_BTC", Integer.valueOf(166));
        CURRENCY_PAIRS_IDS.put("TEK_BTC", Integer.valueOf(114));
        CURRENCY_PAIRS_IDS.put("TGC_BTC", Integer.valueOf(TransportMediator.KEYCODE_MEDIA_RECORD));
        CURRENCY_PAIRS_IDS.put("TIPS_LTC", Integer.valueOf(147));
        CURRENCY_PAIRS_IDS.put("TIX_LTC", Integer.valueOf(107));
        CURRENCY_PAIRS_IDS.put("TIX_XPM", Integer.valueOf(103));
        CURRENCY_PAIRS_IDS.put("TRC_BTC", Integer.valueOf(27));
        CURRENCY_PAIRS_IDS.put("UNO_BTC", Integer.valueOf(133));
        CURRENCY_PAIRS_IDS.put("UTC_BTC", Integer.valueOf(163));
        CURRENCY_PAIRS_IDS.put("VTC_BTC", Integer.valueOf(151));
        CURRENCY_PAIRS_IDS.put("WDC_BTC", Integer.valueOf(14));
        CURRENCY_PAIRS_IDS.put("WDC_LTC", Integer.valueOf(21));
        CURRENCY_PAIRS_IDS.put("XJO_BTC", Integer.valueOf(115));
        CURRENCY_PAIRS_IDS.put("XNC_LTC", Integer.valueOf(67));
        CURRENCY_PAIRS_IDS.put("XPM_BTC", Integer.valueOf(63));
        CURRENCY_PAIRS_IDS.put("XPM_LTC", Integer.valueOf(106));
        CURRENCY_PAIRS_IDS.put("YAC_BTC", Integer.valueOf(11));
        CURRENCY_PAIRS_IDS.put("YAC_LTC", Integer.valueOf(22));
        CURRENCY_PAIRS_IDS.put("YBC_BTC", Integer.valueOf(73));
        CURRENCY_PAIRS_IDS.put("ZCC_BTC", Integer.valueOf(140));
        CURRENCY_PAIRS_IDS.put("ZED_BTC", Integer.valueOf(170));
        CURRENCY_PAIRS_IDS.put("ZET_BTC", Integer.valueOf(85));
        CURRENCY_PAIRS_IDS.put("ZET_LTC", Integer.valueOf(TransportMediator.KEYCODE_MEDIA_PAUSE));
    }

    public Cryptsy() {
        super(TTS_NAME, TTS_NAME, null);
    }

    public String getUrl(int requestId, CheckerInfo checkerInfo) {
        if (checkerInfo.getCurrencyPairId() == null) {
            String pairString =
                String.format("%1$s_%2$s", checkerInfo.getCurrencyBase(), checkerInfo.getCurrencyCounter());
            if (CURRENCY_PAIRS_IDS.containsKey(pairString)) {
                return String.format(URL, String.valueOf(CURRENCY_PAIRS_IDS.get(pairString)));
            }
        }
        return String.format(URL, checkerInfo.getCurrencyPairId());
    }

    protected void parseTickerFromJsonObject(int requestId,
                                             JSONObject jsonObject,
                                             Ticker ticker,
                                             CheckerInfo checkerInfo) throws Exception {
        JSONObject dataJsonObject = jsonObject.getJSONObject("data");
        JSONObject daySummaryObject = dataJsonObject.getJSONObject("24hr");
        ticker.high = daySummaryObject.getDouble("price_high");
        ticker.low = daySummaryObject.getDouble("price_low");
        ticker.last = dataJsonObject.getJSONObject("last_trade").getDouble("price");
    }

    protected String parseError(int requestId, String responseString, CheckerInfo checkerInfo) throws Exception {
        if (checkerInfo.getCurrencyPairId() == null) {
            return "Perform sync and re-add this Checker";
        }
        return super.parseError(requestId, responseString, checkerInfo);
    }

    public String getCurrencyPairsUrl(int requestId) {
        return URL_CURRENCY_PAIRS;
    }

    protected void parseCurrencyPairsFromJsonObject(int requestId, JSONObject jsonObject, List<CurrencyPairInfo> pairs)
        throws Exception {
        JSONArray dataJsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < dataJsonArray.length(); i++) {
            JSONObject marketObject = dataJsonArray.getJSONObject(i);
            String currencyPair = marketObject.getString("label");
            if (currencyPair != null) {
                String[] currencies = currencyPair.split("/");
                if (!(currencies.length != 2 || currencies[0] == null || currencies[1] == null)) {
                    pairs.add(new CurrencyPairInfo(currencies[0], currencies[1], marketObject.getString("id")));
                }
            }
        }
    }
}
