package haivo.us.crypto.content.migrations;

import android.database.sqlite.SQLiteDatabase;
import haivo.us.crypto.mechanoid.db.SQLiteMigration;

public class DefaultMaindbMigrationV7 extends SQLiteMigration {
    public void onBeforeUp(SQLiteDatabase db) {
    }

    public void up(SQLiteDatabase db) {
        db.execSQL("alter table checker add column errorMsg text ");
    }

    public void onAfterUp(SQLiteDatabase db) {
    }
}
