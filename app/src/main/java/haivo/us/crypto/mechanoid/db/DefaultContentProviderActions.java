package haivo.us.crypto.mechanoid.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import haivo.us.crypto.mechanoid.db.SQuery.Op;
import haivo.us.crypto.mechanoid.util.Closeables;

public class DefaultContentProviderActions extends ContentProviderActions {
    private boolean mForUrisWithId;
    private ActiveRecordFactory<?> mRecordFactory;
    private String mSource;

    public DefaultContentProviderActions(String source, boolean forUrisWithId) {
        this(source, forUrisWithId, null);
    }

    public <T extends ActiveRecord> DefaultContentProviderActions(String source,
                                                                  boolean forUrisWithId,
                                                                  ActiveRecordFactory<T> recordFactory) {
        this.mSource = source;
        this.mForUrisWithId = forUrisWithId;
        this.mRecordFactory = recordFactory;
    }

    public int delete(MechanoidContentProvider provider, Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = provider.getOpenHelper().getWritableDatabase();
        if (!this.mForUrisWithId) {
            return db.delete(this.mSource, selection, selectionArgs);
        }
        return SQuery.newQuery()
                     .expr("_id", Op.EQ, ContentUris.parseId(uri))
                     .append(selection, selectionArgs)
                     .delete(db, this.mSource);
    }

    public Uri insert(MechanoidContentProvider provider, Uri uri, ContentValues values) {
        if (this.mForUrisWithId) {
            return null;
        }
        long id = provider.getOpenHelper().getWritableDatabase().insertOrThrow(this.mSource, null, values);
        if (id > -1) {
            return ContentUris.withAppendedId(uri, id);
        }
        return null;
    }

    public int update(MechanoidContentProvider provider,
                      Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = provider.getOpenHelper().getWritableDatabase();
        if (!this.mForUrisWithId) {
            return db.update(this.mSource, values, selection, selectionArgs);
        }
        return SQuery.newQuery()
                     .expr("_id", Op.EQ, ContentUris.parseId(uri))
                     .append(selection, selectionArgs)
                     .update(db, this.mSource, values);
    }

    public Cursor query(MechanoidContentProvider provider,
                        Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = provider.getOpenHelper().getWritableDatabase();
        String groupBy = uri.getQueryParameter(MechanoidContentProvider.PARAM_GROUP_BY);
        if (this.mForUrisWithId) {
            String str;
            SQuery append =
                SQuery.newQuery().expr("_id", Op.EQ, ContentUris.parseId(uri)).append(selection, selectionArgs);
            String str2 = this.mSource;
            if (TextUtils.isEmpty(groupBy)) {
                str = null;
            } else {
                str = groupBy;
            }
            return append.query(db, str2, projection, sortOrder, str);
        }
        return db.query(this.mSource,
                        projection,
                        selection,
                        selectionArgs,
                        TextUtils.isEmpty(groupBy) ? null : groupBy,
                        null,
                        sortOrder);
    }

    public int bulkInsert(MechanoidContentProvider provider, Uri uri, ContentValues[] values) {
        SQLiteDatabase db = provider.getOpenHelper().getWritableDatabase();
        int success = -1;
        try {
            db.beginTransaction();
            for (ContentValues insertOrThrow : values) {
                db.insertOrThrow(this.mSource, null, insertOrThrow);
            }
            db.setTransactionSuccessful();
            success = 1;
        } finally {
            db.endTransaction();
        }
        return success;
    }

    public <T extends ActiveRecord> List<T> selectRecords(MechanoidContentProvider provider,
                                                          Uri uri,
                                                          SQuery sQuery,
                                                          String sortOrder) {
        String str = null;
        if (this.mRecordFactory == null) {
            return null;
        }
        String groupBy = uri.getQueryParameter(MechanoidContentProvider.PARAM_GROUP_BY);
        SQLiteDatabase db = provider.getOpenHelper().getWritableDatabase();
        Cursor c = null;
        List<T> items = new ArrayList();
        try {
            String str2 = this.mSource;
            String[] projection = this.mRecordFactory.getProjection();
            String sQuery2 = sQuery.toString();
            String[] argsArray = sQuery.getArgsArray();
            if (!TextUtils.isEmpty(groupBy)) {
                str = groupBy;
            }
            c = db.query(str2, projection, sQuery2, argsArray, str, null, sortOrder);
            while (c.moveToNext()) {
                items.add((T) this.mRecordFactory.create(c));
            }
            return items;
        } finally {
            Closeables.closeSilently(c);
        }
    }

    public <T extends ActiveRecord> Map<String, T> selectRecordMap(MechanoidContentProvider provider,
                                                                   Uri uri,
                                                                   SQuery sQuery,
                                                                   String keyColumnName) {
        if (this.mRecordFactory == null) {
            return null;
        }
        String groupBy = uri.getQueryParameter(MechanoidContentProvider.PARAM_GROUP_BY);
        SQLiteDatabase db = provider.getOpenHelper().getWritableDatabase();
        Cursor c = null;
        Map<String, T> items = new HashMap();
        try {
            c = db.query(this.mSource,
                         this.mRecordFactory.getProjection(),
                         sQuery.toString(),
                         sQuery.getArgsArray(),
                         TextUtils.isEmpty(groupBy) ? null : groupBy,
                         null,
                         null);
            int keyColumnIndex = c.getColumnIndexOrThrow(keyColumnName);
            while (c.moveToNext()) {
                items.put(c.getString(keyColumnIndex), (T) this.mRecordFactory.create(c));
            }
            return items;
        } finally {
            Closeables.closeSilently(c);
        }
    }
}
