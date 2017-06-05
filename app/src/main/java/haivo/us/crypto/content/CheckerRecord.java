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

public class CheckerRecord extends ActiveRecord implements Parcelable {
    public static final Parcelable.Creator<CheckerRecord> CREATOR;
    public static String[] PROJECTION;
    private static ActiveRecordFactory<CheckerRecord> sFactory;
    private long mContractType;
    private boolean mContractTypeDirty;
    private String mCurrencyDst;
    private boolean mCurrencyDstDirty;
    private String mCurrencyPairId;
    private boolean mCurrencyPairIdDirty;
    private String mCurrencySrc;
    private boolean mCurrencySrcDirty;
    private long mCurrencySubunitDst;
    private boolean mCurrencySubunitDstDirty;
    private long mCurrencySubunitSrc;
    private boolean mCurrencySubunitSrcDirty;
    private boolean mEnabled;
    private boolean mEnabledDirty;
    private String mErrorMsg;
    private boolean mErrorMsgDirty;
    private long mFrequency;
    private boolean mFrequencyDirty;
    private long mLastCheckDate;
    private boolean mLastCheckDateDirty;
    private String mLastCheckPointTicker;
    private boolean mLastCheckPointTickerDirty;
    private String mLastCheckTicker;
    private boolean mLastCheckTickerDirty;
    private String mMarketKey;
    private boolean mMarketKeyDirty;
    private long mNotificationPriority;
    private boolean mNotificationPriorityDirty;
    private String mPreviousCheckTicker;
    private boolean mPreviousCheckTickerDirty;
    private long mSortOrder;
    private boolean mSortOrderDirty;
    private boolean mTtsEnabled;
    private boolean mTtsEnabledDirty;

    static {
        sFactory = new ActiveRecordFactory<CheckerRecord>() {

            @Override
            public CheckerRecord create(Cursor cursor) {
                return CheckerRecord.fromCursor(cursor);
            }

            @Override
            public String[] getProjection() {
                return CheckerRecord.PROJECTION;
            }
        };
        CREATOR = new Parcelable.Creator<CheckerRecord>() {

            public CheckerRecord createFromParcel(Parcel parcel) {
                return new CheckerRecord(parcel);
            }

            public CheckerRecord[] newArray(int n) {
                return new CheckerRecord[n];
            }
        };
        PROJECTION = new String[] {
            "_id",
            "enabled",
            "marketKey",
            "currencySrc",
            "currencyDst",
            "frequency",
            "notificationPriority",
            "ttsEnabled",
            "lastCheckTicker",
            "lastCheckPointTicker",
            "previousCheckTicker",
            "lastCheckDate",
            "sortOrder",
            "currencySubunitSrc",
            "currencySubunitDst",
            "errorMsg",
            "currencyPairId",
            "contractType"
        };
    }

    public CheckerRecord() {
        super(MaindbContract.Checker.CONTENT_URI);
    }

    /*
     * Enabled aggressive block sorting
     */
    private CheckerRecord(Parcel parcel) {
        super(MaindbContract.Checker.CONTENT_URI);
        this.setId(parcel.readLong());
        boolean bl = parcel.readInt() > 0;
        this.mEnabled = bl;
        this.mMarketKey = parcel.readString();
        this.mCurrencySrc = parcel.readString();
        this.mCurrencyDst = parcel.readString();
        this.mFrequency = parcel.readLong();
        this.mNotificationPriority = parcel.readLong();
        bl = parcel.readInt() > 0;
        this.mTtsEnabled = bl;
        this.mLastCheckTicker = parcel.readString();
        this.mLastCheckPointTicker = parcel.readString();
        this.mPreviousCheckTicker = parcel.readString();
        this.mLastCheckDate = parcel.readLong();
        this.mSortOrder = parcel.readLong();
        this.mCurrencySubunitSrc = parcel.readLong();
        this.mCurrencySubunitDst = parcel.readLong();
        this.mErrorMsg = parcel.readString();
        this.mCurrencyPairId = parcel.readString();
        this.mContractType = parcel.readLong();
        boolean[] arrbl = new boolean[17];
        parcel.readBooleanArray(arrbl);
        this.mEnabledDirty = arrbl[0];
        this.mMarketKeyDirty = arrbl[1];
        this.mCurrencySrcDirty = arrbl[2];
        this.mCurrencyDstDirty = arrbl[3];
        this.mFrequencyDirty = arrbl[4];
        this.mNotificationPriorityDirty = arrbl[5];
        this.mTtsEnabledDirty = arrbl[6];
        this.mLastCheckTickerDirty = arrbl[7];
        this.mLastCheckPointTickerDirty = arrbl[8];
        this.mPreviousCheckTickerDirty = arrbl[9];
        this.mLastCheckDateDirty = arrbl[10];
        this.mSortOrderDirty = arrbl[11];
        this.mCurrencySubunitSrcDirty = arrbl[12];
        this.mCurrencySubunitDstDirty = arrbl[13];
        this.mErrorMsgDirty = arrbl[14];
        this.mCurrencyPairIdDirty = arrbl[15];
        this.mContractTypeDirty = arrbl[16];
    }

    public static CheckerRecord fromBundle(Bundle bundle, String string2) {
        bundle.setClassLoader(CheckerRecord.class.getClassLoader());
        return (CheckerRecord) bundle.getParcelable(string2);
    }

    public static CheckerRecord fromCursor(Cursor cursor) {
        CheckerRecord checkerRecord = new CheckerRecord();
        checkerRecord.setPropertiesFromCursor(cursor);
        checkerRecord.makeDirty(false);
        return checkerRecord;
    }

    public static CheckerRecord get(long id) {
        Cursor c = null;
        try {
            c = Mechanoid.getContentResolver()
                         .query(MaindbContract.Checker.CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build(),
                                PROJECTION,
                                null,
                                null,
                                null);
            if (!c.moveToFirst()) {
                return null;
            }
            CheckerRecord fromCursor = fromCursor(c);
            Closeables.closeSilently(c);
            return fromCursor;
        } finally {
            Closeables.closeSilently(c);
        }



    }

    public static ActiveRecordFactory<CheckerRecord> getFactory() {
        return sFactory;
    }

    @Override
    protected String[] _getProjection() {
        return PROJECTION;
    }

    @Override
    protected AbstractValuesBuilder createBuilder() {
        MaindbContract.Checker.Builder builder = MaindbContract.Checker.newBuilder();
        if (this.mEnabledDirty) {
            builder.setEnabled(this.mEnabled);
        }
        if (this.mMarketKeyDirty) {
            builder.setMarketKey(this.mMarketKey);
        }
        if (this.mCurrencySrcDirty) {
            builder.setCurrencySrc(this.mCurrencySrc);
        }
        if (this.mCurrencyDstDirty) {
            builder.setCurrencyDst(this.mCurrencyDst);
        }
        if (this.mFrequencyDirty) {
            builder.setFrequency(this.mFrequency);
        }
        if (this.mNotificationPriorityDirty) {
            builder.setNotificationPriority(this.mNotificationPriority);
        }
        if (this.mTtsEnabledDirty) {
            builder.setTtsEnabled(this.mTtsEnabled);
        }
        if (this.mLastCheckTickerDirty) {
            builder.setLastCheckTicker(this.mLastCheckTicker);
        }
        if (this.mLastCheckPointTickerDirty) {
            builder.setLastCheckPointTicker(this.mLastCheckPointTicker);
        }
        if (this.mPreviousCheckTickerDirty) {
            builder.setPreviousCheckTicker(this.mPreviousCheckTicker);
        }
        if (this.mLastCheckDateDirty) {
            builder.setLastCheckDate(this.mLastCheckDate);
        }
        if (this.mSortOrderDirty) {
            builder.setSortOrder(this.mSortOrder);
        }
        if (this.mCurrencySubunitSrcDirty) {
            builder.setCurrencySubunitSrc(this.mCurrencySubunitSrc);
        }
        if (this.mCurrencySubunitDstDirty) {
            builder.setCurrencySubunitDst(this.mCurrencySubunitDst);
        }
        if (this.mErrorMsgDirty) {
            builder.setErrorMsg(this.mErrorMsg);
        }
        if (this.mCurrencyPairIdDirty) {
            builder.setCurrencyPairId(this.mCurrencyPairId);
        }
        if (this.mContractTypeDirty) {
            builder.setContractType(this.mContractType);
        }
        return builder;
    }

    public int describeContents() {
        return 0;
    }

    public long getContractType() {
        return this.mContractType;
    }

    public String getCurrencyDst() {
        return this.mCurrencyDst;
    }

    public String getCurrencyPairId() {
        return this.mCurrencyPairId;
    }

    public String getCurrencySrc() {
        return this.mCurrencySrc;
    }

    public long getCurrencySubunitDst() {
        return this.mCurrencySubunitDst;
    }

    public long getCurrencySubunitSrc() {
        return this.mCurrencySubunitSrc;
    }

    public boolean getEnabled() {
        return this.mEnabled;
    }

    public String getErrorMsg() {
        return this.mErrorMsg;
    }

    public long getFrequency() {
        return this.mFrequency;
    }

    public long getLastCheckDate() {
        return this.mLastCheckDate;
    }

    public String getLastCheckPointTicker() {
        return this.mLastCheckPointTicker;
    }

    public String getLastCheckTicker() {
        return this.mLastCheckTicker;
    }

    public String getMarketKey() {
        return this.mMarketKey;
    }

    public long getNotificationPriority() {
        return this.mNotificationPriority;
    }

    public String getPreviousCheckTicker() {
        return this.mPreviousCheckTicker;
    }

    public long getSortOrder() {
        return this.mSortOrder;
    }

    public boolean getTtsEnabled() {
        return this.mTtsEnabled;
    }

    @Override
    public void makeDirty(boolean bl) {
        this.mEnabledDirty = bl;
        this.mMarketKeyDirty = bl;
        this.mCurrencySrcDirty = bl;
        this.mCurrencyDstDirty = bl;
        this.mFrequencyDirty = bl;
        this.mNotificationPriorityDirty = bl;
        this.mTtsEnabledDirty = bl;
        this.mLastCheckTickerDirty = bl;
        this.mLastCheckPointTickerDirty = bl;
        this.mPreviousCheckTickerDirty = bl;
        this.mLastCheckDateDirty = bl;
        this.mSortOrderDirty = bl;
        this.mCurrencySubunitSrcDirty = bl;
        this.mCurrencySubunitDstDirty = bl;
        this.mErrorMsgDirty = bl;
        this.mCurrencyPairIdDirty = bl;
        this.mContractTypeDirty = bl;
    }

    public void setContractType(long l) {
        this.mContractType = l;
        this.mContractTypeDirty = true;
    }

    public void setCurrencyDst(String string2) {
        this.mCurrencyDst = string2;
        this.mCurrencyDstDirty = true;
    }

    public void setCurrencyPairId(String string2) {
        this.mCurrencyPairId = string2;
        this.mCurrencyPairIdDirty = true;
    }

    public void setCurrencySrc(String string2) {
        this.mCurrencySrc = string2;
        this.mCurrencySrcDirty = true;
    }

    public void setCurrencySubunitDst(long l) {
        this.mCurrencySubunitDst = l;
        this.mCurrencySubunitDstDirty = true;
    }

    public void setCurrencySubunitSrc(long l) {
        this.mCurrencySubunitSrc = l;
        this.mCurrencySubunitSrcDirty = true;
    }

    public void setEnabled(boolean bl) {
        this.mEnabled = bl;
        this.mEnabledDirty = true;
    }

    public void setErrorMsg(String string2) {
        this.mErrorMsg = string2;
        this.mErrorMsgDirty = true;
    }

    public void setFrequency(long l) {
        this.mFrequency = l;
        this.mFrequencyDirty = true;
    }

    public void setLastCheckDate(long l) {
        this.mLastCheckDate = l;
        this.mLastCheckDateDirty = true;
    }

    public void setLastCheckPointTicker(String string2) {
        this.mLastCheckPointTicker = string2;
        this.mLastCheckPointTickerDirty = true;
    }

    public void setLastCheckTicker(String string2) {
        this.mLastCheckTicker = string2;
        this.mLastCheckTickerDirty = true;
    }

    public void setMarketKey(String string2) {
        this.mMarketKey = string2;
        this.mMarketKeyDirty = true;
    }

    public void setNotificationPriority(long l) {
        this.mNotificationPriority = l;
        this.mNotificationPriorityDirty = true;
    }

    public void setPreviousCheckTicker(String string2) {
        this.mPreviousCheckTicker = string2;
        this.mPreviousCheckTickerDirty = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void setPropertiesFromCursor(Cursor cursor) {
        boolean bl = true;
        this.setId(cursor.getLong(0));
        boolean bl2 = cursor.getInt(1) > 0;
        this.setEnabled(bl2);
        this.setMarketKey(cursor.getString(2));
        this.setCurrencySrc(cursor.getString(3));
        this.setCurrencyDst(cursor.getString(4));
        this.setFrequency(cursor.getLong(5));
        this.setNotificationPriority(cursor.getLong(6));
        bl2 = cursor.getInt(7) > 0 ? bl : false;
        this.setTtsEnabled(bl2);
        this.setLastCheckTicker(cursor.getString(8));
        this.setLastCheckPointTicker(cursor.getString(9));
        this.setPreviousCheckTicker(cursor.getString(10));
        this.setLastCheckDate(cursor.getLong(11));
        this.setSortOrder(cursor.getLong(12));
        this.setCurrencySubunitSrc(cursor.getLong(13));
        this.setCurrencySubunitDst(cursor.getLong(14));
        this.setErrorMsg(cursor.getString(15));
        this.setCurrencyPairId(cursor.getString(16));
        this.setContractType(cursor.getLong(17));
    }

    public void setSortOrder(long l) {
        this.mSortOrder = l;
        this.mSortOrderDirty = true;
    }

    public void setTtsEnabled(boolean bl) {
        this.mTtsEnabled = bl;
        this.mTtsEnabledDirty = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void writeToParcel(Parcel parcel, int n) {
        parcel.writeLong(this.getId());
        n = this.mEnabled ? 1 : 0;
        parcel.writeInt(n);
        parcel.writeString(this.mMarketKey);
        parcel.writeString(this.mCurrencySrc);
        parcel.writeString(this.mCurrencyDst);
        parcel.writeLong(this.mFrequency);
        parcel.writeLong(this.mNotificationPriority);
        n = this.mTtsEnabled ? 1 : 0;
        parcel.writeInt(n);
        parcel.writeString(this.mLastCheckTicker);
        parcel.writeString(this.mLastCheckPointTicker);
        parcel.writeString(this.mPreviousCheckTicker);
        parcel.writeLong(this.mLastCheckDate);
        parcel.writeLong(this.mSortOrder);
        parcel.writeLong(this.mCurrencySubunitSrc);
        parcel.writeLong(this.mCurrencySubunitDst);
        parcel.writeString(this.mErrorMsg);
        parcel.writeString(this.mCurrencyPairId);
        parcel.writeLong(this.mContractType);
        parcel.writeBooleanArray(new boolean[] {
            this.mEnabledDirty,
            this.mMarketKeyDirty,
            this.mCurrencySrcDirty,
            this.mCurrencyDstDirty,
            this.mFrequencyDirty,
            this.mNotificationPriorityDirty,
            this.mTtsEnabledDirty,
            this.mLastCheckTickerDirty,
            this.mLastCheckPointTickerDirty,
            this.mPreviousCheckTickerDirty,
            this.mLastCheckDateDirty,
            this.mSortOrderDirty,
            this.mCurrencySubunitSrcDirty,
            this.mCurrencySubunitDstDirty,
            this.mErrorMsgDirty,
            this.mCurrencyPairIdDirty,
            this.mContractTypeDirty
        });
    }

    public static interface Indices {
        public static final int CONTRACT_TYPE = 17;
        public static final int CURRENCY_DST = 4;
        public static final int CURRENCY_PAIR_ID = 16;
        public static final int CURRENCY_SRC = 3;
        public static final int CURRENCY_SUBUNIT_DST = 14;
        public static final int CURRENCY_SUBUNIT_SRC = 13;
        public static final int ENABLED = 1;
        public static final int ERROR_MSG = 15;
        public static final int FREQUENCY = 5;
        public static final int LAST_CHECK_DATE = 11;
        public static final int LAST_CHECK_POINT_TICKER = 9;
        public static final int LAST_CHECK_TICKER = 8;
        public static final int MARKET_KEY = 2;
        public static final int NOTIFICATION_PRIORITY = 6;
        public static final int PREVIOUS_CHECK_TICKER = 10;
        public static final int SORT_ORDER = 12;
        public static final int TTS_ENABLED = 7;
        public static final int _ID = 0;
    }
}
