package haivo.us.crypto.mechanoid.net;

import java.io.IOException;
import haivo.us.crypto.mechanoid.internal.util.JsonReader;
import haivo.us.crypto.mechanoid.internal.util.JsonToken;

public class JsonReaderUtil {
    private JsonReaderUtil() {
    }

    public static boolean coerceNextBoolean(JsonReader reader) throws IOException {
        JsonToken nextToken = reader.peek();
        if (nextToken == JsonToken.STRING) {
            return Boolean.valueOf(reader.nextString()).booleanValue();
        }
        if (nextToken == JsonToken.NUMBER) {
            return reader.nextInt() > 0;
        } else {
            return reader.nextBoolean();
        }
    }
}
