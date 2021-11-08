package ca.cmpt276.chlorinefinalproject;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;

public class App extends Application {

    public static final String CHANNEL_ID_ACTIVE = "TimerActiveChannel";
    public static final String CHANNEL_ID_RING = "TimerRingChannel";
    public static final String TIMER_NOTIFICATION_CHANNEL = "Active Timer";
    public static final String TIMER_RING_CHANNEL = "Timer Ringing";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannelSilent();
        createNotificationChannelLoud();
    }

    private void createNotificationChannelSilent() {
        NotificationChannel activeTimerChannel = new NotificationChannel(CHANNEL_ID_ACTIVE, TIMER_NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_LOW);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(activeTimerChannel);
    }

    private void createNotificationChannelLoud() {
        NotificationChannel timerRingChannel = new NotificationChannel(CHANNEL_ID_RING, TIMER_RING_CHANNEL, NotificationManager.IMPORTANCE_HIGH);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        timerRingChannel.setSound(soundUri, audioAttributes);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(timerRingChannel);
    }
}