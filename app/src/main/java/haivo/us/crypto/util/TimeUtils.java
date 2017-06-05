package haivo.us.crypto.util;

public class TimeUtils {
    public static final long MILLIS_IN_DAY = 86400000;
    public static final long MILLIS_IN_HOUR = 3600000;
    public static final long MILLIS_IN_MINUTE = 60000;
    public static final long MILLIS_IN_SECOND = 1000;
    private static final long MILLIS_IN_YEAR = 31536000000L;
    public static final long NANOS_IN_MILLIS = 1000;

    public static long parseTimeToMillis(long time) {
        if (time < MILLIS_IN_YEAR) {
            return time * NANOS_IN_MILLIS;
        }
        if (time > 157680000000000L) {
            return time / NANOS_IN_MILLIS;
        }
        return time;
    }
}
