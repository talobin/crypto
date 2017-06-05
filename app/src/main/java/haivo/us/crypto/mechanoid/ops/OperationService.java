package haivo.us.crypto.mechanoid.ops;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public abstract class OperationService extends Service {
    protected static final String ACTION_ABORT = "haivo.us.crypto.mechanoid.op.actions.ABORT";
    public static final String EXTRA_ABORT_REASON = "haivo.us.crypto.mechanoid.op.extras.ABORT_REASON";
    public static final String EXTRA_BRIDGE_MESSENGER = "haivo.us.crypto.mechanoid.op.extras.BRIDGE_MESSENGER";
    public static final String EXTRA_IS_ABORTED = "haivo.us.crypto.mechanoid.op.extras.IS_ABORTED";
    public static final String EXTRA_REQUEST_ID = "haivo.us.crypto.mechanoid.op.extras.REQUEST_ID";
    public static final String EXTRA_START_ID = "haivo.us.crypto.mechanoid.op.extras.START_ID";
    private static final int MSG_STOP = 1;
    private IBinder mBinder;
    protected final boolean mEnableLogging;
    private Handler mHandler;
    protected final String mLogTag;
    private OperationProcessor mProcessor;
    private boolean mStopped;

    /* renamed from: haivo.us.crypto.mechanoid.ops.OperationService.1 */
    class C02381 extends Handler {
        C02381() {
        }

        public void handleMessage(Message msg) {
            if (msg.what == OperationService.MSG_STOP) {
                OperationService.this.stop(msg.arg1);
            }
        }
    }

    class LocalBinder extends Binder {
        LocalBinder() {
        }

        OperationService getService() {
            return OperationService.this;
        }
    }

    protected abstract OperationProcessor createProcessor();

    public boolean isStopped() {
        return this.mStopped;
    }

    public OperationService(boolean enableLogging) {
        this.mHandler = new C02381();
        this.mBinder = new LocalBinder();
        this.mProcessor = createProcessor();
        this.mLogTag = getClass().getSimpleName();
        this.mEnableLogging = enableLogging;
    }

    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (this.mEnableLogging) {
            Log.d(this.mLogTag, String.format("[Start Command] startId:%s, intent:%s", new Object[]{ Integer.valueOf(startId), intent}));
        }
        if (intent != null) {
            intent.putExtra(EXTRA_START_ID, startId);
            handleIntent(intent);
        }
        return MSG_STOP;
    }

    private void handleIntent(Intent intent) {
        this.mProcessor.execute(intent);
    }

    private void sendStopMessage(Intent request) {
        this.mHandler.removeMessages(MSG_STOP);
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(MSG_STOP, request.getIntExtra(EXTRA_START_ID, 0), 0), getIdleStopTime());
    }

    protected long getIdleStopTime() {
        return 10000;
    }

    private void stop(int startId) {
        if (shouldStopOnAllOperationsComplete() && stopSelfResult(startId)) {
            if (this.mEnableLogging) {
                String str = this.mLogTag;
                Object[] objArr = new Object[MSG_STOP];
                objArr[0] = Integer.valueOf(startId);
                Log.d(str, String.format("[Stopping] startId:%s", objArr));
            }
            this.mStopped = true;
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mEnableLogging) {
            Log.d(this.mLogTag, "[Destroying]");
        }
        this.mProcessor.quit();
    }

    protected boolean shouldStopOnAllOperationsComplete() {
        return true;
    }

    public void onOperationStarting(Intent request, Bundle data) {
        if (this.mEnableLogging) {
            Log.d(this.mLogTag, String.format("[Operation Starting] request:%s, data:%s", new Object[]{request, data}));
        }
        Messenger messenger = (Messenger) request.getParcelableExtra(EXTRA_BRIDGE_MESSENGER);
        Message m = new Message();
        m.what = MSG_STOP;
        m.arg1 = OperationServiceBridge.getOperationRequestId(request);
        m.setData(data);
        try {
            messenger.send(m);
        } catch (RemoteException e) {
            if (this.mEnableLogging) {
                String str = this.mLogTag;
                Object[] objArr = new Object[MSG_STOP];
                objArr[0] = Log.getStackTraceString(e);
                Log.e(str, String.format("[Operation Exception] %s", objArr));
            }
        }
    }

    public void onOperationComplete(Intent request, Bundle data) {
        if (this.mEnableLogging) {
            Log.d(this.mLogTag, String.format("[Operation Complete] request:%s, data:%s", new Object[]{request, data}));
        }
        Messenger messenger = (Messenger) request.getParcelableExtra(EXTRA_BRIDGE_MESSENGER);
        Message m = new Message();
        m.what = 2;
        m.arg1 = OperationServiceBridge.getOperationRequestId(request);
        m.setData(data);
        try {
            messenger.send(m);
        } catch (RemoteException e) {
            if (this.mEnableLogging) {
                String str = this.mLogTag;
                Object[] objArr = new Object[MSG_STOP];
                objArr[0] = Log.getStackTraceString(e);
                Log.e(str, String.format("[Operation Exception] %s", objArr));
            }
        }
        sendStopMessage(request);
    }

    public void onOperationAborted(Intent request, int reason, Bundle data) {
        if (this.mEnableLogging) {
            Log.d(this.mLogTag, String.format("[Operation Aborted] request:%s, reason%s, data:%s", new Object[]{request, Integer.valueOf(reason), data}));
        }
        Messenger messenger = (Messenger) request.getParcelableExtra(EXTRA_BRIDGE_MESSENGER);
        Message m = new Message();
        m.what = 4;
        m.arg1 = OperationServiceBridge.getOperationRequestId(request);
        m.arg2 = reason;
        m.setData(data);
        try {
            messenger.send(m);
        } catch (RemoteException e) {
            if (this.mEnableLogging) {
                String str = this.mLogTag;
                Object[] objArr = new Object[MSG_STOP];
                objArr[0] = Log.getStackTraceString(e);
                Log.e(str, String.format("[Operation Exception] %s", objArr));
            }
        }
        sendStopMessage(request);
    }

    public void onOperationProgress(Intent request, int progress, Bundle data) {
        if (this.mEnableLogging) {
            Log.d(this.mLogTag, String.format("[Operation Progress] request:%s, progress:%s, data:%s", new Object[]{request, Integer.valueOf(progress), data}));
        }
        Messenger messenger = (Messenger) request.getParcelableExtra(EXTRA_BRIDGE_MESSENGER);
        Message m = new Message();
        m.what = 3;
        m.arg1 = OperationServiceBridge.getOperationRequestId(request);
        m.arg2 = progress;
        m.setData(data);
        try {
            messenger.send(m);
        } catch (RemoteException e) {
            if (this.mEnableLogging) {
                String str = this.mLogTag;
                Object[] objArr = new Object[MSG_STOP];
                objArr[0] = Log.getStackTraceString(e);
                Log.e(str, String.format("[Operation Exception] %s", objArr));
            }
        }
    }
}
