package haivo.us.crypto.mechanoid.ops;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class OperationResult implements Parcelable {
    public static final Creator<OperationResult> CREATOR;
    public static final int RESULT_ERROR = 1;
    public static final int RESULT_OK = 0;
    private Throwable mError;
    private Intent mRequest;
    private int mResultCode;
    private Bundle mResultData;

    /* renamed from: haivo.us.crypto.mechanoid.ops.OperationResult.1 */
    static class C02371 implements Creator<OperationResult> {
        C02371() {
        }

        public OperationResult createFromParcel(Parcel in) {
            return new OperationResult(null);
        }

        public OperationResult[] newArray(int size) {
            return new OperationResult[size];
        }
    }

    public void setError(Throwable error) {
        this.mError = error;
    }

    public void setCode(int resultCode) {
        this.mResultCode = resultCode;
    }

    public int getCode() {
        return this.mResultCode;
    }

    public Throwable getError() {
        return this.mError;
    }

    public Bundle getData() {
        return this.mResultData;
    }

    public void setData(Bundle resultData) {
        this.mResultData = resultData;
    }

    public boolean isOk() {
        return this.mResultCode == 0;
    }

    public void setRequest(Intent request) {
        this.mRequest = request;
    }

    public Intent getRequest() {
        return this.mRequest;
    }

    static {
        CREATOR = new C02371();
    }

    public OperationResult(int resultCode) {
        this.mResultCode = 0;
        this.mError = null;
        this.mResultData = null;
        this.mRequest = null;
        this.mResultCode = resultCode;
    }

    private OperationResult(Parcel in) {
        this.mResultCode = 0;
        this.mError = null;
        this.mResultData = null;
        this.mRequest = null;
        this.mResultCode = in.readInt();
        this.mError = (Throwable) in.readSerializable();
        this.mResultData = in.readBundle();
        this.mRequest = (Intent) in.readParcelable(null);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mResultCode);
        dest.writeSerializable(this.mError);
        dest.writeBundle(this.mResultData);
        dest.writeParcelable(this.mRequest, 0);
    }

    public static OperationResult error(Throwable error) {
        OperationResult result = new OperationResult((int) RESULT_ERROR);
        result.setError(error);
        return result;
    }

    public static OperationResult ok() {
        return new OperationResult(0);
    }

    public static OperationResult ok(Bundle bundle) {
        if (bundle == null) {
            throw new RuntimeException("bundle cannot be null");
        }
        OperationResult result = new OperationResult(0);
        result.setData(bundle);
        return result;
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(OperationResult.class.getName(), this);
        return bundle;
    }

    public static OperationResult fromBundle(Bundle bundle) {
        bundle.setClassLoader(OperationResult.class.getClassLoader());
        return (OperationResult) bundle.getParcelable(OperationResult.class.getName());
    }
}
