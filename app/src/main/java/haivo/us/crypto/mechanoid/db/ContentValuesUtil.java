package haivo.us.crypto.mechanoid.db;

import android.content.ContentValues;
import java.util.Map;

public class ContentValuesUtil {
    public static void putMapped(String key, Map<String, String> map, ContentValues values, boolean value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, Boolean.valueOf(value));
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, Boolean.valueOf(value));
        }
    }

    public static void putMapped(String key, Map<String, String> map, ContentValues values, byte value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, Byte.valueOf(value));
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, Byte.valueOf(value));
        }
    }

    public static void putMapped(String key, Map<String, String> map, ContentValues values, byte[] value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, value);
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, value);
        }
    }

    public static void putMapped(String key, Map<String, String> map, ContentValues values, double value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, Double.valueOf(value));
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, Double.valueOf(value));
        }
    }

    public static void putMapped(String key, Map<String, String> map, ContentValues values, float value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, Float.valueOf(value));
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, Float.valueOf(value));
        }
    }

    public static void putMapped(String key, Map<String, String> map, ContentValues values, int value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, Integer.valueOf(value));
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, Integer.valueOf(value));
        }
    }

    public static void putMapped(String key, Map<String, String> map, ContentValues values, long value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, Long.valueOf(value));
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, Long.valueOf(value));
        }
    }

    public static void putMapped(String key, Map<String, String> map, ContentValues values, short value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, Short.valueOf(value));
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, Short.valueOf(value));
        }
    }

    public static void putMapped(String key, Map<String, String> map, ContentValues values, String value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, value);
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, value);
        }
    }

    public static void putMapped(String key, Map<String, String> map, ContentValues values, Object value) {
        if (map == null || !map.containsKey(key)) {
            values.put(key, value.toString());
        }
        String alias = (String) map.get(key);
        if (alias != null) {
            values.put(alias, value.toString());
        }
    }
}
