package haivo.us.crypto.mechanoid.ops;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import java.util.LinkedList;
import java.util.Queue;

@Deprecated
public abstract class OperationManagerBase {
    private static final String TAG = "OperationManager";
    OperationManagerCallbacks mCallbacks;
    private boolean mEnableLogging;
    private SparseArray<OpInfo> mOperations;
    private Queue<Runnable> mPendingOperations;
    private OperationServiceListener mServiceListener;
    private boolean mStarted;
    private String mStateKey;

    /* renamed from: haivo.us.crypto.mechanoid.ops.OperationManagerBase.2 */
    class C02342 implements Runnable {
        final /* synthetic */ Intent val$operationIntent;
        final /* synthetic */ boolean val$pendingForce;
        final /* synthetic */ int val$pendingOperationCode;

        C02342(Intent intent, int i, boolean z) {
            this.val$operationIntent = intent;
            this.val$pendingOperationCode = i;
            this.val$pendingForce = z;
        }

        public void run() {
            OperationManagerBase.this.execute(this.val$operationIntent, this.val$pendingOperationCode, this.val$pendingForce);
        }
    }

    static class OpInfo implements Parcelable {
        public static final Creator<OpInfo> CREATOR;
        boolean mCallbackInvoked;
        int mId;
        public Intent mIntent;
        OperationResult mResult;
        int mUserCode;

        /* renamed from: haivo.us.crypto.mechanoid.ops.OperationManagerBase.OpInfo.1 */
        static class C02351 implements Creator<OpInfo> {
            C02351() {
            }

            public OpInfo createFromParcel(Parcel in) {
                return new OpInfo(in);
            }

            public OpInfo[] newArray(int size) {
                return new OpInfo[size];
            }
        }

        static {
            CREATOR = new C02351();
        }

        OpInfo() {
            this.mUserCode = 0;
            this.mId = 0;
            this.mCallbackInvoked = false;
            this.mResult = null;
        }

        OpInfo(Parcel in) {
            boolean z = false;
            this.mUserCode = 0;
            this.mId = 0;
            this.mCallbackInvoked = false;
            this.mResult = null;
            this.mUserCode = in.readInt();
            this.mId = in.readInt();
            if (in.readInt() > 0) {
                z = true;
            }
            this.mCallbackInvoked = z;
            this.mResult = (OperationResult) in.readParcelable(OperationResult.class.getClassLoader());
            this.mIntent = (Intent) in.readParcelable(Intent.class.getClassLoader());
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            int i;
            dest.writeInt(this.mUserCode);
            dest.writeInt(this.mId);
            if (this.mCallbackInvoked) {
                i = 1;
            } else {
                i = 0;
            }
            dest.writeInt(i);
            dest.writeParcelable(this.mResult, 0);
            dest.writeParcelable(this.mIntent, 0);
        }
    }

    /* renamed from: haivo.us.crypto.mechanoid.ops.OperationManagerBase.1 */
    class C03481 extends OperationServiceListener {
        C03481() {
        }

        public void onOperationComplete(int id, OperationResult result) {
            OpInfo op = OperationManagerBase.this.findOperationInfoByRequestId(id);
            if (op != null) {
                op.mResult = result;
                if (OperationManagerBase.this.invokeOnOperationComplete(op.mUserCode, result, false)) {
                    op.mCallbackInvoked = true;
                    if (OperationManagerBase.this.mEnableLogging) {
                        Log.d(OperationManagerBase.TAG, String.format("[Operation Complete] request id:%s, user code:%s", new Object[]{
                            Integer.valueOf(id), Integer.valueOf(op.mUserCode)}));
                    }
                }
            }
        }
    }

    protected OperationManagerBase(OperationManagerCallbacks callbacks, boolean enableLogging) {
        this.mOperations = new SparseArray();
        this.mPendingOperations = new LinkedList();
        this.mStarted = false;
        this.mServiceListener = new C03481();
        this.mCallbacks = callbacks;
        this.mEnableLogging = enableLogging;
        this.mStateKey = "haivo.us.crypto.mechanoid.ops.OperationManager.State";
    }

    public void removeCallbacks() {
        this.mCallbacks = null;
    }

    public void setCallbacks(OperationManagerCallbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    private OpInfo findOperationInfoByRequestId(int requestId) {
        for (int i = 0; i < this.mOperations.size(); i++) {
            OpInfo op = (OpInfo) this.mOperations.valueAt(i);
            if (op.mId == requestId) {
                return op;
            }
        }
        return null;
    }

    void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBundle(this.mStateKey) != null) {
            this.mOperations = savedInstanceState.getSparseParcelableArray("operations");
            if (this.mEnableLogging) {
                Log.d(TAG, String.format("[Restoring State]", new Object[0]));
            }
        }
    }

    void saveState(Bundle outState) {
        if (this.mEnableLogging) {
            Log.d(TAG, String.format("[Saving State]", new Object[0]));
        }
        Bundle state = new Bundle();
        outState.putSparseParcelableArray("operations", this.mOperations);
        outState.putBundle(this.mStateKey, state);
    }

    void start() {
        if (!this.mStarted) {
            if (this.mEnableLogging) {
                Log.d(TAG, String.format("[Starting]", new Object[0]));
            }
            Ops.bindListener(this.mServiceListener);
            this.mStarted = true;
            ensureCallbacks();
            executePendingOperations();
        }
    }

    private void executePendingOperations() {
        while (this.mPendingOperations.peek() != null) {
            ((Runnable) this.mPendingOperations.poll()).run();
        }
    }

    private void ensureCallbacks() {
        for (int i = 0; i < this.mOperations.size(); i++) {
            OpInfo op = (OpInfo) this.mOperations.valueAt(i);
            if (Ops.isOperationPending(op.mId)) {
                if (this.mEnableLogging) {
                    Log.d(TAG, String.format("[Operation Pending] request id: %s, user code:%s", new Object[]{ Integer.valueOf(op.mId), Integer.valueOf(op.mUserCode)}));
                }
                invokeOnOperationPending(op.mUserCode);
            } else {
                OperationResult result = (OperationResult) Ops.getLog().get(Integer.valueOf(op.mId));
                if (result != null) {
                    op.mResult = result;
                    if (!op.mCallbackInvoked && invokeOnOperationComplete(op.mUserCode, op.mResult, false)) {
                        op.mCallbackInvoked = true;
                        if (this.mEnableLogging) {
                            Log.d(TAG, String.format("[Operation Complete] request id: %s, user code:%s", new Object[]{
                                Integer.valueOf(op.mId), Integer.valueOf(op.mUserCode)}));
                        }
                    }
                }
            }
        }
    }

    void stop() {
        if (this.mStarted) {
            this.mStarted = false;
            if (this.mEnableLogging) {
                Log.d(TAG, String.format("[Stopping]", new Object[0]));
            }
            Ops.unbindListener(this.mServiceListener);
        }
    }

    public void execute(Intent operationIntent, int code, boolean force) {
        if (operationIntent == null) {
            Log.d(TAG, String.format("[Operation Null] operationintent argument was null, code:%s", new Object[]{
                Integer.valueOf(code)}));
        } else if (this.mStarted) {
            OpInfo op = (OpInfo) this.mOperations.get(code);
            if (force || op == null) {
                this.mOperations.delete(code);
                if (this.mEnableLogging) {
                    Log.d(TAG, String.format("[Operation Pending] user code:%s", new Object[]{ Integer.valueOf(code)}));
                }
                invokeOnOperationPending(code);
                if (this.mEnableLogging) {
                    Log.d(TAG, String.format("[Execute Operation] user code:%s", new Object[]{ Integer.valueOf(code)}));
                }
                OpInfo newOp = new OpInfo();
                newOp.mUserCode = code;
                newOp.mIntent = operationIntent;
                this.mOperations.put(code, newOp);
                newOp.mId = Ops.execute(operationIntent);
            } else if (op.mResult != null) {
                if (this.mEnableLogging) {
                    Log.d(TAG, String.format("[Operation Complete] request id: %s, user code:%s, from cache:%s", new Object[]{
                        Integer.valueOf(op.mId), Integer.valueOf(op.mUserCode), Boolean.valueOf(op.mCallbackInvoked)}));
                }
                if (invokeOnOperationComplete(op.mUserCode, op.mResult, op.mCallbackInvoked)) {
                    op.mCallbackInvoked = true;
                }
            }
        } else {
            if (this.mEnableLogging) {
                Log.d(TAG, String.format("[Queue Operation] manager not started, queueing, user code:%s", new Object[]{
                    Integer.valueOf(code)}));
            }
            queuePendingOperation(operationIntent, code, force);
        }
    }

    private void queuePendingOperation(Intent operationIntent, int pendingOperationCode, boolean pendingForce) {
        this.mPendingOperations.add(new C02342(operationIntent, pendingOperationCode, pendingForce));
    }

    protected boolean invokeOnOperationPending(int code) {
        if (this.mCallbacks == null) {
            return false;
        }
        this.mCallbacks.onOperationPending(code);
        return true;
    }

    protected boolean invokeOnOperationComplete(int code, OperationResult result, boolean fromCache) {
        if (this.mCallbacks == null) {
            return false;
        }
        this.mCallbacks.onOperationComplete(code, result, fromCache);
        return true;
    }
}
