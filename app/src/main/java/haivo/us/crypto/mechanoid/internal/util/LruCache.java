package haivo.us.crypto.mechanoid.internal.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = n;
        this.map = new LinkedHashMap(0, 0.75f, true);
    }

    private int safeSizeOf(K k, V v) {
        int n = this.sizeOf(k, v);
        if (n < 0) {
            throw new IllegalStateException("Negative size: " + k + "=" + v);
        }
        return n;
    }

    protected V create(K k) {
        return null;
    }

    public final int createCount() {
        synchronized (this) {
            int n = this.createCount;
            return n;
        }
    }

    protected void entryRemoved(boolean bl, K k, V v, V v2) {
    }

    public final void evictAll() {
        this.trimToSize(-1);
    }

    public final int evictionCount() {
        synchronized (this) {
            int n = this.evictionCount;
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final V get(K k) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        // MONITORENTER : this
        V v = this.map.get(k);
        if (v != null) {
            ++this.hitCount;
            // MONITOREXIT : this
            return v;
        }
        ++this.missCount;
        // MONITOREXIT : this
        v = this.create(k);
        if (v == null) {
            return null;
        }
        // MONITORENTER : this
        ++this.createCount;
        V v2 = this.map.put(k, v);
        if (v2 != null) {
            this.map.put(k, v2);
        } else {
            this.size += this.safeSizeOf(k, v);
        }
        // MONITOREXIT : this
        if (v2 != null) {
            this.entryRemoved(false, k, v, v2);
            return v2;
        }
        this.trimToSize(this.maxSize);
        return v;
    }

    public final int hitCount() {
        synchronized (this) {
            int n = this.hitCount;
            return n;
        }
    }

    public final int maxSize() {
        synchronized (this) {
            int n = this.maxSize;
            return n;
        }
    }

    public final int missCount() {
        synchronized (this) {
            int n = this.missCount;
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final V put(K k, V v) {
        if (k == null) throw new NullPointerException("key == null || value == null");
        if (v == null) {
            throw new NullPointerException("key == null || value == null");
        }
        // MONITORENTER : this
        ++this.putCount;
        this.size += this.safeSizeOf(k, v);
        V v2 = this.map.put(k, v);
        if (v2 != null) {
            this.size -= this.safeSizeOf(k, v2);
        }
        // MONITOREXIT : this
        if (v2 != null) {
            this.entryRemoved(false, k, v2, v);
        }
        this.trimToSize(this.maxSize);
        return v2;
    }

    public final int putCount() {
        synchronized (this) {
            int n = this.putCount;
            return n;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public final V remove(K k) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        // MONITORENTER : this
        V v = this.map.remove(k);
        if (v != null) {
            this.size -= this.safeSizeOf(k, v);
        }
        // MONITOREXIT : this
        if (v == null) return v;
        this.entryRemoved(false, k, v, null);
        return v;
    }

    public final int size() {
        synchronized (this) {
            int n = this.size;
            return n;
        }
    }

    protected int sizeOf(K k, V v) {
        return 1;
    }

    public final Map<K, V> snapshot() {
        synchronized (this) {
            LinkedHashMap<K, V> linkedHashMap = new LinkedHashMap<K, V>(this.map);
            return linkedHashMap;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final String toString() {
        int n = 0;
        synchronized (this) {
            int n2 = this.hitCount + this.missCount;
            if (n2 == 0) {
                return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]",
                                     this.maxSize,
                                     this.hitCount,
                                     this.missCount,
                                     n);
            }
            n = this.hitCount * 100 / n2;
            return String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]",
                                 this.maxSize,
                                 this.hitCount,
                                 this.missCount,
                                 n);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void trimToSize(int n) {
        do {
            K k;
            V v;
            synchronized (this) {
                if (this.size < 0 || this.map.isEmpty() && this.size != 0) {
                    throw new IllegalStateException(this.getClass().getName()
                                                        + ".sizeOf() is reporting inconsistent results!");
                }
                if (this.size <= n || this.map.isEmpty()) {
                    return;
                }
                Map.Entry entry2 = this.map.entrySet().iterator().next();
                k = (K) entry2.getKey();
                entry2 = (Map.Entry) entry2.getValue();
                v = this.map.remove(k);
                this.size -= this.safeSizeOf(k, (V) entry2);
                ++this.evictionCount;
            }
            this.entryRemoved(true, k, v, null);
        } while (true);
    }
}
