package haivo.us.crypto.mechanoid.db;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.net.Uri.Builder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class MechanoidContentProvider extends ContentProvider {
    public static final String PARAM_GROUP_BY = "mechdb_group_by";
    public static final String PARAM_NOTIFY = "mechdb_notify";
    private String[] mContentTypes;
    private MechanoidSQLiteOpenHelper mOpenHelper;
    private UriMatcher mUriMatcher;

    protected abstract ContentProviderActions createActions(int i);

    protected abstract String[] createContentTypes();

    protected abstract MechanoidSQLiteOpenHelper createOpenHelper(Context context);

    protected abstract UriMatcher createUriMatcher();

    protected abstract Set<Uri> getRelatedUris(Uri uri);

    public MechanoidSQLiteOpenHelper getOpenHelper() {
        return this.mOpenHelper;
    }

    protected int matchUri(Uri uri) {
        return this.mUriMatcher.match(uri);
    }

    public boolean onCreate() {
        Context context = getContext();
        this.mUriMatcher = createUriMatcher();
        this.mContentTypes = createContentTypes();
        this.mOpenHelper = createOpenHelper(context);
        return true;
    }

    protected void tryNotifyChange(Uri uri) {
        boolean notify = true;
        String paramNotify = uri.getQueryParameter(PARAM_NOTIFY);
        if (paramNotify != null) {
            notify = Boolean.valueOf(paramNotify).booleanValue();
        }
        if (notify) {
            getContext().getContentResolver().notifyChange(uri, null);
            if (uri.getPathSegments().size() > 0) {
                Set<Uri> relatedUris = getRelatedUris(new Builder().scheme(uri.getScheme())
                                                                   .authority(uri.getAuthority())
                                                                   .appendPath((String) uri.getPathSegments().get(0))
                                                                   .build());
                if (relatedUris != null) {
                    for (Uri relatedUri : relatedUris) {
                        getContext().getContentResolver().notifyChange(relatedUri, null);
                    }
                }
            }
        }
    }

    protected void trySetNotificationUri(Cursor cursor, Uri uri) {
        if (cursor != null) {
            boolean notify = true;
            String paramNotify = uri.getQueryParameter(PARAM_NOTIFY);
            if (paramNotify != null) {
                notify = Boolean.valueOf(paramNotify).booleanValue();
            }
            if (notify) {
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
            }
        }
    }

    public String getType(Uri uri) {
        int match = matchUri(uri);
        if (match != -1) {
            return this.mContentTypes[match];
        }
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = matchUri(uri);
        if (match == -1) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        int affected = createActions(match).delete(this, uri, selection, selectionArgs);
        if (affected > 0) {
            tryNotifyChange(uri);
        }
        return affected;
    }

    public Uri insert(Uri uri, ContentValues values) {
        int match = matchUri(uri);
        if (match == -1) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Uri newUri = createActions(match).insert(this, uri, values);
        if (newUri != null) {
            tryNotifyChange(uri);
        }
        return newUri;
    }

    public int bulkInsert(Uri uri, ContentValues[] values) {
        int match = matchUri(uri);
        if (match == -1) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        int affected = createActions(match).bulkInsert(this, uri, values);
        if (affected > 0) {
            tryNotifyChange(uri);
        }
        return affected;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = matchUri(uri);
        if (match == -1) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Cursor cursor = createActions(match).query(this, uri, projection, selection, selectionArgs, sortOrder);
        trySetNotificationUri(cursor, uri);
        return cursor;
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = matchUri(uri);
        if (match == -1) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        int affected = createActions(match).update(this, uri, values, selection, selectionArgs);
        if (affected > 0) {
            tryNotifyChange(uri);
        }
        return affected;
    }

    public <T extends ActiveRecord> List<T> selectRecords(Uri uri, SQuery sQuery, String sortOrder) {
        int match = matchUri(uri);
        if (match != -1) {
            return createActions(match).selectRecords(this, uri, sQuery, sortOrder);
        }
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    public <T extends ActiveRecord> Map<String, T> selectRecordMap(Uri uri, SQuery sQuery, String keyColumnName) {
        int match = matchUri(uri);
        if (match != -1) {
            return createActions(match).selectRecordMap(this, uri, sQuery, keyColumnName);
        }
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
        throws OperationApplicationException {
        SQLiteDatabase db = getOpenHelper().getWritableDatabase();
        db.beginTransaction();
        try {
            int numOperations = operations.size();
            ContentProviderResult[] results = new ContentProviderResult[numOperations];
            for (int i = 0; i < numOperations; i++) {
                results[i] = ((ContentProviderOperation) operations.get(i)).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }
}
