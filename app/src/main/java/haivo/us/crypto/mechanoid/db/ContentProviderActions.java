package haivo.us.crypto.mechanoid.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import java.util.List;
import java.util.Map;

public class ContentProviderActions {
    public int delete(MechanoidContentProvider provider, Uri uri, String selection, String[] selectionArgs) {
        return -1;
    }

    public Uri insert(MechanoidContentProvider provider, Uri uri, ContentValues values) {
        return null;
    }

    public Cursor query(MechanoidContentProvider provider,
                        Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        return null;
    }

    public int update(MechanoidContentProvider provider,
                      Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        return -1;
    }

    public int bulkInsert(MechanoidContentProvider provider, Uri uri, ContentValues[] values) {
        for (ContentValues insert : values) {
            insert(provider, uri, insert);
        }
        return 1;
    }

    public <T extends ActiveRecord> List<T> selectRecords(MechanoidContentProvider provider,
                                                          Uri uri,
                                                          SQuery sQuery,
                                                          String sortOrder) {
        return null;
    }

    public <T extends ActiveRecord> Map<String, T> selectRecordMap(MechanoidContentProvider provider,
                                                                   Uri uri,
                                                                   SQuery sQuery,
                                                                   String keyColumnName) {
        return null;
    }
}
