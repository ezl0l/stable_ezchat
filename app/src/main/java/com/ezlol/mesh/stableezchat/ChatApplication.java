package com.ezlol.mesh.stableezchat;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.ezlol.mesh.stableezchat.model.AccessToken;
import com.ezlol.mesh.stableezchat.service.LongPollService;
import com.google.gson.Gson;

public class ChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        NotificationChannel channel = new NotificationChannel(Config.DEFAULT_NOTIFICATION_CHANNEL,
                "Notifications",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
        notificationManager.createNotificationChannel(new NotificationChannel(Config.TRAY_NOTIFICATION_CHANNEL,
                "Tray",
                NotificationManager.IMPORTANCE_NONE));

        String accessTokenJson = PreferenceManager.getDefaultSharedPreferences(this)
                .getString("accessToken", null);
        if(accessTokenJson != null) {
            AccessToken accessToken = new Gson().fromJson(accessTokenJson, AccessToken.class);
            if (accessToken != null) {
                startForegroundService(new Intent(this, LongPollService.class)
                        .putExtra("accessToken", accessTokenJson));
            }
        }
    }
}
