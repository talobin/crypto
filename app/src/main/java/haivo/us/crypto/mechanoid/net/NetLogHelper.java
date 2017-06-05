package haivo.us.crypto.mechanoid.net;

import android.util.Log;
import java.util.List;
import java.util.Map;

public class NetLogHelper {
    private NetLogHelper() {
    }

    public static void logProperties(String tag, Map<String, List<String>> properties) {
        for (String key : properties.keySet()) {
            Log.d(tag, key + " " + ((List) properties.get(key)).toString());
        }
    }
}
