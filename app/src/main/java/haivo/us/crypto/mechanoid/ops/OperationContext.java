package haivo.us.crypto.mechanoid.ops;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class OperationContext {
    protected static final int MSG_ABORT = 1;
    Handler handler;
    private int mAbortReason;
    private Context mApplicationContext;
    private Intent mIntent;
    private boolean mIsAborted;
    private OperationProcessor mProcessor;

    /* renamed from: haivo.us.crypto.mechanoid.ops.OperationContext.1 */
    class C02321 extends Handler {
        C02321() {
        }

        public void handleMessage(Message msg) {
            if (msg.what == OperationContext.MSG_ABORT) {
                OperationContext.this.abort(msg.arg1);
            }
        }
    }

    public OperationContext() {
        this.handler = new C02321();
    }

    public void reset() {
        this.mApplicationContext = null;
        this.mProcessor = null;
        this.mIntent = null;
        this.mIsAborted = false;
        this.mAbortReason = 0;
    }

    public int getAbortReason() {
        return this.mAbortReason;
    }

    public boolean isAborted() {
        return this.mIsAborted;
    }

    public void abort(int reason) {
        this.mIsAborted = true;
        this.mAbortReason = reason;
    }

    public Context getApplicationContext() {
        return this.mApplicationContext;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    void setApplicationContext(Context context) {
        this.mApplicationContext = context.getApplicationContext();
    }

    void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    void setOperationProcessor(OperationProcessor processor) {
        this.mProcessor = processor;
    }

    public void postProgress(int progress, Bundle data) {
        if (data == null) {
            data = new Bundle();
        }
        if (this.mProcessor != null) {
            this.mProcessor.notifyProgress(progress, data);
        }
    }

    public void postProgress(int progress) {
        postProgress(progress, null);
    }
}
