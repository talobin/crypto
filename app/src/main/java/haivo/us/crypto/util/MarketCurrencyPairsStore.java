package haivo.us.crypto.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import haivo.us.crypto.model.CurrencyPairsListWithDate;
import haivo.us.crypto.mechanoid.Mechanoid;

public class MarketCurrencyPairsStore {
    private static SharedPreferences getSharedPreferences(Context context) {
        if (context == null) {
            context = Mechanoid.getApplicationContext();
        } else {
            context.getApplicationContext();
        }
        return context.getSharedPreferences("MARKET_CURRENCIY_PAIRS", 0);
    }

    public static final void savePairsForMarket(Context context, String marketKey, CurrencyPairsListWithDate currencyPairsListWithDate) {
        try {
            savePairsStringForMarket(context, marketKey, new Gson().toJson((Object) currencyPairsListWithDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final void savePairsStringForMarket(Context context, String marketKey, String jsonString) {
        getSharedPreferences(context).edit().putString(marketKey, jsonString).commit();
    }

    public static final CurrencyPairsListWithDate getPairsForMarket(Context context, String marketKey) {
        try {
            return (CurrencyPairsListWithDate) new Gson().fromJson(getPairsStringForMarket(context, marketKey), CurrencyPairsListWithDate.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final String getPairsStringForMarket(Context context, String marketKey) {
        return getSharedPreferences(context).getString(marketKey, null);
    }
}
