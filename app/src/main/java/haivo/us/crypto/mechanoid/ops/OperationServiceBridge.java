package haivo.us.crypto.mechanoid.ops;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ServiceInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.SparseArray;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Set;
import java.util.WeakHashMap;
import haivo.us.crypto.mechanoid.Mechanoid;
import haivo.us.crypto.mechanoid.ReflectUtil;
import haivo.us.crypto.mechanoid.internal.util.Collections;

public class OperationServiceBridge {
    public static final int MSG_OPERATION_ABORTED = 4;
    public static final int MSG_OPERATION_COMPLETE = 2;
    public static final int MSG_OPERATION_PROGRESS = 3;
    public static final int MSG_OPERATION_STARTING = 1;
    private Hashtable<String, OperationServiceConfiguration> mConfigurations;
    private Set<OperationServiceListener> mListeners;
    private OperationLog mLog;
    private boolean mPaused;
    private SparseArray<Intent> mPausedRequests;
    private SparseArray<Intent> mPendingRequests;
    private int mRequestIdCounter;
    private Messenger messenger;

    /* renamed from: haivo.us.crypto.mechanoid.ops.OperationServiceBridge.1 */
    class C02391 extends Handler {
        C02391() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case OperationServiceBridge.MSG_OPERATION_STARTING /*1*/:
                    OperationServiceBridge.this.onOperationStarting(msg.arg1, msg.getData());
                    break;
                case OperationServiceBridge.MSG_OPERATION_COMPLETE /*2*/:
                    OperationServiceBridge.this.onOperationComplete(msg.arg1, msg.getData());
                    break;
                case OperationServiceBridge.MSG_OPERATION_PROGRESS /*3*/:
                    OperationServiceBridge.this.onOperationProgress(msg.arg1, msg.arg2, msg.getData());
                    break;
                case OperationServiceBridge.MSG_OPERATION_ABORTED /*4*/:
                    OperationServiceBridge.this.onOperationAborted(msg.arg1, msg.arg2, msg.getData());
                    break;
                default:
            }
        }
    }

    public OperationLog getLog() {
        return this.mLog;
    }

    public static int getOperationRequestId(Intent intent) {
        return intent.getIntExtra(OperationService.EXTRA_REQUEST_ID, -1);
    }

    public OperationServiceBridge() {
        this.mRequestIdCounter = MSG_OPERATION_STARTING;
        this.mConfigurations = new Hashtable();
        this.mPendingRequests = new SparseArray();
        this.mPausedRequests = new SparseArray();
        this.mListeners = Collections.newSetFromMap(new WeakHashMap());
        this.mLog = new OperationLog(60);
        this.messenger = new Messenger(new C02391());
        initConfigurations();
    }

    protected void initConfigurations() {
        try {
            ServiceInfo[] services = Mechanoid.getApplicationContext()
                                              .getPackageManager()
                                              .getPackageInfo(Mechanoid.getApplicationContext().getPackageName(),
                                                              MSG_OPERATION_ABORTED).services;
            if (services != null) {
                ServiceInfo[] arr$ = services;
                int len$ = arr$.length;
                for (int i$ = 0; i$ < len$; i$ += MSG_OPERATION_STARTING) {
                    Class<?> clz = ReflectUtil.loadClassSilently(Ops.class.getClassLoader(), arr$[i$].name);
                    if (clz != null && Service.class.isAssignableFrom(clz)) {
                        Field field = ReflectUtil.getFieldSilently(clz, "CONFIG");
                        if (field != null) {
                            this.mConfigurations.put(clz.getName(),
                                                     (OperationServiceConfiguration) ReflectUtil.getFieldValueSilently(
                                                         field));
                        }
                    }
                }
            }
        } catch (NameNotFoundException e) {
        }
    }

    private int createRequestId() {
        int i = this.mRequestIdCounter;
        this.mRequestIdCounter = i + MSG_OPERATION_STARTING;
        return i;
    }

    public Intent findPendingRequestByAction(String action) {
        int i;
        Intent request;
        for (i = 0; i < this.mPendingRequests.size(); i += MSG_OPERATION_STARTING) {
            request = (Intent) this.mPendingRequests.valueAt(i);
            if (request.getAction().equals(action)) {
                return request;
            }
        }
        if (this.mPaused) {
            for (i = 0; i < this.mPausedRequests.size(); i += MSG_OPERATION_STARTING) {
                request = (Intent) this.mPausedRequests.valueAt(i);
                if (request.getAction().equals(action)) {
                    return request;
                }
            }
        }
        return null;
    }

    public Intent findPendingRequestByActionWithExtras(String action, Bundle extras) {
        int i;
        Intent request;
        for (i = 0; i < this.mPendingRequests.size(); i += MSG_OPERATION_STARTING) {
            request = (Intent) this.mPendingRequests.valueAt(i);
            if (request.getAction().equals(action) && intentContainsExtras(request, extras)) {
                return request;
            }
        }
        if (this.mPaused) {
            for (i = 0; i < this.mPausedRequests.size(); i += MSG_OPERATION_STARTING) {
                request = (Intent) this.mPausedRequests.valueAt(i);
                if (request.getAction().equals(action) && intentContainsExtras(request, extras)) {
                    return request;
                }
            }
        }
        return null;
    }

    private Intent removePendingRequestById(int requestId) {
        Intent intent = (Intent) this.mPendingRequests.get(requestId);
        if (intent != null) {
            this.mPendingRequests.delete(requestId);
            return intent;
        } else if (!this.mPaused) {
            return intent;
        } else {
            Intent pausedIntent = (Intent) this.mPausedRequests.get(requestId);
            if (pausedIntent == null) {
                return intent;
            }
            this.mPausedRequests.delete(requestId);
            return pausedIntent;
        }
    }

    public boolean isOperationPending(int id) {
        if (id <= 0) {
            return false;
        }
        if ((!this.mPaused || this.mPendingRequests.get(id) == null) && this.mPendingRequests.get(id) == null) {
            return false;
        }
        return true;
    }

    public void abort(int id, int reason) {
        Intent opIntent = (Intent) this.mPendingRequests.get(id);
        if (opIntent != null) {
            Context context = Mechanoid.getApplicationContext();
            Intent intent = new Intent("haivo.us.crypto.mechanoid.op.actions.ABORT");
            intent.setClassName(context, opIntent.getComponent().getClassName());
            intent.putExtra(OperationService.EXTRA_BRIDGE_MESSENGER, this.messenger);
            intent.putExtra(OperationService.EXTRA_REQUEST_ID, id);
            intent.putExtra(OperationService.EXTRA_ABORT_REASON, reason);
            context.startService(intent);
        }
        if (this.mPaused && ((Intent) this.mPausedRequests.get(id)) != null) {
            onOperationAborted(id, reason, new Bundle());
        }
    }

    public int execute(Intent intent) {
        Intent pending = ((OperationServiceConfiguration) this.mConfigurations.get(intent.getComponent()
                                                                                         .getClassName())).getOperationConfigurationRegistry()
                                                                                                          .getOperationConfiguration(
                                                                                                              intent.getAction())
                                                                                                          .findMatchOnConstraint(
                                                                                                              this,
                                                                                                              intent);
        if (pending != null) {
            return getOperationRequestId(pending);
        }
        Intent clonedIntent = (Intent) intent.clone();
        int id = createRequestId();
        clonedIntent.putExtra(OperationService.EXTRA_BRIDGE_MESSENGER, this.messenger);
        clonedIntent.putExtra(OperationService.EXTRA_REQUEST_ID, id);
        if (this.mPaused) {
            this.mPausedRequests.put(id, clonedIntent);
            return id;
        }
        this.mPendingRequests.put(id, clonedIntent);
        Mechanoid.startService(clonedIntent);
        return id;
    }

    public void pause(boolean pause) {
        if (this.mPaused && !pause) {
            this.mPaused = false;
            for (int i = 0; i < this.mPausedRequests.size(); i += MSG_OPERATION_STARTING) {
                Intent request = (Intent) this.mPausedRequests.valueAt(i);
                this.mPendingRequests.put(this.mPausedRequests.keyAt(i), request);
                Mechanoid.startService(request);
            }
            this.mPausedRequests.clear();
        }
        this.mPaused = pause;
    }

    public void bindListener(OperationServiceListener listener) {
        this.mListeners.add(listener);
    }

    public void unbindListener(OperationServiceListener listener) {
        this.mListeners.remove(listener);
    }

    protected void onOperationStarting(int id, Bundle data) {
        Intent intent = (Intent) this.mPendingRequests.get(id);
        if (intent != null) {
            if (data == null) {
                data = new Bundle();
            }
            notifyOperationStarting(id, intent, data);
        }
    }

    protected void onOperationComplete(int id, Bundle bundledResult) {
        Intent intent = removePendingRequestById(id);
        OperationResult result = OperationResult.fromBundle(bundledResult);
        if (intent != null) {
            result.setRequest(intent);
            this.mLog.put(Integer.valueOf(id), result);
            notifyOperationComplete(id, result);
        }
    }

    protected void onOperationProgress(int id, int progress, Bundle data) {
        Intent intent = (Intent) this.mPendingRequests.get(id);
        if (intent != null) {
            notifyOperationProgress(id, intent, progress, data);
        }
    }

    protected void onOperationAborted(int id, int reason, Bundle data) {
        Intent intent = removePendingRequestById(id);
        if (intent != null) {
            notifyOperationAborted(id, intent, reason, data);
        }
    }

    private void notifyOperationStarting(int id, Intent intent, Bundle data) {
        for (OperationServiceListener listener : this.mListeners) {
            if (listener != null) {
                listener.onOperationStarting(id, intent, data);
            }
        }
    }

    private void notifyOperationComplete(int id, OperationResult result) {
        for (OperationServiceListener listener : this.mListeners) {
            if (listener != null) {
                listener.onOperationComplete(id, result);
            }
        }
    }

    private void notifyOperationProgress(int id, Intent intent, int progress, Bundle data) {
        for (OperationServiceListener listener : this.mListeners) {
            if (listener != null) {
                listener.onOperationProgress(id, intent, progress, data);
            }
        }
    }

    private void notifyOperationAborted(int id, Intent intent, int reason, Bundle data) {
        for (OperationServiceListener listener : this.mListeners) {
            if (listener != null) {
                listener.onOperationAborted(id, intent, reason, data);
            }
        }
    }

    public boolean intentContainsExtras(Intent intent, Bundle extras) {
        Bundle intentExtras = intent.getExtras();
        for (String key : extras.keySet()) {
            Object a = extras.get(key);
            Object b = intentExtras.get(key);
            if (b == null) {
                return false;
            }
            if (!b.equals(a)) {
                return false;
            }
        }
        return true;
    }
}
