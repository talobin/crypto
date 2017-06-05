package haivo.us.crypto.activity.generic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import haivo.us.crypto.R;
import haivo.us.crypto.util.TTSHelper;

public abstract class SimpleFragmentActivity<T extends Fragment> extends ActionBarActivity {
    private static final String CHILD_FRAGMENT_TAG = "CHILD_FRAGMENT";
    private static final IntentFilter audioHeadsetChangedFilter;
    private final BroadcastReceiver audioHeadsetChangedReceiver;
    protected T childFragment;

    /* renamed from: haivo.us.crypto.activity.generic.SimpleFragmentActivity.1 */
    class C01571 extends BroadcastReceiver {
        C01571() {
        }

        public void onReceive(Context context, Intent intent) {
            SimpleFragmentActivity.this.setVolumeControlStream(TTSHelper.getProperAudioStream(context));
        }
    }

    protected abstract T createChildFragment();

    public SimpleFragmentActivity() {
        this.audioHeadsetChangedReceiver = new C01571();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        this.childFragment = (T) getSupportFragmentManager().findFragmentByTag(CHILD_FRAGMENT_TAG);
        if (this.childFragment == null) {
            this.childFragment = createChildFragment();
            if (this.childFragment != null) {
                FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                mFragmentTransaction.replace(R.id.content, this.childFragment, CHILD_FRAGMENT_TAG);
                mFragmentTransaction.commit();
            }
        }
    }

    static {
        audioHeadsetChangedFilter = new IntentFilter("android.intent.action.HEADSET_PLUG");
    }

    protected void onStart() {
        super.onStart();
        registerReceiver(this.audioHeadsetChangedReceiver, audioHeadsetChangedFilter);
        setVolumeControlStream(TTSHelper.getProperAudioStream(this));
    }

    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(this.audioHeadsetChangedReceiver);
        } catch (Exception e) {
        }
    }

    protected int getContentViewResId() {
        return R.layout.fragment_activity;
    }

    public T getChildFragment() {
        return this.childFragment;
    }
}
