package haivo.us.crypto.mechanoid.ops;

import android.content.Intent;

public abstract class OperationConfiguration {
    public abstract Operation createOperation();

    public abstract Intent findMatchOnConstraint(OperationServiceBridge operationServiceBridge, Intent intent);
}
