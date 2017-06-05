package haivo.us.crypto.util;

import android.text.TextUtils;
import com.google.gson.Gson;
import haivo.us.crypto.model.Ticker;

public class TickerUtils {
    public static String toJson(Ticker ticker) {
        try {
            return new Gson().toJson((Object) ticker);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Ticker fromJson(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                return (Ticker) new Gson().fromJson(jsonString, Ticker.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
