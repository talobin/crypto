package haivo.us.crypto.mechanoid.util;

import android.database.Cursor;
import java.io.Closeable;
import java.io.IOException;

public final class Closeables {
    private Closeables() {
    }

    public static void closeSilently(Cursor closeable) {
        if (closeable != null) {
            closeable.close();
        }
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException x) {
                x.printStackTrace();
            }
        }
    }
}
