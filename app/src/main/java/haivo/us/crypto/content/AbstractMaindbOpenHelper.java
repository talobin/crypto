package haivo.us.crypto.content;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.support.v4.media.TransportMediator;
import haivo.us.crypto.content.migrations.DefaultMaindbMigrationV1;
import haivo.us.crypto.content.migrations.DefaultMaindbMigrationV2;
import haivo.us.crypto.content.migrations.DefaultMaindbMigrationV3;
import haivo.us.crypto.content.migrations.DefaultMaindbMigrationV4;
import haivo.us.crypto.content.migrations.DefaultMaindbMigrationV5;
import haivo.us.crypto.content.migrations.DefaultMaindbMigrationV6;
import haivo.us.crypto.content.migrations.DefaultMaindbMigrationV7;
import haivo.us.crypto.content.migrations.DefaultMaindbMigrationV8;
import haivo.us.crypto.content.migrations.DefaultMaindbMigrationV9;
import haivo.us.crypto.fragment.CheckersListFragment;
import haivo.us.crypto.fragment.MarketPickerListFragment;
import haivo.us.crypto.mechanoid.db.MechanoidSQLiteOpenHelper;
import haivo.us.crypto.mechanoid.db.SQLiteMigration;

public abstract class AbstractMaindbOpenHelper extends MechanoidSQLiteOpenHelper {
    private static final String DATABASE_NAME = "maindb.db";
    public static final int VERSION = 9;

    public interface Sources {
        public static final String ALARM = "alarm";
        public static final String CHECKER = "checker";
    }

    public AbstractMaindbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public AbstractMaindbOpenHelper(Context context, String name) {
        super(context, name, null, VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        applyMigrations(db, 0, VERSION);
    }

    protected SQLiteMigration createMigration(int version) {
        switch (version) {
            case MarketPickerListFragment.SORT_MODE_ALPHABETICALLY /*0*/:
                return createMaindbMigrationV1();
            case CheckersListFragment.SORT_MODE_CURRENCY /*1*/:
                return createMaindbMigrationV2();
            case CheckersListFragment.SORT_MODE_EXCHANGE /*2*/:
                return createMaindbMigrationV3();
            case WearableExtender.SIZE_MEDIUM /*3*/:
                return createMaindbMigrationV4();
            case AbstractMaindbContentProvider.NUM_URI_MATCHERS /*4*/:
                return createMaindbMigrationV5();
            case WearableExtender.SIZE_FULL_SCREEN /*5*/:
                return createMaindbMigrationV6();
            case 6 /*6*/:
                return createMaindbMigrationV7();
            case 7 /*7*/:
                return createMaindbMigrationV8();
            case TransportMediator.FLAG_KEY_MEDIA_PLAY_PAUSE /*8*/:
                return createMaindbMigrationV9();
            default:
                throw new IllegalStateException("No migration for version " + version);
        }
    }

    protected SQLiteMigration createMaindbMigrationV1() {
        return new DefaultMaindbMigrationV1();
    }

    protected SQLiteMigration createMaindbMigrationV2() {
        return new DefaultMaindbMigrationV2();
    }

    protected SQLiteMigration createMaindbMigrationV3() {
        return new DefaultMaindbMigrationV3();
    }

    protected SQLiteMigration createMaindbMigrationV4() {
        return new DefaultMaindbMigrationV4();
    }

    protected SQLiteMigration createMaindbMigrationV5() {
        return new DefaultMaindbMigrationV5();
    }

    protected SQLiteMigration createMaindbMigrationV6() {
        return new DefaultMaindbMigrationV6();
    }

    protected SQLiteMigration createMaindbMigrationV7() {
        return new DefaultMaindbMigrationV7();
    }

    protected SQLiteMigration createMaindbMigrationV8() {
        return new DefaultMaindbMigrationV8();
    }

    protected SQLiteMigration createMaindbMigrationV9() {
        return new DefaultMaindbMigrationV9();
    }
}
