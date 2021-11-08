package ca.cmpt276.chlorinefinalproject;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class App extends Application{

    public static final  String CHANNEL_ID = "TimerServiceChannel";
    public static final String TIMER_NOTIFICATION_CHANNEL = "Timer Notification Channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        NotificationChannel timerChannel = new NotificationChannel(CHANNEL_ID, TIMER_NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
        timerChannel.setSound(null, null);

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(timerChannel);
    }
}
