package haivo.us.crypto.mechanoid;

import java.lang.reflect.Field;

public class ReflectUtil {
    public static Class<?> loadClassSilently(ClassLoader cl, String name) {
        try {
            return cl.loadClass(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Field getFieldSilently(Class<?> clz, String fieldName) {
        try {
            return clz.getField(fieldName);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    public static <T> Object getFieldValueSilently(Field field) {
        Object obj = null;
        try {
            obj = field.get(null);
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e2) {
        }
        return obj;
    }
}
