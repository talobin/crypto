package haivo.us.crypto.mechanoid.net;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;
import haivo.us.crypto.mechanoid.util.Streams;

public class HttpUrlConnectionResponse<T> implements Response<T> {
    private HttpURLConnection mConn;
    private T mContent;
    private Map<String, List<String>> mHeaders;
    private byte[] mInputBytes;
    private boolean mParsed;
    private Parser<T> mParser;
    private int mResponseCode;

    public int getResponseCode() {
        return this.mResponseCode;
    }

    public void checkResponseCodeOk() throws UnexpectedHttpStatusException {
        if (this.mResponseCode != Response.HTTP_OK) {
            throw new UnexpectedHttpStatusException(this.mResponseCode, Response.HTTP_OK);
        }
    }

    public void checkResponseCode(int responseCode) throws UnexpectedHttpStatusException {
        if (this.mResponseCode != responseCode) {
            throw new UnexpectedHttpStatusException(this.mResponseCode, Response.HTTP_OK);
        }
    }

    public Map<String, List<String>> getHeaders() {
        return this.mHeaders;
    }

    public HttpUrlConnectionResponse(HttpURLConnection conn, Parser<T> parser) throws ServiceException {
        this.mConn = conn;
        this.mParser = parser;
        try {
            this.mResponseCode = conn.getResponseCode();
            this.mHeaders = conn.getHeaderFields();
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public T parse() throws ServiceException {
        if (this.mParsed) {
            return this.mContent;
        }
        InputStream stream = null;
        try {
            if (this.mInputBytes == null) {
                stream = getInputStream();
            } else if (this.mInputBytes.length > 0) {
                stream = new ByteArrayInputStream(this.mInputBytes);
            }
            if (stream != null) {
                this.mContent = this.mParser.parse(stream);
            }
            this.mParsed = true;
            return this.mContent;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    private InputStream getInputStream() throws IOException {
        InputStream stream = this.mConn.getErrorStream();
        return stream != null ? stream : this.mConn.getInputStream();
    }

    public String readAsText() throws IOException {
        if (this.mInputBytes == null) {
            if (getInputStream() == null) {
                this.mInputBytes = new byte[0];
            } else {
                this.mInputBytes = Streams.readAllBytes(getInputStream());
            }
        }
        return Streams.readAllText(new ByteArrayInputStream(this.mInputBytes));
    }
}
