package haivo.us.crypto.mechanoid.internal.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import haivo.us.crypto.mechanoid.net.JsonReaderUtil;

public class JsonUtil {
    public static List<String> readStringList(JsonReader reader) throws IOException {
        ArrayList<String> list = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            list.add(reader.nextString());
        }
        reader.endArray();
        return list;
    }

    public static List<Integer> readIntegerList(JsonReader reader) throws IOException {
        ArrayList<Integer> list = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            list.add(Integer.valueOf(reader.nextInt()));
        }
        reader.endArray();
        return list;
    }

    public static List<Double> readDoubleList(JsonReader reader) throws IOException {
        ArrayList<Double> list = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            list.add(Double.valueOf(reader.nextDouble()));
        }
        reader.endArray();
        return list;
    }

    public static List<Long> readLongList(JsonReader reader) throws IOException {
        ArrayList<Long> list = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            list.add(Long.valueOf(reader.nextLong()));
        }
        reader.endArray();
        return list;
    }

    public static List<Boolean> readBooleanList(JsonReader reader) throws IOException {
        ArrayList<Boolean> list = new ArrayList();
        reader.beginArray();
        while (reader.hasNext()) {
            list.add(Boolean.valueOf(JsonReaderUtil.coerceNextBoolean(reader)));
        }
        reader.endArray();
        return list;
    }

    public static void writeStringList(JsonWriter writer, List<String> values) throws IOException {
        writer.beginArray();
        for (String value : values) {
            writer.value(value);
        }
        writer.endArray();
    }

    public static void writeIntegerList(JsonWriter writer, List<Integer> values) throws IOException {
        writer.beginArray();
        for (Number value : values) {
            writer.value(value);
        }
        writer.endArray();
    }

    public static void writeLongList(JsonWriter writer, List<Long> values) throws IOException {
        writer.beginArray();
        for (Number value : values) {
            writer.value(value);
        }
        writer.endArray();
    }

    public static void writeDoubleList(JsonWriter writer, List<Double> values) throws IOException {
        writer.beginArray();
        for (Number value : values) {
            writer.value(value);
        }
        writer.endArray();
    }

    public static void writeBooleanList(JsonWriter writer, List<Boolean> values) throws IOException {
        writer.beginArray();
        for (Boolean value : values) {
            writer.value(value.booleanValue());
        }
        writer.endArray();
    }
}
