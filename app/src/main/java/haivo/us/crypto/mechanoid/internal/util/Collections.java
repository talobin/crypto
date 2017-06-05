package haivo.us.crypto.mechanoid.internal.util;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Collections {

    private static class SetFromMap<E> extends AbstractSet<E> implements Serializable {
        private static final long serialVersionUID = 2454657854757543876L;
        private transient Set<E> backingSet;
        private Map<E, Boolean> f2m;

        SetFromMap(Map<E, Boolean> map) {
            this.f2m = map;
            this.backingSet = map.keySet();
        }

        public boolean equals(Object object) {
            return this.backingSet.equals(object);
        }

        public int hashCode() {
            return this.backingSet.hashCode();
        }

        public boolean add(E object) {
            return this.f2m.put(object, Boolean.TRUE) == null;
        }

        public void clear() {
            this.f2m.clear();
        }

        public String toString() {
            return this.backingSet.toString();
        }

        public boolean contains(Object object) {
            return this.backingSet.contains(object);
        }

        public boolean containsAll(Collection<?> collection) {
            return this.backingSet.containsAll(collection);
        }

        public boolean isEmpty() {
            return this.f2m.isEmpty();
        }

        public boolean remove(Object object) {
            return this.f2m.remove(object) != null;
        }

        public boolean retainAll(Collection<?> collection) {
            return this.backingSet.retainAll(collection);
        }

        public Object[] toArray() {
            return this.backingSet.toArray();
        }

        public <T> T[] toArray(T[] contents) {
            return this.backingSet.toArray(contents);
        }

        public Iterator<E> iterator() {
            return this.backingSet.iterator();
        }

        public int size() {
            return this.f2m.size();
        }
    }

    public static <E> Set<E> newSetFromMap(Map<E, Boolean> map) {
        if (map.isEmpty()) {
            return new SetFromMap(map);
        }
        throw new IllegalArgumentException();
    }
}
