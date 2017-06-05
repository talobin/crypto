package haivo.us.crypto.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class IntentManager {
    public static final String ACTION_CHECKPOINT_REFRESH = "haivo.us.crypto.receiver.action.checkpoint_refresh";

    public static void sendLocalBroadcast(Context context, String action) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(action));
    }
}
