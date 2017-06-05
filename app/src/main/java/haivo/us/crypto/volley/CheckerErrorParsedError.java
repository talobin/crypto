package haivo.us.crypto.volley;

import com.android.volley.ParseError;

public class CheckerErrorParsedError extends ParseError {
    private static final long serialVersionUID = -8541129282633613311L;
    private final String errorMsg;

    public CheckerErrorParsedError(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}
