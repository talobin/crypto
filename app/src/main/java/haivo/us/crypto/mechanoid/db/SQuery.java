package haivo.us.crypto.mechanoid.db;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import haivo.us.crypto.mechanoid.Mechanoid;
import haivo.us.crypto.mechanoid.util.Closeables;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQuery {
    private static final String AND = " AND ";
    public static final Literal CURRENT_DATE;
    public static final Literal CURRENT_TIME;
    public static final Literal CURRENT_TIMESTAMP;
    public static final Literal NULL;
    private static final String OR = " OR ";
    private List<String> mArgs;
    private StringBuilder mBuilder;
    private String mNextOp;
    private MechanoidContentProvider mProvider;

    public static final class Literal {
        protected final String value;

        public Literal(String value) {
            this.value = value;
        }
    }

    public interface Op {
        public static final String EQ = " = ";
        public static final String GT = " > ";
        public static final String GTEQ = " >= ";
        public static final String IS = " IS ";
        public static final String ISNOT = " IS NOT ";
        public static final String LIKE = " LIKE ";
        public static final String LT = " < ";
        public static final String LTEQ = " <= ";
        public static final String NEQ = " != ";
        public static final String REGEXP = " REGEXP ";
    }

    static {
        NULL = new Literal("NULL");
        CURRENT_TIME = new Literal("CURRENT_TIME");
        CURRENT_DATE = new Literal("CURRENT_DATE");
        CURRENT_TIMESTAMP = new Literal("CURRENT_TIMESTAMP");
    }

    public List<String> getArgs() {
        return this.mArgs;
    }

    public String[] getArgsArray() {
        return (String[]) this.mArgs.toArray(new String[this.mArgs.size()]);
    }

    private SQuery() {
        this.mArgs = new ArrayList();
        this.mNextOp = null;
        this.mBuilder = new StringBuilder();
    }

    public SQuery(MechanoidContentProvider provider) {
        this();
        this.mProvider = provider;
    }

    public static SQuery newQuery() {
        return new SQuery();
    }

    public static SQuery newQuery(MechanoidContentProvider provider) {
        return new SQuery(provider);
    }

    public SQuery expr(String column, String op, String arg) {
        ensureOp();
        this.mBuilder.append(column).append(op).append("?");
        this.mArgs.add(arg);
        this.mNextOp = null;
        return this;
    }

    public SQuery expr(String column, String op, Literal arg) {
        ensureOp();
        this.mBuilder.append(column).append(op).append(" ").append(arg.value);
        this.mNextOp = null;
        return this;
    }

    public SQuery exprIsNull(String column) {
        ensureOp();
        this.mBuilder.append(column).append(" ISNULL");
        this.mNextOp = null;
        return this;
    }

    public SQuery exprNotNull(String column) {
        ensureOp();
        this.mBuilder.append(column).append(" NOTNULL");
        this.mNextOp = null;
        return this;
    }

    public SQuery expr(SQuery query) {
        List<String> args = query.getArgs();
        ensureOp();
        this.mBuilder.append("(").append(query).append(")");
        if (args.size() > 0) {
            this.mArgs.addAll(args);
        }
        this.mNextOp = null;
        return this;
    }

    public SQuery expr(String column, String op, boolean arg) {
        return expr(column, op, arg ? "1" : "0");
    }

    public SQuery expr(String column, String op, int arg) {
        return expr(column, op, String.valueOf(arg));
    }

    public SQuery expr(String column, String op, long arg) {
        return expr(column, op, String.valueOf(arg));
    }

    public SQuery expr(String column, String op, float arg) {
        return expr(column, op, String.valueOf(arg));
    }

    public SQuery expr(String column, String op, double arg) {
        return expr(column, op, String.valueOf(arg));
    }

    public SQuery opt(String column, String op, String arg) {
        return arg == null ? this : expr(column, op, arg);
    }

    public SQuery opt(String column, String op, int arg) {
        return arg == 0 ? this : expr(column, op, String.valueOf(arg));
    }

    public SQuery opt(String column, String op, boolean arg) {
        return !arg ? this : expr(column, op, arg);
    }

    public SQuery opt(String column, String op, long arg) {
        return arg == 0 ? this : expr(column, op, arg);
    }

    public SQuery opt(String column, String op, float arg) {
        return arg == 0.0f ? this : expr(column, op, arg);
    }

    public SQuery opt(String column, String op, double arg) {
        return arg == 0.0d ? this : expr(column, op, arg);
    }

    public SQuery append(String query, String... args) {
        if (query != null && query.length() > 0) {
            ensureOp();
            this.mBuilder.append(query);
            if (args != null && args.length > 0) {
                for (String arg : args) {
                    this.mArgs.add(arg);
                }
            }
            this.mNextOp = null;
        }
        return this;
    }

    public SQuery and() {
        this.mNextOp = AND;
        return this;
    }

    public SQuery or() {
        this.mNextOp = OR;
        return this;
    }

    private void ensureOp() {
        if (this.mBuilder.length() != 0) {
            if (this.mNextOp == null) {
                this.mBuilder.append(AND);
                return;
            }
            this.mBuilder.append(this.mNextOp);
            this.mNextOp = null;
        }
    }

    public String toString() {
        return this.mBuilder.toString();
    }

    public Cursor query(SQLiteDatabase db, String table, String[] projection, String orderBy, String groupBy) {
        return db.query(table, projection, this.mBuilder.toString(), getArgsArray(), groupBy, null, orderBy);
    }

    public int firstInt(SQLiteDatabase db, String table, String column) {
        return firstInt(db, table, column, null);
    }

    public int firstInt(SQLiteDatabase db, String table, String column, String orderBy) {
        int value = 0;
        try {
            Cursor cursor = query(db, table, new String[] { column }, orderBy, null);
            if (cursor.moveToFirst()) {
                value = cursor.getInt(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public long firstLong(SQLiteDatabase db, String table, String column) {
        return firstLong(db, table, column, null);
    }

    public long firstLong(SQLiteDatabase db, String table, String column, String orderBy) {
        long value = 0;
        try {
            Cursor cursor = query(db, table, new String[] { column }, orderBy, null);
            if (cursor.moveToFirst()) {
                value = cursor.getLong(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public double firstDouble(SQLiteDatabase db, String table, String column) {
        return firstDouble(db, table, column, null);
    }

    public double firstDouble(SQLiteDatabase db, String table, String column, String orderBy) {
        double value = 0.0d;
        try {
            Cursor cursor = query(db, table, new String[] { column }, orderBy, null);
            if (cursor.moveToFirst()) {
                value = cursor.getDouble(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public float firstFloat(SQLiteDatabase db, String table, String column) {
        return firstFloat(db, table, column, null);
    }

    public float firstFloat(SQLiteDatabase db, String table, String column, String orderBy) {
        float value = 0.0f;
        try {
            Cursor cursor = query(db, table, new String[] { column }, orderBy, null);
            if (cursor.moveToFirst()) {
                value = cursor.getFloat(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public short firstShort(SQLiteDatabase db, String table, String column) {
        return firstShort(db, table, column, null);
    }

    public short firstShort(SQLiteDatabase db, String table, String column, String orderBy) {
        short value = (short) 0;
        try {
            Cursor cursor = query(db, table, new String[] { column }, orderBy, null);
            if (cursor.moveToFirst()) {
                value = cursor.getShort(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public byte[] firstBlob(SQLiteDatabase db, String table, String column) {
        return firstBlob(db, table, column, null);
    }

    public byte[] firstBlob(SQLiteDatabase db, String table, String column, String orderBy) {
        byte[] value = null;
        try {
            Cursor cursor = query(db, table, new String[] { column }, orderBy, null);
            if (cursor.moveToFirst()) {
                value = cursor.getBlob(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public boolean firstBoolean(SQLiteDatabase db, String table, String column) {
        return firstBoolean(db, table, column, null);
    }

    public boolean firstBoolean(SQLiteDatabase db, String table, String column, String orderBy) {
        return firstShort(db, table, column, orderBy) > (short) 0;
    }

    public String firstString(SQLiteDatabase db, String table, String column) {
        return firstString(db, table, column, null);
    }

    public String firstString(SQLiteDatabase db, String table, String column, String orderBy) {
        String value = null;
        try {
            Cursor cursor = query(db, table, new String[] { column }, orderBy, null);
            if (cursor.moveToFirst()) {
                value = cursor.getString(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public int update(SQLiteDatabase db, String table, ContentValues values) {
        return db.update(table, values, this.mBuilder.toString(), getArgsArray());
    }

    public int delete(SQLiteDatabase db, String table) {
        return db.delete(table, this.mBuilder.toString(), getArgsArray());
    }

    public <T extends ActiveRecord> List<T> select(Uri uri) {
        return getContentProvider(uri).selectRecords(uri, this, null);
    }

    public <T extends ActiveRecord> List<T> select(Uri uri, String sortOrder) {
        return getContentProvider(uri).selectRecords(uri, this, sortOrder);
    }

    public <T extends ActiveRecord> List<T> select(Uri uri, String sortOrder, String... groupBy) {
        uri = appendGroupByToUri(uri, groupBy);
        return getContentProvider(uri).selectRecords(uri, this, sortOrder);
    }

    private Uri appendGroupByToUri(Uri uri, String... groupBy) {
        if (groupBy == null || groupBy.length == 0) {
            return uri;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < groupBy.length; i++) {
            if (!TextUtils.isEmpty(groupBy[i])) {
                builder.append(groupBy[i]);
                if (i < groupBy.length - 1) {
                    builder.append(", ");
                }
            }
        }
        return uri.buildUpon()
                  .appendQueryParameter(MechanoidContentProvider.PARAM_GROUP_BY, builder.toString())
                  .build();
    }

    public <T extends ActiveRecord> Map<String, T> selectMap(Uri uri, String keyColumnName, String... groupBy) {
        uri = appendGroupByToUri(uri, groupBy);
        return getContentProvider(uri).selectRecordMap(uri, this, keyColumnName);
    }

    public <T extends ActiveRecord> T selectFirst(Uri uri, String sortOrder) {
        List<T> records = getContentProvider(uri).selectRecords(uri, this, sortOrder);
        if (records.size() > 0) {
            return (T) records.get(0);
        }
        return null;
    }

    public <T extends ActiveRecord> T selectFirst(Uri uri) {
        List<T> records = getContentProvider(uri).selectRecords(uri, this, null);
        if (records.size() > 0) {
            return (T) records.get(0);
        }
        return null;
    }

    private MechanoidContentProvider getContentProvider(Uri uri) {
        if (this.mProvider != null) {
            return this.mProvider;
        }
        return (MechanoidContentProvider) Mechanoid.getContentResolver()
                                                   .acquireContentProviderClient(uri)
                                                   .getLocalContentProvider();
    }

    public Cursor select(Uri uri, String[] projection, String sortOrder) {
        return Mechanoid.getContentResolver().query(uri, projection, toString(), getArgsArray(), sortOrder);
    }

    public Cursor select(Uri uri, String[] projection, String sortOrder, String... groupBy) {
        return Mechanoid.getContentResolver()
                        .query(appendGroupByToUri(uri, groupBy), projection, toString(), getArgsArray(), sortOrder);
    }

    public Cursor select(Uri uri, String[] projection, String sortOrder, boolean enableNotifications) {
        return Mechanoid.getContentResolver()
                        .query(uri.buildUpon()
                                  .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                        String.valueOf(enableNotifications))
                                  .build(), projection, toString(), getArgsArray(), sortOrder);
    }

    public Cursor select(Uri uri,
                         String[] projection,
                         String sortOrder,
                         boolean enableNotifications,
                         String... groupBy) {
        return Mechanoid.getContentResolver()
                        .query(appendGroupByToUri(uri.buildUpon()
                                                     .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                                           String.valueOf(enableNotifications))
                                                     .build(), groupBy),
                               projection,
                               toString(),
                               getArgsArray(),
                               sortOrder);
    }

    public Cursor select(Uri uri, String[] projection) {
        return select(uri, projection, (String) null);
    }

    public Cursor select(Uri uri, String[] projection, String... groupBy) {
        return select(uri, projection, null, groupBy);
    }

    public Cursor select(Uri uri, String[] projection, boolean enableNotifications) {
        return select(uri, projection, null, enableNotifications);
    }

    public Cursor select(Uri uri, String[] projection, boolean enableNotifications, String... groupBy) {
        return select(uri, projection, null, enableNotifications, groupBy);
    }

    @TargetApi(11)
    public CursorLoader createLoader(Uri uri, String[] projection, String sortOrder) {
        if (VERSION.SDK_INT < 11) {
            return null;
        }
        return new CursorLoader(Mechanoid.getApplicationContext(),
                                uri,
                                projection,
                                toString(),
                                getArgsArray(),
                                sortOrder);
    }

    @TargetApi(11)
    public CursorLoader createLoader(Uri uri, String[] projection, String sortOrder, String... groupBy) {
        if (VERSION.SDK_INT < 11) {
            return null;
        }
        return new CursorLoader(Mechanoid.getApplicationContext(),
                                appendGroupByToUri(uri, groupBy),
                                projection,
                                toString(),
                                getArgsArray(),
                                sortOrder);
    }

    @TargetApi(11)
    public CursorLoader createLoader(Uri uri, String[] projection, String sortOrder, boolean enableNotifications) {
        if (VERSION.SDK_INT < 11) {
            return null;
        }
        return new CursorLoader(Mechanoid.getApplicationContext(),
                                uri.buildUpon()
                                   .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                         String.valueOf(enableNotifications))
                                   .build(),
                                projection,
                                toString(),
                                getArgsArray(),
                                sortOrder);
    }

    @TargetApi(11)
    public CursorLoader createLoader(Uri uri,
                                     String[] projection,
                                     String sortOrder,
                                     boolean enableNotifications,
                                     String... groupBy) {
        if (VERSION.SDK_INT < 11) {
            return null;
        }
        return new CursorLoader(Mechanoid.getApplicationContext(),
                                appendGroupByToUri(uri.buildUpon()
                                                      .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                                            String.valueOf(enableNotifications))
                                                      .build(), groupBy),
                                projection,
                                toString(),
                                getArgsArray(),
                                sortOrder);
    }

    @TargetApi(11)
    public CursorLoader createLoader(Uri uri, String[] projection) {
        if (VERSION.SDK_INT < 11) {
            return null;
        }
        return new CursorLoader(Mechanoid.getApplicationContext(), uri, projection, toString(), getArgsArray(), null);
    }

    @TargetApi(11)
    public CursorLoader createLoader(Uri uri, String[] projection, boolean enableNotifications, String... groupBy) {
        if (VERSION.SDK_INT < 11) {
            return null;
        }
        return new CursorLoader(Mechanoid.getApplicationContext(),
                                appendGroupByToUri(uri.buildUpon()
                                                      .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                                            String.valueOf(enableNotifications))
                                                      .build(), groupBy),
                                projection,
                                toString(),
                                getArgsArray(),
                                null);
    }

    public android.support.v4.content.CursorLoader createSupportLoader(Uri uri, String[] projection, String sortOrder) {
        return new android.support.v4.content.CursorLoader(Mechanoid.getApplicationContext(),
                                                           uri,
                                                           projection,
                                                           toString(),
                                                           getArgsArray(),
                                                           sortOrder);
    }

    public android.support.v4.content.CursorLoader createSupportLoader(Uri uri,
                                                                       String[] projection,
                                                                       String sortOrder,
                                                                       String... groupBy) {
        return new android.support.v4.content.CursorLoader(Mechanoid.getApplicationContext(),
                                                           appendGroupByToUri(uri, groupBy),
                                                           projection,
                                                           toString(),
                                                           getArgsArray(),
                                                           sortOrder);
    }

    public android.support.v4.content.CursorLoader createSupportLoader(Uri uri,
                                                                       String[] projection,
                                                                       String sortOrder,
                                                                       boolean enableNotifications) {
        return new android.support.v4.content.CursorLoader(Mechanoid.getApplicationContext(),
                                                           uri.buildUpon()
                                                              .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                                                    String.valueOf(enableNotifications))
                                                              .build(),
                                                           projection,
                                                           toString(),
                                                           getArgsArray(),
                                                           sortOrder);
    }

    public android.support.v4.content.CursorLoader createSupportLoader(Uri uri,
                                                                       String[] projection,
                                                                       String sortOrder,
                                                                       boolean enableNotifications,
                                                                       String... groupBy) {
        return new android.support.v4.content.CursorLoader(Mechanoid.getApplicationContext(),
                                                           appendGroupByToUri(uri.buildUpon()
                                                                                 .appendQueryParameter(
                                                                                     MechanoidContentProvider.PARAM_NOTIFY,
                                                                                     String.valueOf(enableNotifications))
                                                                                 .build(), groupBy),
                                                           projection,
                                                           toString(),
                                                           getArgsArray(),
                                                           sortOrder);
    }

    public android.support.v4.content.CursorLoader createSupportLoader(Uri uri, String[] projection) {
        return new android.support.v4.content.CursorLoader(Mechanoid.getApplicationContext(),
                                                           uri,
                                                           projection,
                                                           toString(),
                                                           getArgsArray(),
                                                           null);
    }

    public android.support.v4.content.CursorLoader createSupportLoader(Uri uri,
                                                                       String[] projection,
                                                                       boolean enableNotifications) {
        return new android.support.v4.content.CursorLoader(Mechanoid.getApplicationContext(),
                                                           uri.buildUpon()
                                                              .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                                                    String.valueOf(enableNotifications))
                                                              .build(),
                                                           projection,
                                                           toString(),
                                                           getArgsArray(),
                                                           null);
    }

    public android.support.v4.content.CursorLoader createSupportLoader(Uri uri,
                                                                       String[] projection,
                                                                       boolean enableNotifications,
                                                                       String... groupBy) {
        return new android.support.v4.content.CursorLoader(Mechanoid.getApplicationContext(),
                                                           appendGroupByToUri(uri.buildUpon()
                                                                                 .appendQueryParameter(
                                                                                     MechanoidContentProvider.PARAM_NOTIFY,
                                                                                     String.valueOf(enableNotifications))
                                                                                 .build(), groupBy),
                                                           projection,
                                                           toString(),
                                                           getArgsArray(),
                                                           null);
    }

    public int firstInt(Uri uri, String column) {
        return firstInt(uri, column, null);
    }

    public int firstInt(Uri uri, String column, String orderBy) {
        int value = 0;
        try {
            Cursor cursor = select(uri, new String[] { column }, orderBy, false);
            if (cursor.moveToFirst()) {
                value = cursor.getInt(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public long firstLong(Uri uri, String column) {
        return firstLong(uri, column, null);
    }

    public long firstLong(Uri uri, String column, String orderBy) {
        long value = 0;
        try {
            Cursor cursor = select(uri, new String[] { column }, orderBy, false);
            if (cursor.moveToFirst()) {
                value = cursor.getLong(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public double firstDouble(Uri uri, String column) {
        return firstDouble(uri, column, null);
    }

    public double firstDouble(Uri uri, String column, String orderBy) {
        double value = 0.0d;
        try {
            Cursor cursor = select(uri, new String[] { column }, orderBy, false);
            if (cursor.moveToFirst()) {
                value = cursor.getDouble(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public float firstFloat(Uri uri, String column) {
        return firstFloat(uri, column, null);
    }

    public float firstFloat(Uri uri, String column, String orderBy) {
        float value = 0.0f;
        try {
            Cursor cursor = select(uri, new String[] { column }, orderBy, false);
            if (cursor.moveToFirst()) {
                value = cursor.getFloat(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public short firstShort(Uri uri, String column) {
        return firstShort(uri, column, null);
    }

    public short firstShort(Uri uri, String column, String orderBy) {
        short value = (short) 0;
        try {
            Cursor cursor = select(uri, new String[] { column }, orderBy, false);
            if (cursor.moveToFirst()) {
                value = cursor.getShort(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public byte[] firstBlob(Uri uri, String column) {
        return firstBlob(uri, column, null);
    }

    public byte[] firstBlob(Uri uri, String column, String orderBy) {
        byte[] value = null;
        try {
            Cursor cursor = select(uri, new String[] { column }, orderBy, false);
            if (cursor.moveToFirst()) {
                value = cursor.getBlob(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public boolean firstBoolean(Uri uri, String column) {
        return firstBoolean(uri, column, null);
    }

    public boolean firstBoolean(Uri uri, String column, String orderBy) {
        return firstShort(uri, column, orderBy) > (short) 0;
    }

    public String firstString(Uri uri, String column) {
        return firstString(uri, column, null);
    }

    public String firstString(Uri uri, String column, String orderBy) {
        String value = null;
        try {
            Cursor cursor = select(uri, new String[] { column }, orderBy, false);
            if (cursor.moveToFirst()) {
                value = cursor.getString(0);
            }
            Closeables.closeSilently(cursor);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return value;
    }

    public int[] selectIntArray(Uri uri, String column) {
        return selectIntArray(uri, column, null);
    }

    public int[] selectIntArray(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        int[] array = new int[1];
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            array = new int[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                array[i] = cursor.getInt(0);
            }
        } finally {
            Closeables.closeSilently(cursor);
        }
        return array;
    }

    public long[] selectLongArray(Uri uri, String column) {
        return selectLongArray(uri, column, null);
    }

    public long[] selectLongArray(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        long[] array = new long[1];
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            array = new long[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                array[i] = cursor.getLong(0);
            }
        } finally {
            Closeables.closeSilently(cursor);
        }
        return array;
    }

    public double[] selectDoubleArray(Uri uri, String column) {
        return selectDoubleArray(uri, column, null);
    }

    public double[] selectDoubleArray(Uri uri, String column, String orderBy) {
        double[] array = new double[1];
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            array = new double[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                array[i] = cursor.getDouble(0);
            }
        } finally {
            Closeables.closeSilently(cursor);
        }
        return array;
    }

    public float[] selectFloatArray(Uri uri, String column) {
        return selectFloatArray(uri, column, null);
    }

    public float[] selectFloatArray(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            float[] array = new float[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                array[i] = cursor.getFloat(0);
            }
            return array;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public short[] selectShortArray(Uri uri, String column) {
        return selectShortArray(uri, column, null);
    }

    public short[] selectShortArray(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            short[] array = new short[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                array[i] = cursor.getShort(0);
            }
            return array;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public byte[][] selectBlobArray(Uri uri, String column) {
        return selectBlobArray(uri, column, null);
    }

    public byte[][] selectBlobArray(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            byte[][] array = new byte[cursor.getCount()][];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                array[i] = cursor.getBlob(0);
            }
            return array;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public boolean[] selectBooleanArray(Uri uri, String column) {
        return selectBooleanArray(uri, column, null);
    }

    public boolean[] selectBooleanArray(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            boolean[] array = new boolean[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                boolean z;
                cursor.moveToNext();
                if (cursor.getLong(0) > 0) {
                    z = true;
                } else {
                    z = false;
                }
                array[i] = z;
            }
            return array;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public String[] selectStringArray(Uri uri, String column) {
        return selectStringArray(uri, column, null);
    }

    public String[] selectStringArray(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            String[] array = new String[cursor.getCount()];
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                array[i] = cursor.getString(0);
            }
            return array;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public List<Integer> selectIntegerList(Uri uri, String column) {
        return selectIntegerList(uri, column, null);
    }

    public List<Integer> selectIntegerList(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            List<Integer> list = new ArrayList(cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                list.add(Integer.valueOf(cursor.getInt(0)));
            }
            return list;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public List<Long> selectLongList(Uri uri, String column) {
        return selectLongList(uri, column, null);
    }

    public List<Long> selectLongList(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            List<Long> list = new ArrayList<>();
            if (cursor != null) {
                list = new ArrayList(cursor.getCount());
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToNext();
                    list.add(Long.valueOf(cursor.getLong(0)));
                }
            }
            return list;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public List<Double> selectDoubleList(Uri uri, String column) {
        return selectDoubleList(uri, column, null);
    }

    public List<Double> selectDoubleList(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            List<Double> list = new ArrayList(cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                list.add(Double.valueOf(cursor.getDouble(0)));
            }
            return list;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public List<Float> selectFloatList(Uri uri, String column) {
        return selectFloatList(uri, column, null);
    }

    public List<Float> selectFloatList(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            List<Float> list = new ArrayList(cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                list.add(Float.valueOf(cursor.getFloat(0)));
            }
            return list;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public List<Short> selectShortList(Uri uri, String column) {
        return selectShortList(uri, column, null);
    }

    public List<Short> selectShortList(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            List<Short> list = new ArrayList(cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                list.add(Short.valueOf(cursor.getShort(0)));
            }
            return list;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public List<byte[]> selectBlobList(Uri uri, String column) {
        return selectBlobList(uri, column, null);
    }

    public List<byte[]> selectBlobList(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            List<byte[]> list = new ArrayList(cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                list.add(cursor.getBlob(0));
            }
            return list;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public List<Boolean> selectBooleanList(Uri uri, String column) {
        return selectBooleanList(uri, column, null);
    }

    public List<Boolean> selectBooleanList(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            List<Boolean> list = new ArrayList(cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                boolean z;
                cursor.moveToNext();
                if (cursor.getLong(0) > 0) {
                    z = true;
                } else {
                    z = false;
                }
                list.add(Boolean.valueOf(z));
            }
            return list;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public List<String> selectStringList(Uri uri, String column) {
        return selectStringList(uri, column, null);
    }

    public List<String> selectStringList(Uri uri, String column, String orderBy) {
        Cursor cursor = null;
        try {
            cursor = select(uri, new String[] { column }, orderBy, false);
            List<String> list = new ArrayList(cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                list.add(cursor.getString(0));
            }
            return list;
        } finally {
            Closeables.closeSilently(cursor);
        }
    }

    public int update(Uri uri, ContentValues values) {
        return Mechanoid.getContentResolver().update(uri, values, toString(), getArgsArray());
    }

    public int update(Uri uri, ContentValues values, boolean notifyChange) {
        return Mechanoid.getContentResolver()
                        .update(uri.buildUpon()
                                   .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                         String.valueOf(notifyChange))
                                   .build(), values, toString(), getArgsArray());
    }

    public int delete(Uri uri) {
        return Mechanoid.getContentResolver().delete(uri, toString(), getArgsArray());
    }

    public int delete(Uri uri, boolean notifyChange) {
        return Mechanoid.getContentResolver()
                        .delete(uri.buildUpon()
                                   .appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY,
                                                         String.valueOf(notifyChange))
                                   .build(), toString(), getArgsArray());
    }

    public int count(Uri uri) {
        int count = 0;
        try {
            Uri build = uri.buildUpon().appendQueryParameter(MechanoidContentProvider.PARAM_NOTIFY, "false").build();
            Cursor c = Mechanoid.getContentResolver()
                                .query(build, new String[] { "count(*)" }, toString(), getArgsArray(), null);

            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            Closeables.closeSilently(c);
        } catch (Throwable th) {
            Closeables.closeSilently(null);
        }
        return count;
    }

    public boolean exists(Uri uri) {
        return count(uri) > 0;
    }
}
