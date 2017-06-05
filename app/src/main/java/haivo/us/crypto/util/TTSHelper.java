package haivo.us.crypto.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import java.util.HashMap;
import java.util.Locale;

public class TTSHelper {
    private static final Object LOCK;
    static final OnAudioFocusChangeListener afChangeListener;
    private static AudioManager audioManager;
    private static Locale defaultLanguageLocale;
    private static String textToBeSpoken;
    private static TextToSpeech tts;

    /* renamed from: haivo.us.crypto.util.TTSHelper.2 */
    static class C02032 extends UtteranceProgressListener {
        C02032() {
        }

        public void onStart(String utteranceId) {
        }

        public void onError(String utteranceId) {
            TTSHelper.onDoneSpeaking();
        }

        public void onDone(String utteranceId) {
            TTSHelper.onDoneSpeaking();
        }
    }

    /* renamed from: haivo.us.crypto.util.TTSHelper.3 */
    static class C02043 implements OnUtteranceCompletedListener {
        C02043() {
        }

        public void onUtteranceCompleted(String utteranceId) {
            TTSHelper.onDoneSpeaking();
        }
    }

    /* renamed from: haivo.us.crypto.util.TTSHelper.4 */
    static class C02054 implements OnAudioFocusChangeListener {
        C02054() {
        }

        public void onAudioFocusChange(int focusChange) {
            if (focusChange != -2 && focusChange != 1 && focusChange == -1) {
                TTSHelper.onDoneSpeaking();
            }
        }
    }

    static {
        tts = null;
        LOCK = new Object();
        afChangeListener = new C02054();
    }

    public static void initTTS(Context context, OnInitListener onInitListener) {
        if (tts == null) {
            tts = new TextToSpeech(context.getApplicationContext(), onInitListener);
        } else {
            onInitListener.onInit(0);
        }
    }

    public static void speak(final android.content.Context context, java.lang.String string2) {
        Object object = LOCK;
        synchronized (object) {
            block10:
            {
                block9:
                {

                    if (!PreferencesUtils.getTTSDisplayOffOnly(context) || !((PowerManager) context.getSystemService(
                        Context.POWER_SERVICE)).isScreenOn()) {
                        break block9;
                    }
                    return;
                }
                if (!PreferencesUtils.getTTSHeadphonesOnly(context.getApplicationContext())
                    || TTSHelper.isHeadsetConnected(context.getApplicationContext())) {
                    break block10;
                }
                return;
            }
            try {
                textToBeSpoken = string2;
                if (tts == null) {
                    tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {

                        public void onInit(int n) {
                            if (TTSHelper.isStatusSuccess(n)) {
                                defaultLanguageLocale = tts.getLanguage();
                                TTSHelper.speakAfterInit(context, textToBeSpoken);
                                return;
                            }
                            Log.d((String) "TTS", (String) "Initilization Failed!");
                            defaultLanguageLocale = null;
                            tts = null;
                        }
                    });
                } else {
                    TTSHelper.speakAfterInit(context, textToBeSpoken);
                }
            } catch (Throwable var0_1) {
                var0_1.printStackTrace();
            }
            return;
        }
    }

    public static boolean isStatusSuccess(int status) {
        return status == 0;
    }

    private static boolean setLanguageAndCheck(Locale locale) {
        int result = tts.setLanguage(locale);
        return (result == -1 || result == -2) ? false : true;
    }

    private static void speakAfterInit(Context appContext, String text) {
        if (tts != null) {
            try {
                audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
                if (!setLanguageAndCheck(Locale.ENGLISH)) {
                    Log.d("TTS", "ENGLISH language is not supported, setting default instead.");
                    if (defaultLanguageLocale == null || !setLanguageAndCheck(defaultLanguageLocale)) {
                        Log.d("TTS", "Error while setting default language!");
                        return;
                    }
                }
                int streamId = getProperAudioStream(appContext);
                int requestAudioForucResult = audioManager.requestAudioFocus(afChangeListener, streamId, 3);
                setTTSListeners();
                HashMap<String, String> myHashParams = new HashMap();
                myHashParams.put("utteranceId", "DONE");
                myHashParams.put("streamType", String.valueOf(streamId));
                tts.speak(text, 1, myHashParams);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(15)
    private static void setTTSListeners() {
        if (VERSION.SDK_INT < 15) {
            tts.setOnUtteranceCompletedListener(new C02043());
        } else if (tts.setOnUtteranceProgressListener(new C02032()) != 0) {
            onDoneSpeaking();
        }
    }

    public static void stopSpeaking() {
        if (tts != null) {
            tts.stop();
        }
    }

    private static void onDoneSpeaking() {
        if (audioManager != null) {
            audioManager.abandonAudioFocus(afChangeListener);
        }
    }

    public static int getProperAudioStream(Context context) {
        return isHeadsetConnected(context) ? 3 : PreferencesUtils.getTTSAdvancedStream(context);
    }

    public static boolean isHeadsetConnected(Context appContext) {
        AudioManager audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.isWiredHeadsetOn() || audioManager.isBluetoothA2dpOn();
    }
}
