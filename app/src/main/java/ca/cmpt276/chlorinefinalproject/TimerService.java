package ca.cmpt276.chlorinefinalproject;

import static ca.cmpt276.chlorinefinalproject.App.CHANNEL_ID_ACTIVE;
import static ca.cmpt276.chlorinefinalproject.App.CHANNEL_ID_RING;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

// Service that handles timer running when app is in background or closed
public class TimerService extends Service {

    private final static long COUNT_DOWN_INTERVAL = 1000;
    private final static int ACTIVE_TIMER_NOTIF_ID = 1;
    private final static int TIMER_EXPIRED_NOTIF_ID = 2;
    private final static int INTENT_REQUEST_CODE = 1;

    private CountDownTimer timer;
    public static Boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long remainingTime = intent.getLongExtra(TimerActivity.REMAINING_TIME, 0);
        double timerRate = intent.getDoubleExtra(TimerActivity.TIMER_RATE, 1);
        sendNotification(getString(R.string.timer_running_notification_message), CHANNEL_ID_ACTIVE, ACTIVE_TIMER_NOTIF_ID);
        startTimer(remainingTime, timerRate);
        isRunning = true;

        return START_NOT_STICKY;
    }

    private void sendNotification(String text, String channelID, int ID) {
        Intent notificationIntent = new Intent(this, TimerActivity.class);
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent[] intents = {mainIntent, notificationIntent};

        PendingIntent pendingIntent = PendingIntent.getActivities(this, INTENT_REQUEST_CODE, intents,
                (PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));

        Notification notification = new NotificationCompat.Builder(this, channelID)
                .setContentTitle(getString(R.string.timer_notification_title))
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_baseline_timer_24)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(ID, notification);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        isRunning = false;
        super.onDestroy();
    }

    public static Boolean isRunning(){
        return isRunning;
    }

    public void sendRemainingMs(long ms){
        Intent intent = new Intent();
        intent.putExtra(TimerActivity.REMAINING_TIME, ms);
        intent.setAction(TimerActivity.INTENT_FILTER);
        sendBroadcast(intent);
    }

    private void startTimer(long durationInMillis, double rate){
        long duration = (long) (durationInMillis / rate);
        int interval = (int) (COUNT_DOWN_INTERVAL / rate);
        timer = new CountDownTimer(duration, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                sendRemainingMs((long) (millisUntilFinished * rate));
            }

            @Override
            public void onFinish() {
                sendNotification(getString(R.string.timer_finish_notification_message), CHANNEL_ID_RING, TIMER_EXPIRED_NOTIF_ID);
                sendRemainingMs(0);
            }
        };
        timer.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}