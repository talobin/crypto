package haivo.us.crypto.activity;

import haivo.us.crypto.activity.generic.SimpleFragmentSubActivity;
import android.content.Intent;
import haivo.us.crypto.fragment.SettingsSoundsFragment;

public class SettingsSoundsActivity extends SimpleFragmentSubActivity<SettingsSoundsFragment> {
    protected SettingsSoundsFragment createChildFragment() {
        return new SettingsSoundsFragment();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        (getChildFragment()).onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
