package haivo.us.crypto.mechanoid.net;

import java.util.LinkedHashMap;
import java.util.Set;

public abstract class ServiceRequest {
    private int mConnectTimeout;
    private LinkedHashMap<String, String> mHeaders;
    private int mReadTimeout;

    public abstract String createUrl(String str);

    public ServiceRequest() {
        this.mHeaders = new LinkedHashMap();
        this.mReadTimeout = -1;
        this.mConnectTimeout = -1;
    }

    public void setHeader(String field, String value) {
        this.mHeaders.put(field, value);
    }

    public Set<String> getHeaderKeys() {
        return this.mHeaders.keySet();
    }

    public String getHeaderValue(String key) {
        return (String) this.mHeaders.get(key);
    }

    public void setReadTimeout(int readTimeout) {
        this.mReadTimeout = readTimeout;
    }

    public int getReadTimeout() {
        return this.mReadTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.mConnectTimeout = connectTimeout;
    }

    public int getConnectTimeout() {
        return this.mConnectTimeout;
    }
}
