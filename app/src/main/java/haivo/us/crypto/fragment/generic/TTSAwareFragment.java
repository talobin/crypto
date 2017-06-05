package haivo.us.crypto.fragment.generic;

import android.os.Bundle;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.preference.PreferenceFragment;
import haivo.us.crypto.util.TTSHelper;

public class TTSAwareFragment extends PreferenceFragment implements OnInitListener {
    private boolean isTTSAvailable;

    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        onCreateBeforeInitTTS(paramBundle);
        TTSHelper.initTTS(getActivity(), this);
    }

    protected void onCreateBeforeInitTTS(Bundle paramBundle) {
    }

    protected boolean isTTSAvailable() {
        return this.isTTSAvailable;
    }

    protected void onTTSAvailable(boolean available) {
    }

    public final void onInit(int status) {
        this.isTTSAvailable = TTSHelper.isStatusSuccess(status);
        onTTSAvailable(this.isTTSAvailable);
    }
}
