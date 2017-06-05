package haivo.us.crypto.mechanoid.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build.VERSION;

public abstract class MechanoidSQLiteOpenHelper extends SQLiteOpenHelper {
    protected abstract SQLiteMigration createMigration(int i);

    public MechanoidSQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public void onConfigure(SQLiteDatabase db) {
        if (!db.isReadOnly() && shouldEnableForeignKeyConstraints()) {
            if (VERSION.SDK_INT >= 16) {
                db.setForeignKeyConstraintsEnabled(true);
            } else if (VERSION.SDK_INT >= 8) {
                db.execSQL("PRAGMA foreign_keys=ON;");
            }
        }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        applyMigrations(db, oldVersion, newVersion);
    }

    protected void applyMigrations(SQLiteDatabase db, int from, int to) {
        for (int i = from; i < to; i++) {
            SQLiteMigration migration = createMigration(i);
            migration.onBeforeUp(db);
            migration.up(db);
            migration.onAfterUp(db);
        }
    }

    protected boolean shouldEnableForeignKeyConstraints() {
        return false;
    }
}
