package haivo.us.crypto.content;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import haivo.us.crypto.mechanoid.Mechanoid;
import haivo.us.crypto.mechanoid.db.AbstractValuesBuilder;
import haivo.us.crypto.mechanoid.db.ActiveRecord;
import haivo.us.crypto.mechanoid.db.ActiveRecordFactory;
import haivo.us.crypto.mechanoid.util.Closeables;

public class AlarmRecord extends ActiveRecord implements Parcelable {
    public static final Parcelable.Creator<AlarmRecord> CREATOR;
    public static String[] PROJECTION;
    private static ActiveRecordFactory<AlarmRecord> sFactory;
    private long mCheckerId;
    private boolean mCheckerIdDirty;
    private boolean mEnabled;
    private boolean mEnabledDirty;
    private String mLastCheckPointTicker;
    private boolean mLastCheckPointTickerDirty;
    private boolean mLed;
    private boolean mLedDirty;
    private boolean mSound;
    private boolean mSoundDirty;
    private String mSoundUri;
    private boolean mSoundUriDirty;
    private boolean mTtsEnabled;
    private boolean mTtsEnabledDirty;
    private long mType;
    private boolean mTypeDirty;
    private double mValue;
    private boolean mValueDirty;
    private boolean mVibrate;
    private boolean mVibrateDirty;

    static {
        sFactory = new ActiveRecordFactory<AlarmRecord>() {

            @Override
            public AlarmRecord create(Cursor cursor) {
                return AlarmRecord.fromCursor(cursor);
            }

            @Override
            public String[] getProjection() {
                return AlarmRecord.PROJECTION;
            }
        };
        CREATOR = new Parcelable.Creator<AlarmRecord>() {

            public AlarmRecord createFromParcel(Parcel parcel) {
                return new AlarmRecord(parcel);
            }

            public AlarmRecord[] newArray(int n) {
                return new AlarmRecord[n];
            }
        };
        PROJECTION = new String[] {
            "_id",
            "checkerId",
            "enabled",
            "type",
            "value",
            "sound",
            "soundUri",
            "vibrate",
            "led",
            "ttsEnabled",
            "lastCheckPointTicker"
        };
    }

    public AlarmRecord() {
        super(MaindbContract.Alarm.CONTENT_URI);
    }

    /*
     * Enabled aggressive block sorting
     */
    private AlarmRecord(Parcel parcel) {
        super(MaindbContract.Alarm.CONTENT_URI);
        this.setId(parcel.readLong());
        this.mCheckerId = parcel.readLong();
        boolean bl = parcel.readInt() > 0;
        this.mEnabled = bl;
        this.mType = parcel.readLong();
        this.mValue = parcel.readDouble();
        bl = parcel.readInt() > 0;
        this.mSound = bl;
        this.mSoundUri = parcel.readString();
        bl = parcel.readInt() > 0;
        this.mVibrate = bl;
        bl = parcel.readInt() > 0;
        this.mLed = bl;
        bl = parcel.readInt() > 0;
        this.mTtsEnabled = bl;
        this.mLastCheckPointTicker = parcel.readString();
        boolean[] arrbl = new boolean[10];
        parcel.readBooleanArray(arrbl);
        this.mCheckerIdDirty = arrbl[0];
        this.mEnabledDirty = arrbl[1];
        this.mTypeDirty = arrbl[2];
        this.mValueDirty = arrbl[3];
        this.mSoundDirty = arrbl[4];
        this.mSoundUriDirty = arrbl[5];
        this.mVibrateDirty = arrbl[6];
        this.mLedDirty = arrbl[7];
        this.mTtsEnabledDirty = arrbl[8];
        this.mLastCheckPointTickerDirty = arrbl[9];
    }

    public static AlarmRecord fromBundle(Bundle bundle, String string2) {
        bundle.setClassLoader(AlarmRecord.class.getClassLoader());
        return (AlarmRecord) bundle.getParcelable(string2);
    }

    public static AlarmRecord fromCursor(Cursor cursor) {
        AlarmRecord alarmRecord = new AlarmRecord();
        alarmRecord.setPropertiesFromCursor(cursor);
        alarmRecord.makeDirty(false);
        return alarmRecord;
    }

    public static AlarmRecord get(long id) {
        Cursor c = null;
        try {
            c = Mechanoid.getContentResolver()
                         .query(MaindbContract.Alarm.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build(),
                                PROJECTION,
                                null,
                                null,
                                null);
            if (!c.moveToFirst()) {
                return null;
            }
            AlarmRecord fromCursor = fromCursor(c);
            Closeables.closeSilently(c);
            return fromCursor;
        } finally {
            Closeables.closeSilently(c);
        }
    }

    public static ActiveRecordFactory<AlarmRecord> getFactory() {
        return sFactory;
    }

    @Override
    protected String[] _getProjection() {
        return PROJECTION;
    }

    @Override
    protected AbstractValuesBuilder createBuilder() {
        MaindbContract.Alarm.Builder builder = MaindbContract.Alarm.newBuilder();
        if (this.mCheckerIdDirty) {
            builder.setCheckerId(this.mCheckerId);
        }
        if (this.mEnabledDirty) {
            builder.setEnabled(this.mEnabled);
        }
        if (this.mTypeDirty) {
            builder.setType(this.mType);
        }
        if (this.mValueDirty) {
            builder.setValue(this.mValue);
        }
        if (this.mSoundDirty) {
            builder.setSound(this.mSound);
        }
        if (this.mSoundUriDirty) {
            builder.setSoundUri(this.mSoundUri);
        }
        if (this.mVibrateDirty) {
            builder.setVibrate(this.mVibrate);
        }
        if (this.mLedDirty) {
            builder.setLed(this.mLed);
        }
        if (this.mTtsEnabledDirty) {
            builder.setTtsEnabled(this.mTtsEnabled);
        }
        if (this.mLastCheckPointTickerDirty) {
            builder.setLastCheckPointTicker(this.mLastCheckPointTicker);
        }
        return builder;
    }

    public int describeContents() {
        return 0;
    }

    public long getCheckerId() {
        return this.mCheckerId;
    }

    public boolean getEnabled() {
        return this.mEnabled;
    }

    public String getLastCheckPointTicker() {
        return this.mLastCheckPointTicker;
    }

    public boolean getLed() {
        return this.mLed;
    }

    public boolean getSound() {
        return this.mSound;
    }

    public String getSoundUri() {
        return this.mSoundUri;
    }

    public boolean getTtsEnabled() {
        return this.mTtsEnabled;
    }

    public long getType() {
        return this.mType;
    }

    public double getValue() {
        return this.mValue;
    }

    public boolean getVibrate() {
        return this.mVibrate;
    }

    @Override
    public void makeDirty(boolean bl) {
        this.mCheckerIdDirty = bl;
        this.mEnabledDirty = bl;
        this.mTypeDirty = bl;
        this.mValueDirty = bl;
        this.mSoundDirty = bl;
        this.mSoundUriDirty = bl;
        this.mVibrateDirty = bl;
        this.mLedDirty = bl;
        this.mTtsEnabledDirty = bl;
        this.mLastCheckPointTickerDirty = bl;
    }

    public void setCheckerId(long l) {
        this.mCheckerId = l;
        this.mCheckerIdDirty = true;
    }

    public void setEnabled(boolean bl) {
        this.mEnabled = bl;
        this.mEnabledDirty = true;
    }

    public void setLastCheckPointTicker(String string2) {
        this.mLastCheckPointTicker = string2;
        this.mLastCheckPointTickerDirty = true;
    }

    public void setLed(boolean bl) {
        this.mLed = bl;
        this.mLedDirty = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void setPropertiesFromCursor(Cursor cursor) {
        boolean bl = true;
        this.setId(cursor.getLong(0));
        this.setCheckerId(cursor.getLong(1));
        boolean bl2 = cursor.getInt(2) > 0;
        this.setEnabled(bl2);
        this.setType(cursor.getLong(3));
        this.setValue(cursor.getDouble(4));
        bl2 = cursor.getInt(5) > 0;
        this.setSound(bl2);
        this.setSoundUri(cursor.getString(6));
        bl2 = cursor.getInt(7) > 0;
        this.setVibrate(bl2);
        bl2 = cursor.getInt(8) > 0;
        this.setLed(bl2);
        bl2 = cursor.getInt(9) > 0 ? bl : false;
        this.setTtsEnabled(bl2);
        this.setLastCheckPointTicker(cursor.getString(10));
    }

    public void setSound(boolean bl) {
        this.mSound = bl;
        this.mSoundDirty = true;
    }

    public void setSoundUri(String string2) {
        this.mSoundUri = string2;
        this.mSoundUriDirty = true;
    }

    public void setTtsEnabled(boolean bl) {
        this.mTtsEnabled = bl;
        this.mTtsEnabledDirty = true;
    }

    public void setType(long l) {
        this.mType = l;
        this.mTypeDirty = true;
    }

    public void setValue(double d) {
        this.mValue = d;
        this.mValueDirty = true;
    }

    public void setVibrate(boolean bl) {
        this.mVibrate = bl;
        this.mVibrateDirty = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeLong(this.getId());
        parcel.writeLong(this.mCheckerId);
        n = this.mEnabled ? 1 : 0;
        parcel.writeInt(n);
        parcel.writeLong(this.mType);
        parcel.writeDouble(this.mValue);
        n = this.mSound ? 1 : 0;
        parcel.writeInt(n);
        parcel.writeString(this.mSoundUri);
        n = this.mVibrate ? 1 : 0;
        parcel.writeInt(n);
        n = this.mLed ? 1 : 0;
        parcel.writeInt(n);
        n = this.mTtsEnabled ? 1 : 0;
        parcel.writeInt(n);
        parcel.writeString(this.mLastCheckPointTicker);
        parcel.writeBooleanArray(new boolean[] {
            this.mCheckerIdDirty,
            this.mEnabledDirty,
            this.mTypeDirty,
            this.mValueDirty,
            this.mSoundDirty,
            this.mSoundUriDirty,
            this.mVibrateDirty,
            this.mLedDirty,
            this.mTtsEnabledDirty,
            this.mLastCheckPointTickerDirty
        });
    }

    public static interface Indices {
        public static final int CHECKER_ID = 1;
        public static final int ENABLED = 2;
        public static final int LAST_CHECK_POINT_TICKER = 10;
        public static final int LED = 8;
        public static final int SOUND = 5;
        public static final int SOUND_URI = 6;
        public static final int TTS_ENABLED = 9;
        public static final int TYPE = 3;
        public static final int VALUE = 4;
        public static final int VIBRATE = 7;
        public static final int _ID = 0;
    }
}
