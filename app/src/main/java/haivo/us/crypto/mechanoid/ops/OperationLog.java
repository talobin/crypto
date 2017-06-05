package haivo.us.crypto.mechanoid.ops;

import haivo.us.crypto.mechanoid.internal.util.LruCache;

public class OperationLog extends LruCache<Integer, OperationResult> {
    public OperationLog(int size) {
        super(size);
    }
}
