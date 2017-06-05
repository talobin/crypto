package haivo.us.crypto.content.migrations;

import android.database.sqlite.SQLiteDatabase;
import haivo.us.crypto.mechanoid.db.SQLiteMigration;

public class DefaultMaindbMigrationV1 extends SQLiteMigration {
    public void onBeforeUp(SQLiteDatabase db) {
    }

    public void up(SQLiteDatabase db) {
        db.execSQL(
            "create table checker ( _id integer primary key autoincrement, enabled boolean, marketKey text, currencySrc text, currencyDst text, frequency integer, notificationPriority integer, ttsEnabled boolean,  lastCheckTicker text, lastCheckPointTicker text ) ");
        db.execSQL(
            "create table alarm ( _id integer primary key autoincrement, checkerId integer, enabled boolean, type integer, value real, sound boolean, soundUri text, vibrate boolean, led boolean, ttsEnabled boolean ) ");
    }

    public void onAfterUp(SQLiteDatabase db) {
    }
}
