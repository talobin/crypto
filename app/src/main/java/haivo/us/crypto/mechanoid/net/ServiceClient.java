package haivo.us.crypto.mechanoid.net;

import android.util.Log;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;

public abstract class ServiceClient {
    private static final String DEFAULT_LOG_TAG;
    protected static final String METHOD_DELETE = "DELETE";
    protected static final String METHOD_GET = "GET";
    protected static final String METHOD_POST = "POST";
    protected static final String METHOD_PUT = "PUT";
    private String mBaseUrl;
    private int mConnectTimeout;
    private boolean mDebug;
    private LinkedHashMap<String, String> mHeaders;
    private int mReadTimeout;
    private JsonEntityReaderProvider mReaderProvider;
    private JsonEntityWriterProvider mWriterProvider;

    protected abstract JsonEntityReaderProvider createReaderProvider();

    protected abstract JsonEntityWriterProvider createWriterProvider();

    static {
        DEFAULT_LOG_TAG = ServiceClient.class.getSimpleName();
    }

    protected String getBaseUrl() {
        return this.mBaseUrl;
    }

    protected boolean isDebug() {
        return this.mDebug;
    }

    protected LinkedHashMap<String, String> getHeaders() {
        return this.mHeaders;
    }

    protected String getLogTag() {
        return DEFAULT_LOG_TAG;
    }

    public JsonEntityReaderProvider getReaderProvider() {
        return this.mReaderProvider;
    }

    public JsonEntityWriterProvider getWriterProvider() {
        return this.mWriterProvider;
    }

    public void setHeader(String field, String value) {
        getHeaders().put(field, value);
    }

    public ServiceClient(String baseUrl, boolean debug) {
        this.mHeaders = new LinkedHashMap();
        this.mConnectTimeout = 20000;
        this.mReadTimeout = 20000;
        this.mBaseUrl = baseUrl;
        this.mDebug = debug;
        this.mReaderProvider = createReaderProvider();
        this.mWriterProvider = createWriterProvider();
    }

    protected <REQUEST extends ServiceRequest, RESULT extends ServiceResult> Response<RESULT> get(REQUEST request,
                                                                                                  Parser<RESULT> resultParser)
        throws ServiceException {
        try {
            URL url = createUrl(request);
            Response<RESULT> mockedResponse = createMockedResponse(url, request, resultParser);
            if (mockedResponse == null) {
                if (isDebug()) {
                    Log.d(getLogTag(), "GET " + url.toString());
                }
                HttpURLConnection conn = openConnection(url);
                applyRequestTimeouts(request, conn);
                conn.setRequestMethod(METHOD_GET);
                conn.setRequestProperty("Accept", "application/json, text/json");
                applyRequestProperties(request, conn);
                if (isDebug()) {
                    NetLogHelper.logProperties(getLogTag(), conn.getRequestProperties());
                }
                conn.connect();
                Response<RESULT> response = new HttpUrlConnectionResponse(conn, resultParser);
                if (isDebug()) {
                    NetLogHelper.logProperties(getLogTag(), response.getHeaders());
                    Log.d(getLogTag(), response.readAsText());
                }
                return response;
            } else if (!isDebug()) {
                return mockedResponse;
            } else {
                Log.d(getLogTag(), "GET Mocked Response");
                return mockedResponse;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    protected <REQUEST extends ServiceRequest, RESULT extends ServiceResult> Response<RESULT> delete(REQUEST request,
                                                                                                     Parser<RESULT> resultParser)
        throws ServiceException {
        try {
            URL url = createUrl(request);
            Response<RESULT> mockedResponse = createMockedResponse(url, request, resultParser);
            if (mockedResponse == null) {
                if (isDebug()) {
                    Log.d(getLogTag(), "DELETE " + url.toString());
                }
                HttpURLConnection conn = openConnection(url);
                applyRequestTimeouts(request, conn);
                conn.setRequestMethod(METHOD_DELETE);
                applyRequestProperties(request, conn);
                if (isDebug()) {
                    NetLogHelper.logProperties(getLogTag(), conn.getRequestProperties());
                }
                conn.connect();
                Response<RESULT> response = new HttpUrlConnectionResponse(conn, resultParser);
                if (isDebug()) {
                    NetLogHelper.logProperties(getLogTag(), response.getHeaders());
                    Log.d(getLogTag(), response.readAsText());
                }
                return response;
            } else if (!isDebug()) {
                return mockedResponse;
            } else {
                Log.d(getLogTag(), "DELETE Mocked Response");
                return mockedResponse;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    protected <REQUEST extends ServiceRequest, RESULT extends ServiceResult> Response<RESULT> postUnlessPut(REQUEST request,
                                                                                                            Parser<RESULT> resultParser,
                                                                                                            boolean doPut)
        throws ServiceException {
        String method = doPut ? METHOD_PUT : METHOD_POST;
        try {
            URL url = createUrl(request);
            Response<RESULT> mockedResponse = createMockedResponse(url, request, resultParser);
            if (mockedResponse == null) {
                if (isDebug()) {
                    Log.d(getLogTag(), method + " " + url.toString());
                }
                HttpURLConnection conn = openConnection(url);
                applyRequestTimeouts(request, conn);
                conn.setDoOutput(true);
                conn.setRequestMethod(method);
                conn.setRequestProperty("Content-Type", "application/json, text/json");
                applyRequestProperties(request, conn);
                if (isDebug()) {
                    NetLogHelper.logProperties(getLogTag(), conn.getRequestProperties());
                }
                conn.connect();
                if (request instanceof EntityEnclosedServiceRequest) {
                    EntityEnclosedServiceRequest entityEnclosedRequest = (EntityEnclosedServiceRequest) request;
                    if (isDebug()) {
                        ByteArrayOutputStream debugOutStream = new ByteArrayOutputStream();
                        entityEnclosedRequest.writeBody(this.mWriterProvider, debugOutStream);
                        Log.d(getLogTag(), new String(debugOutStream.toByteArray(), "UTF-8"));
                    }
                    entityEnclosedRequest.writeBody(this.mWriterProvider, conn.getOutputStream());
                }
                Response<RESULT> response = new HttpUrlConnectionResponse(conn, resultParser);
                if (isDebug()) {
                    NetLogHelper.logProperties(getLogTag(), response.getHeaders());
                    Log.d(getLogTag(), response.readAsText());
                }
                return response;
            } else if (!isDebug()) {
                return mockedResponse;
            } else {
                Log.d(getLogTag(), method + " Mocked Response");
                return mockedResponse;
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    protected HttpURLConnection openConnection(URL url) throws IOException {
        return (HttpURLConnection) url.openConnection();
    }

    protected <REQUEST extends ServiceRequest, RESULT extends ServiceResult> Response<RESULT> post(REQUEST request,
                                                                                                   Parser<RESULT> resultParser)
        throws ServiceException {
        return postUnlessPut(request, resultParser, false);
    }

    protected <REQUEST extends ServiceRequest, RESULT extends ServiceResult> Response<RESULT> put(REQUEST request,
                                                                                                  Parser<RESULT> resultParser)
        throws ServiceException {
        return postUnlessPut(request, resultParser, true);
    }

    protected <REQUEST extends ServiceRequest> void applyRequestTimeouts(REQUEST request, HttpURLConnection conn) {
        if (request.getReadTimeout() > -1) {
            conn.setReadTimeout(request.getReadTimeout());
        } else {
            conn.setReadTimeout(this.mReadTimeout);
        }
        if (request.getConnectTimeout() > -1) {
            conn.setConnectTimeout(request.getConnectTimeout());
        } else {
            conn.setConnectTimeout(this.mConnectTimeout);
        }
    }

    protected <REQUEST extends ServiceRequest> void applyRequestProperties(REQUEST request, HttpURLConnection conn) {
        for (String key : getHeaders().keySet()) {
            conn.setRequestProperty(key, (String) getHeaders().get(key));
        }
        for (String key2 : request.getHeaderKeys()) {
            conn.setRequestProperty(key2, request.getHeaderValue(key2));
        }
    }

    protected <REQUEST extends ServiceRequest> URL createUrl(REQUEST request) throws MalformedURLException {
        return new URL(request.createUrl(getBaseUrl()));
    }

    protected <REQUEST extends ServiceRequest, RESULT extends ServiceResult> Response<RESULT> createMockedResponse(URL url,
                                                                                                                   REQUEST request,
                                                                                                                   Parser<RESULT> parser) {
        return null;
    }
}
