package haivo.us.crypto.mechanoid.ops;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

@Deprecated
public class SupportOperationManager extends OperationManagerBase {
    private static final String TAG = "OperationManager";

    public static class PersistenceFragment extends Fragment {
        private OperationManagerBase mOperationManager;
        private Bundle mSavedState;

        public void setOperationManager(OperationManagerBase operationManager) {
            this.mOperationManager = operationManager;
            if (this.mSavedState != null) {
                this.mOperationManager.restoreState(this.mSavedState);
                this.mOperationManager.start();
            }
        }

        public OperationManagerBase getOperationManager() {
            return this.mOperationManager;
        }

        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            OperationManagerBase operationManager = getOperationManager();
            if (operationManager == null) {
                this.mSavedState = savedInstanceState;
            } else {
                operationManager.restoreState(savedInstanceState);
            }
        }

        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            OperationManagerBase operationManager = getOperationManager();
            if (operationManager != null) {
                operationManager.saveState(outState);
            } else {
                removeSelf();
            }
        }

        public void onStart() {
            super.onStart();
            OperationManagerBase operationManager = getOperationManager();
            if (operationManager != null) {
                operationManager.start();
            } else {
                removeSelf();
            }
        }

        public void onStop() {
            super.onStop();
            OperationManagerBase operationManager = getOperationManager();
            if (operationManager != null) {
                operationManager.stop();
            } else {
                removeSelf();
            }
        }

        private void removeSelf() {
            getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    public static SupportOperationManager create(FragmentActivity activity, OperationManagerCallbacks callbacks, boolean enableLogging) {
        String tag = "Tags." + callbacks.getClass().getName();
        FragmentManager fm = activity.getSupportFragmentManager();
        PersistenceFragment frag = (PersistenceFragment) fm.findFragmentByTag(tag);
        if (frag == null) {
            if (enableLogging) {
                Log.d(TAG, String.format("[Create Fragment] tag:%s", new Object[]{tag}));
            }
            Fragment frag2 = new PersistenceFragment();
            SupportOperationManager operationManager = new SupportOperationManager(callbacks, enableLogging);
            //hai
            //frag2.setOperationManager(operationManager);
            fm.beginTransaction().add(frag2, tag).commit();
            return operationManager;
        }
        if (enableLogging) {
            Log.d(TAG, String.format("[Recover Fragment] tag:%s", new Object[]{tag}));
        }
        SupportOperationManager operationManager = (SupportOperationManager) frag.getOperationManager();
        if (operationManager == null) {
            if (enableLogging) {
                Log.d(TAG, String.format("[Create Manager] tag:%s", new Object[]{tag}));
            }
            operationManager = new SupportOperationManager(callbacks, enableLogging);
            frag.setOperationManager(operationManager);
            return operationManager;
        }
        if (enableLogging) {
            Log.d(TAG, String.format("[Recover Manager] tag:%s", new Object[]{tag}));
        }
        operationManager.mCallbacks = callbacks;
        return operationManager;
    }

    public static SupportOperationManager create(FragmentActivity activity, OperationManagerCallbacks callbacks) {
        return create(activity, callbacks, false);
    }

    private SupportOperationManager(OperationManagerCallbacks callbacks, boolean enableLogging) {
        super(callbacks, enableLogging);
    }
}
