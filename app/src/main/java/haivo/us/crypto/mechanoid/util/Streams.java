package haivo.us.crypto.mechanoid.util;

import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Streams {
    public static String readAllText(InputStream in) throws IOException {
        StringBuffer stream = new StringBuffer();
        byte[] b = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
        while (true) {
            int n = in.read(b);
            if (n == -1) {
                return stream.toString();
            }
            stream.append(new String(b, 0, n));
        }
    }

    public static byte[] readAllBytes(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[AccessibilityNodeInfoCompat.ACTION_SCROLL_FORWARD];
        while (true) {
            int read = in.read(b);
            if (read <= -1) {
                return out.toByteArray();
            }
            out.write(b, 0, read);
        }
    }
}
