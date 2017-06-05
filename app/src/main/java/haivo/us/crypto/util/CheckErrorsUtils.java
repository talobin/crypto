package haivo.us.crypto.util;

import android.content.Context;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import haivo.us.crypto.R;

public class CheckErrorsUtils {
    public static final String parseVolleyErrorMsg(Context context, VolleyError error) {
        if (error instanceof NetworkError) {
            return context.getString(R.string.check_error_network);
        }
        if (error instanceof TimeoutError) {
            return context.getString(R.string.check_error_timeout);
        }
        if (error instanceof ServerError) {
            return context.getString(R.string.check_error_server);
        }
        if (error instanceof ParseError) {
            return context.getString(R.string.check_error_parse);
        }
        return context.getString(R.string.check_error_unknown);
    }

    public static final String formatError(Context context, String errorMsg) {
        Object[] objArr = new Object[1];
        if (errorMsg == null) {
            errorMsg = "UNKNOWN";
        }
        objArr[0] = errorMsg;
        return context.getString(R.string.check_error_generic_prefix, objArr);
    }
}
