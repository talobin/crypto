package haivo.us.crypto.mechanoid.ops;

public interface OperationExecutorCallbacks {
    boolean onOperationComplete(String str, OperationResult operationResult);

    void onOperationPending(String str);
}
