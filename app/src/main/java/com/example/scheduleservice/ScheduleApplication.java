package com.example.scheduleservice;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ScheduleApplication extends Application {
    public static final String CHANNEL_ID = "ScheduleNotificationChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "NotificationChannel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("Notification for new schedule available");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }
}
