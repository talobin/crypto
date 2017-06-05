package haivo.us.crypto.mechanoid.ops;

import android.content.Intent;
import android.os.Bundle;

public abstract class OperationServiceListener {
    public abstract void onOperationComplete(int i, OperationResult operationResult);

    public void onOperationStarting(int id, Intent intent, Bundle data) {
    }

    public void onOperationProgress(int id, Intent intent, int progress, Bundle data) {
    }

    public void onOperationAborted(int id, Intent intent, int reason, Bundle data) {
    }
}
