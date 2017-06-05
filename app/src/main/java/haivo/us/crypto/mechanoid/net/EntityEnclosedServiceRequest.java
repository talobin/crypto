package haivo.us.crypto.mechanoid.net;

import java.io.IOException;
import java.io.OutputStream;

public abstract class EntityEnclosedServiceRequest extends ServiceRequest {
    public abstract void writeBody(JsonEntityWriterProvider jsonEntityWriterProvider, OutputStream outputStream)
        throws IOException;
}
