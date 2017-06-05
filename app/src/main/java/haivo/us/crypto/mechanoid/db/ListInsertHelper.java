package haivo.us.crypto.mechanoid.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import java.util.List;
import haivo.us.crypto.mechanoid.Mechanoid;

public abstract class ListInsertHelper<T> {
    protected abstract ContentValues createValues(T t);

    protected boolean onBeforeInsert(ContentResolver resolver, T t) {
        return true;
    }

    protected void onAfterInsert(Uri uri, T t) {
    }

    public void insert(Uri contentUri, List<T> items) {
        ContentResolver resolver = Mechanoid.getContentResolver();
        if (items != null && items.size() != 0) {
            for (int i = 0; i < items.size(); i++) {
                T item = items.get(i);
                ContentValues values = createValues(item);
                if (onBeforeInsert(resolver, item)) {
                    onAfterInsert(resolver.insert(contentUri, values), item);
                }
            }
        }
    }
}
