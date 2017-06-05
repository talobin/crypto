package haivo.us.crypto.mechanoid.net;

public interface JsonEntityWriterProvider {
    <T, R extends JsonEntityWriter<T>> R get(Class<T> cls);
}
