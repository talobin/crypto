package haivo.us.crypto.content.migrations;

import android.database.sqlite.SQLiteDatabase;
import haivo.us.crypto.mechanoid.db.SQLiteMigration;

public class DefaultMaindbMigrationV6 extends SQLiteMigration {
    public void onBeforeUp(SQLiteDatabase db) {
    }

    public void up(SQLiteDatabase db) {
        db.execSQL("alter table checker add column currencySubunitSrc integer ");
        db.execSQL("alter table checker add column currencySubunitDst integer ");
    }

    public void onAfterUp(SQLiteDatabase db) {
    }
}
