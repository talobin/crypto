package haivo.us.crypto.mechanoid.ops;

@Deprecated
public interface OperationManagerCallbacks {
    void onOperationComplete(int i, OperationResult operationResult, boolean z);

    void onOperationPending(int i);
}
