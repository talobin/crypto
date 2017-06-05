package haivo.us.crypto.mechanoid.db;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import haivo.us.crypto.mechanoid.Mechanoid;
import haivo.us.crypto.mechanoid.util.Closeables;

public abstract class ActiveRecord {
    protected final Uri mContentUri;
    private long mId;

    protected abstract String[] _getProjection();

    protected abstract AbstractValuesBuilder createBuilder();

    public abstract void makeDirty(boolean z);

    protected abstract void setPropertiesFromCursor(Cursor cursor);

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        if (this.mId != id) {
            makeDirty(true);
        }
        this.mId = id;
    }

    protected ActiveRecord(Uri contentUri) {
        this.mContentUri = contentUri;
    }

    public long save() {
        return save(true);
    }

    public long save(boolean notifyChange) {
        this.mId = this.mId > 0 ? update(notifyChange) : insert(notifyChange);
        return this.mId;
    }

    public boolean delete() {
        return delete(true);
    }

    public boolean delete(boolean notifyChange) {
        boolean result;
        if (Mechanoid.getContentResolver()
                     .delete(this.mContentUri.buildUpon()
                                             .appendPath(String.valueOf(this.mId))
                                             .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                                   String.valueOf(notifyChange))
                                             .build(), null, null) > 0) {
            result = true;
        } else {
            result = false;
        }
        makeDirty(false);
        return result;
    }

    public long insert() {
        return insert(true);
    }

    public long insert(boolean notifyChange) {
        this.mId = ContentUris.parseId(createBuilder().insert(notifyChange));
        makeDirty(false);
        return this.mId;
    }

    public long update() {
        return update(true);
    }

    public long update(boolean notifyChange) {
        createBuilder().update(this.mId, notifyChange);
        makeDirty(false);
        return this.mId;
    }

    public void reload() {
        if (this.mId != 0) {
            try {
                Cursor c = Mechanoid.getContentResolver()
                                    .query(this.mContentUri.buildUpon().appendPath(String.valueOf(this.mId)).build(),
                                           _getProjection(),
                                           null,
                                           null,
                                           null);
                if (c.moveToFirst()) {
                    setPropertiesFromCursor(c);
                    makeDirty(false);
                }
                Closeables.closeSilently(c);
            } catch (Throwable th) {
                Closeables.closeSilently(null);
            }
        }
    }
}
