package haivo.us.crypto.mechanoid.ops;

import android.content.Intent;

public class Ops {
    private static OperationServiceBridge mBridge;

    static void init() {
        if (mBridge == null) {
            mBridge = new OperationServiceBridge();
        }
    }

    public static int execute(Intent intent) {
        return mBridge.execute(intent);
    }

    public static void bindListener(OperationServiceListener listener) {
        mBridge.bindListener(listener);
    }

    public static void unbindListener(OperationServiceListener listener) {
        mBridge.unbindListener(listener);
    }

    public static boolean isOperationPending(int id) {
        return mBridge.isOperationPending(id);
    }

    public static OperationLog getLog() {
        return mBridge.getLog();
    }

    public static void abort(int id, int reason) {
        mBridge.abort(id, reason);
    }

    public static void pause(boolean pause) {
        mBridge.pause(pause);
    }
}
