package haivo.us.crypto.mechanoid.net;

import java.io.IOException;
import java.util.List;
import haivo.us.crypto.mechanoid.internal.util.JsonWriter;

public abstract class JsonEntityWriter<T> {
    private JsonEntityWriterProvider mProvider;

    public abstract void write(JsonWriter jsonWriter, T t) throws IOException;

    public abstract void writeList(JsonWriter jsonWriter, List<T> list) throws IOException;

    public JsonEntityWriterProvider getProvider() {
        return this.mProvider;
    }

    public JsonEntityWriter(JsonEntityWriterProvider provider) {
        this.mProvider = provider;
    }
}
