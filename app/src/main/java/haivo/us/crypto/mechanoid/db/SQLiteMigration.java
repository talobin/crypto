package haivo.us.crypto.mechanoid.db;

import android.database.sqlite.SQLiteDatabase;

public abstract class SQLiteMigration {
    public abstract void onAfterUp(SQLiteDatabase sQLiteDatabase);

    public abstract void onBeforeUp(SQLiteDatabase sQLiteDatabase);

    public abstract void up(SQLiteDatabase sQLiteDatabase);
}
