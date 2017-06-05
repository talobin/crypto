package haivo.us.crypto.mechanoid.ops;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import java.lang.ref.WeakReference;

public class OperationExecutor {
    public static final int MODE_ALWAYS = 1;
    public static final int MODE_ONCE = 0;
    public static final int MODE_ON_ERROR = 2;
    private static final String STATE_KEY = "haivo.us.crypto.mechanoid.ops.OperationExecutor.State";
    private static final String TAG;
    private WeakReference<OperationExecutorCallbacks> mCallbacksRef;
    private boolean mEnableLogging;
    private OpInfo mOpInfo;
    private OperationServiceListener mServiceListener;
    private String mUserStateKey;

    static class OpInfo implements Parcelable {
        public static final Creator<OpInfo> CREATOR;
        boolean mCallbackInvoked;
        int mId;
        public Intent mIntent;
        OperationResult mResult;

        /* renamed from: haivo.us.crypto.mechanoid.ops.OperationExecutor.OpInfo.1 */
        static class C02331 implements Creator<OpInfo> {
            C02331() {
            }

            public OpInfo createFromParcel(Parcel in) {
                return new OpInfo(in);
            }

            public OpInfo[] newArray(int size) {
                return new OpInfo[size];
            }
        }

        static {
            CREATOR = new C02331();
        }

        OpInfo() {
            this.mId = OperationExecutor.MODE_ONCE;
            this.mCallbackInvoked = false;
            this.mResult = null;
        }

        OpInfo(Parcel in) {
            boolean z = false;
            this.mId = OperationExecutor.MODE_ONCE;
            this.mCallbackInvoked = false;
            this.mResult = null;
            this.mId = in.readInt();
            if (in.readInt() > 0) {
                z = true;
            }
            this.mCallbackInvoked = z;
            this.mResult = (OperationResult) in.readParcelable(OperationResult.class.getClassLoader());
            this.mIntent = (Intent) in.readParcelable(Intent.class.getClassLoader());
        }

        public int describeContents() {
            return OperationExecutor.MODE_ONCE;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i;
            dest.writeInt(this.mId);
            if (this.mCallbackInvoked) {
                i = OperationExecutor.MODE_ALWAYS;
            } else {
                i = OperationExecutor.MODE_ONCE;
            }
            dest.writeInt(i);
            dest.writeParcelable(this.mResult, OperationExecutor.MODE_ONCE);
            dest.writeParcelable(this.mIntent, OperationExecutor.MODE_ONCE);
        }
    }

    /* renamed from: haivo.us.crypto.mechanoid.ops.OperationExecutor.1 */
    class C03471 extends OperationServiceListener {
        C03471() {
        }

        public void onOperationComplete(int id, OperationResult result) {
            if (OperationExecutor.this.mOpInfo != null && OperationExecutor.this.mOpInfo.mId == id) {
                OperationExecutor.this.mOpInfo.mResult = result;
                if (OperationExecutor.this.invokeOnOperationComplete(result)) {
                    OperationExecutor.this.mOpInfo.mCallbackInvoked = true;
                    if (OperationExecutor.this.mEnableLogging) {
                        String access$200 = OperationExecutor.TAG;
                        Object[] objArr = new Object[OperationExecutor.MODE_ALWAYS];
                        objArr[OperationExecutor.MODE_ONCE] = OperationExecutor.this.mUserStateKey;
                        Log.d(access$200, String.format("[Operation Complete] key: %s", objArr));
                    }
                }
            }
        }
    }

    static {
        TAG = OperationExecutor.class.getSimpleName();
    }

    public String getKey() {
        return this.mUserStateKey;
    }

    public OperationExecutor(String key, Bundle savedInstanceState, OperationExecutorCallbacks callbacks) {
        this(key, savedInstanceState, callbacks, false);
    }

    public OperationExecutor(String key,
                             Bundle savedInstanceState,
                             OperationExecutorCallbacks callbacks,
                             boolean enableLogging) {
        this.mServiceListener = new C03471();
        this.mUserStateKey = key;
        this.mCallbacksRef = new WeakReference(callbacks);
        this.mEnableLogging = enableLogging;
        restoreState(savedInstanceState);
        Ops.bindListener(this.mServiceListener);
        ensureCallbacks();
    }

    public boolean isComplete() {
        return this.mOpInfo != null && this.mOpInfo.mCallbackInvoked;
    }

    public boolean isOk() {
        return isComplete() && getResult().isOk();
    }

    public boolean isError() {
        return isComplete() && !getResult().isOk();
    }

    public boolean isPending() {
        return this.mOpInfo != null && this.mOpInfo.mResult == null;
    }

    public OperationResult getResult() {
        if (this.mOpInfo == null) {
            return null;
        }
        return this.mOpInfo.mResult;
    }

    public Intent getIntent() {
        if (this.mOpInfo == null) {
            return null;
        }
        return this.mOpInfo.mIntent;
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Bundle state = savedInstanceState.getBundle(STATE_KEY);
            if (state != null) {
                state.setClassLoader(OpInfo.class.getClassLoader());
                this.mOpInfo = (OpInfo) state.getParcelable(this.mUserStateKey);
                if (this.mEnableLogging) {
                    String str = TAG;
                    Object[] objArr = new Object[MODE_ALWAYS];
                    objArr[MODE_ONCE] = this.mUserStateKey;
                    Log.d(str, String.format("[Restoring State] key:%s", objArr));
                }
            }
        }
    }

    public void saveState(Bundle outState) {
        if (this.mEnableLogging) {
            String str = TAG;
            Object[] objArr = new Object[MODE_ALWAYS];
            objArr[MODE_ONCE] = this.mUserStateKey;
            Log.d(str, String.format("[Saving State] key: %s", objArr));
        }
        Bundle state = new Bundle();
        state.putParcelable(this.mUserStateKey, this.mOpInfo);
        outState.putBundle(STATE_KEY, state);
    }

    public void removeCallback() {
        this.mCallbacksRef = null;
    }

    public void setCallback(OperationExecutorCallbacks callbacks) {
        this.mCallbacksRef = new WeakReference(callbacks);
    }

    private void ensureCallbacks() {
        if (this.mOpInfo != null) {
            if (this.mOpInfo.mResult != null) {
                completeOperation();
            } else if (Ops.isOperationPending(this.mOpInfo.mId)) {
                if (this.mEnableLogging) {
                    Object[] r3 = new Object[MODE_ON_ERROR];
                    r3[MODE_ONCE] = Integer.valueOf(this.mOpInfo.mId);
                    r3[MODE_ALWAYS] = this.mUserStateKey;
                    Log.d(TAG, String.format("[Operation Pending] request id: %s, key: %s", r3));
                }
                invokeOnOperationPending();
            } else {
                OperationResult result = (OperationResult) Ops.getLog().get(Integer.valueOf(this.mOpInfo.mId));
                if (result == null) {
                    Object[] r3 = new Object[MODE_ON_ERROR];
                    r3[MODE_ONCE] = Integer.valueOf(this.mOpInfo.mId);
                    r3[MODE_ALWAYS] = this.mUserStateKey;
                    Log.d(TAG,
                          String.format("[Operation Retry] the log did not contain request id: %s, key: %s, retrying...",
                                        r3));
                    executeOperation(this.mOpInfo.mIntent);
                    return;
                }
                this.mOpInfo.mResult = result;
                completeOperation();
            }
        }
    }

    protected boolean invokeOnOperationPending() {
        if (this.mCallbacksRef == null) {
            return false;
        }
        OperationExecutorCallbacks callbacks = (OperationExecutorCallbacks) this.mCallbacksRef.get();
        if (callbacks == null) {
            return false;
        }
        callbacks.onOperationPending(this.mUserStateKey);
        return true;
    }

    protected boolean invokeOnOperationComplete(OperationResult result) {
        if (this.mCallbacksRef == null) {
            return false;
        }
        OperationExecutorCallbacks callbacks = (OperationExecutorCallbacks) this.mCallbacksRef.get();
        if (callbacks != null) {
            return callbacks.onOperationComplete(this.mUserStateKey, result);
        }
        return false;
    }

    public void execute(Intent operationIntent, int mode) {
        if (operationIntent == null) {
            String str = TAG;
            Object[] objArr = new Object[MODE_ALWAYS];
            objArr[MODE_ONCE] = this.mUserStateKey;
            Log.d(str, String.format("[Operation Null] operationintent argument was null, key: %s", objArr));
            return;
        }
        if (mode == MODE_ALWAYS) {
            this.mOpInfo = null;
            executeOperation(operationIntent);
        } else if (mode == 0) {
            if (this.mOpInfo == null) {
                executeOperation(operationIntent);
            }
        } else if (mode == MODE_ON_ERROR && (this.mOpInfo == null || isError())) {
            executeOperation(operationIntent);
        }
        completeOperation();
    }

    private void completeOperation() {
        if (this.mOpInfo.mResult != null
            && !this.mOpInfo.mCallbackInvoked
            && invokeOnOperationComplete(this.mOpInfo.mResult)) {
            if (this.mEnableLogging) {
                String str = TAG;
                Object[] objArr = new Object[MODE_ON_ERROR];
                objArr[MODE_ONCE] = Integer.valueOf(this.mOpInfo.mId);
                objArr[MODE_ALWAYS] = this.mUserStateKey;
                Log.d(str, String.format("[Operation Complete] request id: %s, key: %s", objArr));
            }
            this.mOpInfo.mCallbackInvoked = true;
        }
    }

    protected void executeOperation(Intent operationIntent) {
        if (this.mEnableLogging) {
            String str = TAG;
            Object[] objArr = new Object[MODE_ALWAYS];
            objArr[MODE_ONCE] = this.mUserStateKey;
            Log.d(str, String.format("[Execute Operation] key: %s", objArr));
        }
        this.mOpInfo = new OpInfo();
        this.mOpInfo.mIntent = operationIntent;
        invokeOnOperationPending();
        this.mOpInfo.mId = Ops.execute(operationIntent);
    }
}
