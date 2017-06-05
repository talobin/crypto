package haivo.us.crypto.content;

import android.net.Uri;
import android.provider.BaseColumns;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import haivo.us.crypto.mechanoid.Mechanoid;
import haivo.us.crypto.mechanoid.db.AbstractValuesBuilder;

public class MaindbContract {
    private static final Uri BASE_CONTENT_URI;
    public static final String CONTENT_AUTHORITY;
    static Map<Uri, Set<Uri>> REFERENCING_VIEWS;

    public interface AlarmColumns {
        public static final String CHECKER_ID = "checkerId";
        public static final String ENABLED = "enabled";
        public static final String LAST_CHECK_POINT_TICKER = "lastCheckPointTicker";
        public static final String LED = "led";
        public static final String SOUND = "sound";
        public static final String SOUND_URI = "soundUri";
        public static final String TTS_ENABLED = "ttsEnabled";
        public static final String TYPE = "type";
        public static final String VALUE = "value";
        public static final String VIBRATE = "vibrate";
    }

    public interface CheckerColumns {
        public static final String CONTRACT_TYPE = "contractType";
        public static final String CURRENCY_DST = "currencyDst";
        public static final String CURRENCY_PAIR_ID = "currencyPairId";
        public static final String CURRENCY_SRC = "currencySrc";
        public static final String CURRENCY_SUBUNIT_DST = "currencySubunitDst";
        public static final String CURRENCY_SUBUNIT_SRC = "currencySubunitSrc";
        public static final String ENABLED = "enabled";
        public static final String ERROR_MSG = "errorMsg";
        public static final String FREQUENCY = "frequency";
        public static final String LAST_CHECK_DATE = "lastCheckDate";
        public static final String LAST_CHECK_POINT_TICKER = "lastCheckPointTicker";
        public static final String LAST_CHECK_TICKER = "lastCheckTicker";
        public static final String MARKET_KEY = "marketKey";
        public static final String NOTIFICATION_PRIORITY = "notificationPriority";
        public static final String PREVIOUS_CHECK_TICKER = "previousCheckTicker";
        public static final String SORT_ORDER = "sortOrder";
        public static final String TTS_ENABLED = "ttsEnabled";
    }

    public static class Alarm implements AlarmColumns, BaseColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maindb.haivo.us.crypto.alarm";
        public static final Uri CONTENT_URI;
        public static final String ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.maindb.haivo.us.crypto.alarm";
        static final Set<Uri> VIEW_URIS;

        public static class Builder extends AbstractValuesBuilder {
            private Builder() {
                super(Mechanoid.getApplicationContext(), Alarm.CONTENT_URI);
            }

            public Builder setCheckerId(long value) {
                this.mValues.put(AlarmColumns.CHECKER_ID, Long.valueOf(value));
                return this;
            }

            public Builder setEnabled(boolean value) {
                this.mValues.put(CheckerColumns.ENABLED, Boolean.valueOf(value));
                return this;
            }

            public Builder setType(long value) {
                this.mValues.put(AlarmColumns.TYPE, Long.valueOf(value));
                return this;
            }

            public Builder setValue(double value) {
                this.mValues.put(AlarmColumns.VALUE, Double.valueOf(value));
                return this;
            }

            public Builder setSound(boolean value) {
                this.mValues.put(AlarmColumns.SOUND, Boolean.valueOf(value));
                return this;
            }

            public Builder setSoundUri(String value) {
                this.mValues.put(AlarmColumns.SOUND_URI, value);
                return this;
            }

            public Builder setVibrate(boolean value) {
                this.mValues.put(AlarmColumns.VIBRATE, Boolean.valueOf(value));
                return this;
            }

            public Builder setLed(boolean value) {
                this.mValues.put(AlarmColumns.LED, Boolean.valueOf(value));
                return this;
            }

            public Builder setTtsEnabled(boolean value) {
                this.mValues.put(CheckerColumns.TTS_ENABLED, Boolean.valueOf(value));
                return this;
            }

            public Builder setLastCheckPointTicker(String value) {
                this.mValues.put(CheckerColumns.LAST_CHECK_POINT_TICKER, value);
                return this;
            }
        }

        static {
            CONTENT_URI =
                MaindbContract.BASE_CONTENT_URI.buildUpon().appendPath(AbstractMaindbOpenHelper.Sources.ALARM).build();
            VIEW_URIS = Collections.unmodifiableSet(new HashSet());
        }

        public static Uri buildUriWithId(long id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static int delete() {
            return Mechanoid.getContentResolver().delete(CONTENT_URI, null, null);
        }

        public static int delete(String where, String[] selectionArgs) {
            return Mechanoid.getContentResolver().delete(CONTENT_URI, where, selectionArgs);
        }

        public static Builder newBuilder() {
            return new Builder();
        }
    }

    public static class Checker implements CheckerColumns, BaseColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.maindb.checker";
        public static final Uri CONTENT_URI;
        public static final String ITEM_CONTENT_TYPE = "vnd.android.cursor.item/vnd.maindb.checker";
        static final Set<Uri> VIEW_URIS;

        public static class Builder extends AbstractValuesBuilder {
            private Builder() {
                super(Mechanoid.getApplicationContext(), Checker.CONTENT_URI);
            }

            public Builder setEnabled(boolean value) {
                this.mValues.put(CheckerColumns.ENABLED, Boolean.valueOf(value));
                return this;
            }

            public Builder setMarketKey(String value) {
                this.mValues.put(CheckerColumns.MARKET_KEY, value);
                return this;
            }

            public Builder setCurrencySrc(String value) {
                this.mValues.put(CheckerColumns.CURRENCY_SRC, value);
                return this;
            }

            public Builder setCurrencyDst(String value) {
                this.mValues.put(CheckerColumns.CURRENCY_DST, value);
                return this;
            }

            public Builder setFrequency(long value) {
                this.mValues.put(CheckerColumns.FREQUENCY, Long.valueOf(value));
                return this;
            }

            public Builder setNotificationPriority(long value) {
                this.mValues.put(CheckerColumns.NOTIFICATION_PRIORITY, Long.valueOf(value));
                return this;
            }

            public Builder setTtsEnabled(boolean value) {
                this.mValues.put(CheckerColumns.TTS_ENABLED, Boolean.valueOf(value));
                return this;
            }

            public Builder setLastCheckTicker(String value) {
                this.mValues.put(CheckerColumns.LAST_CHECK_TICKER, value);
                return this;
            }

            public Builder setLastCheckPointTicker(String value) {
                this.mValues.put(CheckerColumns.LAST_CHECK_POINT_TICKER, value);
                return this;
            }

            public Builder setPreviousCheckTicker(String value) {
                this.mValues.put(CheckerColumns.PREVIOUS_CHECK_TICKER, value);
                return this;
            }

            public Builder setLastCheckDate(long value) {
                this.mValues.put(CheckerColumns.LAST_CHECK_DATE, Long.valueOf(value));
                return this;
            }

            public Builder setSortOrder(long value) {
                this.mValues.put(CheckerColumns.SORT_ORDER, Long.valueOf(value));
                return this;
            }

            public Builder setCurrencySubunitSrc(long value) {
                this.mValues.put(CheckerColumns.CURRENCY_SUBUNIT_SRC, Long.valueOf(value));
                return this;
            }

            public Builder setCurrencySubunitDst(long value) {
                this.mValues.put(CheckerColumns.CURRENCY_SUBUNIT_DST, Long.valueOf(value));
                return this;
            }

            public Builder setErrorMsg(String value) {
                this.mValues.put(CheckerColumns.ERROR_MSG, value);
                return this;
            }

            public Builder setCurrencyPairId(String value) {
                this.mValues.put(CheckerColumns.CURRENCY_PAIR_ID, value);
                return this;
            }

            public Builder setContractType(long value) {
                this.mValues.put(CheckerColumns.CONTRACT_TYPE, Long.valueOf(value));
                return this;
            }
        }

        static {
            CONTENT_URI = MaindbContract.BASE_CONTENT_URI.buildUpon()
                                                         .appendPath(AbstractMaindbOpenHelper.Sources.CHECKER)
                                                         .build();
            VIEW_URIS = Collections.unmodifiableSet(new HashSet());
        }

        public static Uri buildUriWithId(long id) {
            return CONTENT_URI.buildUpon().appendPath(String.valueOf(id)).build();
        }

        public static int delete() {
            return Mechanoid.getContentResolver().delete(CONTENT_URI, null, null);
        }

        public static int delete(String where, String[] selectionArgs) {
            return Mechanoid.getContentResolver().delete(CONTENT_URI, where, selectionArgs);
        }

        public static Builder newBuilder() {
            return new Builder();
        }
    }

    static {
        CONTENT_AUTHORITY = initAuthority();
        BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        Map<Uri, Set<Uri>> map = new HashMap();
        map.put(Alarm.CONTENT_URI, Alarm.VIEW_URIS);
        map.put(Checker.CONTENT_URI, Checker.VIEW_URIS);
        REFERENCING_VIEWS = Collections.unmodifiableMap(map);
    }

    private static String initAuthority() {
        String authority = "haivo.us.crypto.content.maindb";
        try {
            authority = MaindbContract.class.getClassLoader()
                                            .loadClass("haivo.us.crypto.content.MaindbContentProviderAuthority")
                                            .getDeclaredField("CONTENT_AUTHORITY")
                                            .get(null)
                                            .toString();
        } catch (ClassNotFoundException e) {
        } catch (NoSuchFieldException e2) {
        } catch (IllegalArgumentException e3) {
        } catch (IllegalAccessException e4) {
        }
        return authority;
    }

    private MaindbContract() {
    }

    public static void deleteAll() {
        Alarm.delete();
        Checker.delete();
    }
}
