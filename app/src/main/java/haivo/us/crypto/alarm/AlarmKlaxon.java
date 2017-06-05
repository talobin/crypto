package haivo.us.crypto.alarm;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import haivo.us.crypto.content.AlarmRecord;
import haivo.us.crypto.R;
import java.io.IOException;
import haivo.us.crypto.receiver.MarketChecker;
import haivo.us.crypto.util.NotificationUtils;
import haivo.us.crypto.util.TimeUtils;

public class AlarmKlaxon extends Service {
    private static final int DEFAULT_ALARM_TIMEOUT_MINUTES = 10;
    private static final int KILLER = 1000;
    private static final String TAG = "AlarmKlaxon";
    private static final boolean loggingOn = false;
    private static final long[] sVibratePattern;
    private AlarmRecord alarmRecord;
    @SuppressLint({ "HandlerLeak" }) private Handler mHandler;
    private MediaPlayer mMediaPlayer;
    private boolean mPlaying;
    private final BroadcastReceiver mReceiver;
    private long mStartTime;
    private Vibrator mVibrator;

    /* renamed from: haivo.us.crypto.alarm.AlarmKlaxon.1 */
    class C01631 extends Handler {
        C01631() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AlarmKlaxon.KILLER /*1000*/:
                    AlarmKlaxon.this.sendKillBroadcast((AlarmRecord) msg.obj, false);
                    AlarmKlaxon.this.stopSelf();
                    break;
                default:
            }
        }
    }

    /* renamed from: haivo.us.crypto.alarm.AlarmKlaxon.2 */
    class C01642 extends BroadcastReceiver {
        C01642() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(AlarmKlaxon.TAG, "AlarmAlertFullScreen - onReceive " + action);
            long checkerRecordId = intent.getLongExtra(MarketChecker.EXTRA_CHECKER_RECORD_ID, -1);
            if (action.equals(Alarms.ALARM_DISMISS_ACTION)) {
                AlarmKlaxon.this.dismiss(checkerRecordId, false);
            } else {
                AlarmKlaxon.this.dismiss(checkerRecordId, intent.getBooleanExtra(Alarms.ALARM_REPLACED, false));
            }
        }
    }

    /* renamed from: haivo.us.crypto.alarm.AlarmKlaxon.3 */
    class C01653 implements OnErrorListener {
        C01653() {
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            Log.e(AlarmKlaxon.TAG, "Error occurred while playing audio.");
            mp.stop();
            mp.release();
            AlarmKlaxon.this.mMediaPlayer = null;
            return true;
        }
    }

    public AlarmKlaxon() {
        this.mPlaying = false;
        this.mHandler = new C01631();
        this.mReceiver = new C01642();
    }

    static {
        sVibratePattern = new long[] { 500, 500 };
    }

    public void onCreate() {
        this.mVibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        IntentFilter filter = new IntentFilter(Alarms.ALARM_KILLED);
        filter.addAction(Alarms.ALARM_DISMISS_ACTION);
        registerReceiver(this.mReceiver, filter);
        AlarmAlertWakeLock.acquireCpuWakeLock(this);
    }

    public void onDestroy() {
        stop();
        Intent alarmDone = new Intent(Alarms.ALARM_DONE_ACTION);
        if (this.alarmRecord != null) {
            alarmDone.putExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, this.alarmRecord.getId());
        }
        sendBroadcast(alarmDone);
        try {
            unregisterReceiver(this.mReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        AlarmAlertWakeLock.releaseCpuLock();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            stopSelf();
            return START_NOT_STICKY;
        }
        AlarmRecord newAlarmRecord = AlarmRecord.get(intent.getLongExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, -1));
        if (newAlarmRecord == null) {
            Log.v(TAG, "AlarmKlaxon failed to parse the alarm from the intent");
            stopSelf();
            return START_NOT_STICKY;
        }
        if (this.alarmRecord != null) {
            sendKillBroadcast(this.alarmRecord, true);
        }
        play(newAlarmRecord);
        this.alarmRecord = newAlarmRecord;
        return START_STICKY;
    }

    private void dismiss(long checkerRecordId, boolean replaced) {
        if (this.alarmRecord != null && this.alarmRecord.getCheckerId() == checkerRecordId) {
            NotificationUtils.clearAlarmNotificationForAlarmRecord(this, this.alarmRecord.getId());
            if (!replaced) {
                stopSelf();
            }
        }
    }

    private void sendKillBroadcast(AlarmRecord alarmRecord, boolean replaced) {
        dismiss(alarmRecord.getCheckerId(), replaced);
        int minutes = (int) Math.round(((double) (System.currentTimeMillis() - this.mStartTime)) / 60000.0d);
        Intent alarmKilled = new Intent(Alarms.ALARM_KILLED);
        alarmKilled.putExtra(MarketChecker.EXTRA_ALARM_RECORD_ID, alarmRecord.getId());
        alarmKilled.putExtra(Alarms.ALARM_KILLED_TIMEOUT, minutes);
        alarmKilled.putExtra(Alarms.ALARM_REPLACED, replaced);
        sendBroadcast(alarmKilled);
    }

    private void play(AlarmRecord alarmRecord) {
        stop();
        Uri alert = RingtoneManager.getDefaultUri(4);
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnErrorListener(new C01653());
        try {
            this.mMediaPlayer.setDataSource(this, alert);
            startAlarm(this.mMediaPlayer);
        } catch (Exception e) {
            Log.v(TAG, "Using the fallback ringtone");
            try {
                this.mMediaPlayer.reset();
                setDataSourceFromResource(getResources(), this.mMediaPlayer, R.raw.fallbackring);
                startAlarm(this.mMediaPlayer);
            } catch (Exception ex2) {
                Log.e(TAG, "Failed to play fallback ringtone", ex2);
            }
        }
        this.mVibrator.vibrate(sVibratePattern, 0);
        enableKiller(alarmRecord);
        this.mPlaying = true;
        this.mStartTime = System.currentTimeMillis();
    }

    private void startAlarm(MediaPlayer player) throws IOException, IllegalArgumentException, IllegalStateException {
        if (((AudioManager) getSystemService(AUDIO_SERVICE)).getStreamVolume(4) != 0) {
            player.setAudioStreamType(4);
            player.setLooping(true);
            player.prepare();
            player.start();
        }
    }

    private void setDataSourceFromResource(Resources resources, MediaPlayer player, int res) throws IOException {
        AssetFileDescriptor afd = resources.openRawResourceFd(res);
        if (afd != null) {
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
        }
    }

    public void stop() {
        if (this.mPlaying) {
            this.mPlaying = false;
            if (this.mMediaPlayer != null) {
                this.mMediaPlayer.stop();
                this.mMediaPlayer.release();
                this.mMediaPlayer = null;
            }
            this.mVibrator.cancel();
        }
        disableKiller();
    }

    private void enableKiller(AlarmRecord alarmRecord) {
        if (DEFAULT_ALARM_TIMEOUT_MINUTES != -1) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(KILLER, alarmRecord),
                                             ((long) DEFAULT_ALARM_TIMEOUT_MINUTES) * TimeUtils.MILLIS_IN_MINUTE);
        }
    }

    private void disableKiller() {
        this.mHandler.removeMessages(KILLER);
    }

    public static void stopAlarmService(Context context) {
        context.stopService(new Intent(Alarms.ALARM_ALERT_ACTION, null, context, AlarmKlaxon.class));
    }
}
