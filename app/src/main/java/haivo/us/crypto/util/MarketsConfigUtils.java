package haivo.us.crypto.util;

import haivo.us.crypto.config.MarketsConfig;
import java.util.ArrayList;
import haivo.us.crypto.model.Market;
import haivo.us.crypto.model.market.Unknown;

public class MarketsConfigUtils {
    private static final Market UNKNOWN;

    static {
        UNKNOWN = new Unknown();
    }

    public static Market getMarketById(int id) {
        synchronized (MarketsConfig.MARKETS) {
            if (id >= 0) {
                if (id < MarketsConfig.MARKETS.size()) {
                    Market market = (Market) new ArrayList(MarketsConfig.MARKETS.values()).get(id);
                    return market;
                }
            }
            return UNKNOWN;
        }
    }

    public static Market getMarketByKey(String key) {
        synchronized (MarketsConfig.MARKETS) {
            if (MarketsConfig.MARKETS.containsKey(key)) {
                Market market = (Market) MarketsConfig.MARKETS.get(key);
                return market;
            }
            return UNKNOWN;
        }
    }

    public static int getMarketIdByKey(String key) {
        int i = 0;
        for (Market market : MarketsConfig.MARKETS.values()) {
            if (market.key.equals(key)) {
                return i;
            }
            i++;
        }
        return 0;
    }
}
