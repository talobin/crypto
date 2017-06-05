package haivo.us.crypto.mechanoid.db;

import android.content.ContentProviderOperation;
import android.content.ContentProviderOperation.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public abstract class AbstractValuesBuilder {
    private ContentResolver mContentResolver;
    private Uri mContentUri;
    protected ContentValues mValues;

    protected AbstractValuesBuilder(Context context, Uri contentUri) {
        this.mValues = new ContentValues();
        this.mContentResolver = context.getContentResolver();
        this.mContentUri = contentUri;
    }

    public Uri insert() {
        return this.mContentResolver.insert(this.mContentUri, this.mValues);
    }

    public Uri insert(boolean notifyChange) {
        return this.mContentResolver.insert(this.mContentUri.buildUpon()
                                                            .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                                                  String.valueOf(notifyChange))
                                                            .build(), this.mValues);
    }

    public int update(SQuery query) {
        return this.mContentResolver.update(this.mContentUri, this.mValues, query.toString(), query.getArgsArray());
    }

    public int update(SQuery query, boolean notifyChange) {
        return this.mContentResolver.update(this.mContentUri.buildUpon()
                                                            .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                                                  String.valueOf(notifyChange))
                                                            .build(),
                                            this.mValues,
                                            query.toString(),
                                            query.getArgsArray());
    }

    public int update(long id) {
        return this.mContentResolver.update(this.mContentUri.buildUpon().appendPath(String.valueOf(id)).build(),
                                            this.mValues,
                                            null,
                                            null);
    }

    public int update(long id, boolean notifyChange) {
        return this.mContentResolver.update(this.mContentUri.buildUpon()
                                                            .appendPath(String.valueOf(id))
                                                            .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                                                  String.valueOf(notifyChange))
                                                            .build(), this.mValues, null, null);
    }

    public ContentValues getValues() {
        return this.mValues;
    }

    public Builder toInsertOperationBuilder() {
        return ContentProviderOperation.newInsert(this.mContentUri).withValues(this.mValues);
    }

    public Builder toUpdateOperationBuilder() {
        return ContentProviderOperation.newUpdate(this.mContentUri).withValues(this.mValues);
    }

    public Builder toDeleteOperationBuilder() {
        return ContentProviderOperation.newDelete(this.mContentUri);
    }

    public Builder toAssertQueryOperationBuilder() {
        return ContentProviderOperation.newAssertQuery(this.mContentUri).withValues(this.mValues);
    }
}
