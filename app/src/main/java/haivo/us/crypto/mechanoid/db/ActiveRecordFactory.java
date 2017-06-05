package haivo.us.crypto.mechanoid.db;

import android.database.Cursor;

public abstract class ActiveRecordFactory<T extends ActiveRecord> {
    public abstract T create(Cursor cursor);

    public abstract String[] getProjection();
}
