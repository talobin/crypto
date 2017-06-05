package haivo.us.crypto.mechanoid.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import java.util.List;
import haivo.us.crypto.mechanoid.Mechanoid;

public abstract class BulkInsertHelper<T> {
    protected abstract ContentValues createValues(T t);

    public int insert(Uri contentUri, List<T> items) {
        ContentResolver resolver = Mechanoid.getContentResolver();
        if (items == null || items.size() == 0) {
            return 0;
        }
        ContentValues[] values = new ContentValues[items.size()];
        for (int i = 0; i < values.length; i++) {
            values[i] = createValues(items.get(i));
        }
        return resolver.bulkInsert(contentUri, values);
    }
}
