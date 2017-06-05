package haivo.us.crypto.mechanoid.ops;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import java.util.LinkedList;

public abstract class OperationProcessor {
    protected static final int MSG_OPERATION_ABORTED = 4;
    protected static final int MSG_OPERATION_COMPLETE = 2;
    protected static final int MSG_OPERATION_PROGRESS = 3;
    protected static final int MSG_OPERATION_STARTING = 1;
    protected static final int MSG_WORKER_READY = 5;
    private Handler handler;
    private OperationContext mContext;
    private Operation mCurrentOperation;
    private Intent mCurrentRequest;
    protected final boolean mEnableLogging;
    protected final String mLogTag;
    private final OperationService mService;
    private Worker mWorker;
    private boolean mWorkerReady;
    private LinkedList<Intent> requestQueue;

    /* renamed from: haivo.us.crypto.mechanoid.ops.OperationProcessor.1 */
    class C02361 extends Handler {
        C02361() {
        }

        public void handleMessage(Message msg) {
            String str;
            Object[] objArr;
            switch (msg.what) {
                case OperationProcessor.MSG_OPERATION_STARTING /*1*/:
                    if (OperationProcessor.this.mEnableLogging) {
                        str = OperationProcessor.this.mLogTag;
                        objArr = new Object[OperationProcessor.MSG_OPERATION_STARTING];
                        objArr[0] = OperationProcessor.this.mCurrentRequest;
                        Log.d(str, String.format("[Handle Operation Starting] intent:%s", objArr));
                    }
                    OperationProcessor.this.mService.onOperationStarting(OperationProcessor.this.mCurrentRequest, msg.getData());
                case OperationProcessor.MSG_OPERATION_COMPLETE /*2*/:
                    if (OperationProcessor.this.mEnableLogging) {
                        str = OperationProcessor.this.mLogTag;
                        objArr = new Object[OperationProcessor.MSG_OPERATION_STARTING];
                        objArr[0] = OperationProcessor.this.mCurrentRequest;
                        Log.d(str, String.format("[Handle Operation Complete] intent:%s", objArr));
                    }
                    OperationProcessor.this.mCurrentOperation = null;
                    OperationProcessor.this.mService.onOperationComplete(OperationProcessor.this.mCurrentRequest, msg.getData());
                    OperationProcessor.this.executePendingOperations();
                case OperationProcessor.MSG_OPERATION_PROGRESS /*3*/:
                    if (OperationProcessor.this.mEnableLogging) {
                        str = OperationProcessor.this.mLogTag;
                        objArr = new Object[OperationProcessor.MSG_OPERATION_STARTING];
                        objArr[0] = OperationProcessor.this.mCurrentRequest;
                        Log.d(str, String.format("[Handle Operation Progress] intent:%s", objArr));
                    }
                    OperationProcessor.this.mService.onOperationProgress(OperationProcessor.this.mCurrentRequest, msg.arg1, msg.getData());
                case OperationProcessor.MSG_OPERATION_ABORTED /*4*/:
                    if (OperationProcessor.this.mEnableLogging) {
                        str = OperationProcessor.this.mLogTag;
                        objArr = new Object[OperationProcessor.MSG_OPERATION_STARTING];
                        objArr[0] = OperationProcessor.this.mCurrentRequest;
                        Log.d(str, String.format("[Handle Operation Aborted] intent:%s", objArr));
                    }
                    OperationProcessor.this.mCurrentOperation = null;
                    OperationProcessor.this.mService.onOperationAborted(OperationProcessor.this.mCurrentRequest, msg.arg1, msg.getData());
                    OperationProcessor.this.executePendingOperations();
                case OperationProcessor.MSG_WORKER_READY /*5*/:
                    OperationProcessor.this.mWorkerReady = true;
                    OperationProcessor.this.executePendingOperations();
                default:
            }
        }
    }

    static class OperationRunnable implements Runnable {
        private Handler mCallbackHandler;
        private boolean mEnableLogging;
        private String mLogTag;
        private Operation mOperation;
        private OperationContext mOperationContext;

        public OperationRunnable(Handler callbackHandler, OperationContext operationContext, Operation operation, boolean enableLogging, String logTag) {
            this.mCallbackHandler = callbackHandler;
            this.mOperation = operation;
            this.mEnableLogging = enableLogging;
            this.mOperationContext = operationContext;
            this.mLogTag = logTag;
        }

        public void run() {
            this.mCallbackHandler.sendMessage(this.mCallbackHandler.obtainMessage(OperationProcessor.MSG_OPERATION_STARTING));
            Bundle result;
            try {
                OperationResult opResult = this.mOperation.execute(this.mOperationContext);
                if (opResult == null) {
                    throw new NullPointerException("OperationResult should not be null");
                }
                Message m;
                result = opResult.toBundle();
                if (this.mOperationContext.isAborted()) {
                    m = this.mCallbackHandler.obtainMessage(OperationProcessor.MSG_OPERATION_ABORTED);
                    m.arg1 = this.mOperationContext.getAbortReason();
                } else {
                    m = this.mCallbackHandler.obtainMessage(OperationProcessor.MSG_OPERATION_COMPLETE);
                }
                m.setData(result);
                this.mCallbackHandler.sendMessage(m);
            } catch (Exception x) {
                result = OperationResult.error(x).toBundle();
                if (this.mEnableLogging) {
                    String str = this.mLogTag;
                    Object[] objArr = new Object[OperationProcessor.MSG_OPERATION_STARTING];
                    objArr[0] = Log.getStackTraceString(x);
                    Log.e(str, String.format("[Operation Error] %s", objArr));
                }
            }
        }
    }

    static class Worker extends HandlerThread {
        private Handler mProcessorHandler;
        private Handler mWorkerHandler;

        public Worker(Handler processorHandler) {
            super("worker", 10);
            this.mProcessorHandler = processorHandler;
        }

        public void post(Runnable runnable) {
            this.mWorkerHandler.post(runnable);
        }

        protected void onLooperPrepared() {
            super.onLooperPrepared();
            this.mWorkerHandler = new Handler();
            this.mProcessorHandler.sendEmptyMessage(OperationProcessor.MSG_WORKER_READY);
        }
    }

    protected abstract Operation createOperation(String str);

    protected void notifyProgress(int progress, Bundle data) {
        Message m = this.handler.obtainMessage(MSG_OPERATION_PROGRESS);
        m.arg1 = progress;
        m.setData(data);
        this.handler.sendMessage(m);
    }

    public boolean hasPendingOperations() {
        return this.requestQueue.size() > 0;
    }

    public OperationProcessor(OperationService service, boolean enableLogging) {
        this.requestQueue = new LinkedList();
        this.handler = new C02361();
        this.mService = service;
        this.mLogTag = getClass().getSimpleName();
        this.mEnableLogging = enableLogging;
        this.mContext = new OperationContext();
        this.mWorker = new Worker(this.handler);
        this.mWorker.start();
    }

    public void execute(Intent intent) {
        if (this.mEnableLogging) {
            String str = this.mLogTag;
            Object[] objArr = new Object[MSG_OPERATION_STARTING];
            objArr[0] = intent;
            Log.d(str, String.format("[Execute (Queue)] intent:%s", objArr));
        }
        if (intent.getAction().equals("haivo.us.crypto.mechanoid.op.actions.ABORT")) {
            abortOperation(intent.getIntExtra(OperationService.EXTRA_REQUEST_ID, 0), intent.getIntExtra(OperationService.EXTRA_ABORT_REASON, 0));
            return;
        }
        this.requestQueue.offer(intent);
        executePendingOperations();
    }

    private void abortOperation(int abortRequestId, int abortReason) {
        if (this.mEnableLogging) {
            String str = this.mLogTag;
            Object[] objArr = new Object[MSG_OPERATION_COMPLETE];
            objArr[0] = Integer.valueOf(abortRequestId);
            objArr[MSG_OPERATION_STARTING] = Integer.valueOf(abortReason);
            Log.d(str, String.format("[Aborting] id:%s, reason:%s", objArr));
        }
        if (this.mCurrentOperation == null || OperationServiceBridge.getOperationRequestId(this.mCurrentRequest) != abortRequestId) {
            tryFlagQueuedOperationAsAborted(abortRequestId, abortReason);
            return;
        }
        this.mContext.handler.sendMessage(this.mContext.handler.obtainMessage(MSG_OPERATION_STARTING, abortReason, 0));
    }

    private void tryFlagQueuedOperationAsAborted(int abortRequestId, int abortReason) {
        for (int i = 0; i < this.requestQueue.size(); i += MSG_OPERATION_STARTING) {
            Intent queuedRequest = (Intent) this.requestQueue.get(i);
            if (OperationServiceBridge.getOperationRequestId(queuedRequest) == abortRequestId) {
                queuedRequest.putExtra(OperationService.EXTRA_IS_ABORTED, true);
                queuedRequest.putExtra(OperationService.EXTRA_ABORT_REASON, abortReason);
                return;
            }
        }
    }

    private void executePendingOperations() {
        if (!this.mWorkerReady) {
            Log.d(this.mLogTag, "[Waiting on Worker]");
        } else if (this.mCurrentOperation == null) {
            if (this.mEnableLogging) {
                Log.d(this.mLogTag, "[Executing Pending]");
            }
            Intent request = (Intent) this.requestQueue.poll();
            if (request != null) {
                executeOperation(request);
            }
        }
    }

    public void quit() {
        if (this.mEnableLogging) {
            Log.d(this.mLogTag, "[Quit]");
        }
        while (true) {
            Intent request = (Intent) this.requestQueue.poll();
            if (request != null) {
                this.mService.onOperationComplete(request, OperationResult.error(new OperationServiceStoppedException()).toBundle());
            } else {
                this.mWorker.quit();
                return;
            }
        }
    }

    private void executeOperation(Intent request) {
        if (this.mEnableLogging) {
            String str = this.mLogTag;
            Object[] objArr = new Object[MSG_OPERATION_STARTING];
            objArr[0] = request;
            Log.d(str, String.format("[Execute Operation] intent:%s", objArr));
        }
        if (request.getBooleanExtra(OperationService.EXTRA_IS_ABORTED, false)) {
            this.mService.onOperationAborted(request, request.getIntExtra(OperationService.EXTRA_ABORT_REASON, 0), new Bundle());
            executePendingOperations();
            return;
        }
        this.mCurrentRequest = request;
        this.mCurrentOperation = createOperation(request.getAction());
        if (this.mCurrentOperation == null) {
            throw new RuntimeException(request.getAction() + " Not Implemented");
        }
        this.mContext.reset();
        this.mContext.setApplicationContext(this.mService.getApplicationContext());
        this.mContext.setIntent(request);
        this.mContext.setOperationProcessor(this);
        this.mWorker.post(new OperationRunnable(this.handler, this.mContext, this.mCurrentOperation, this.mEnableLogging, this.mLogTag));
    }
}
