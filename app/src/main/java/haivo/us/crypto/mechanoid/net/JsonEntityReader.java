package haivo.us.crypto.mechanoid.net;

import java.io.IOException;
import java.util.List;
import haivo.us.crypto.mechanoid.internal.util.JsonReader;

public abstract class JsonEntityReader<T> {
    private JsonEntityReaderProvider mProvider;

    public abstract void read(JsonReader jsonReader, T t) throws IOException;

    public abstract void readList(JsonReader jsonReader, List<T> list) throws IOException;

    public JsonEntityReaderProvider getProvider() {
        return this.mProvider;
    }

    public JsonEntityReader(JsonEntityReaderProvider provider) {
        this.mProvider = provider;
    }
}
