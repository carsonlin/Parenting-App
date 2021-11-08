package ca.cmpt276.chlorinefinalproject;

import static ca.cmpt276.chlorinefinalproject.App.CHANNEL_ID_ACTIVE;
import static ca.cmpt276.chlorinefinalproject.App.CHANNEL_ID_RING;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;

public class TimerService extends Service {

    private final static long COUNT_DOWN_INTERVAL = 1000;

    private CountDownTimer timer;
    public static Boolean isRunning = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        long remainingTime = intent.getLongExtra(TimerActivity.REMAINING_TIME, 0);
        sendNotification("Timer running!", CHANNEL_ID_ACTIVE, 1);
        startTimer(remainingTime);
        isRunning = true;

        return START_NOT_STICKY;
    }

    private void sendNotification(String text, String channelID, int ID) {
        Intent notificationIntent = new Intent(this, TimerActivity.class);
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent[] intents = {mainIntent, notificationIntent};

        PendingIntent pendingIntent = PendingIntent.getActivities(this, 3, intents, (PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT));

        Notification notification = new NotificationCompat.Builder(this, channelID)
                .setContentTitle("Timeout Timer")
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

    private void startTimer(long durationInMillis){
        timer = new CountDownTimer(durationInMillis, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                sendRemainingMs(millisUntilFinished);
            }

            @Override
            public void onFinish() {
                sendNotification("Timer is up!", CHANNEL_ID_RING, 2);
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