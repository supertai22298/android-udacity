package com.example.familyapp.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.notification.RxBus;
import com.example.familyapp.utils.LanguageUtil;
import com.example.familyapp.utils.Utils;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.multidex.MultiDex;
import io.realm.Realm;

public class App extends Application implements Application.ActivityLifecycleCallbacks {

    private int activityReferences = 0;
    private static boolean activityVisible = true;

    private static RxBus bus;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Realm.init(this);
        bus = new RxBus();
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        createChannel();
        registerActivityLifecycleCallbacks(this);
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = getApplicationContext().getString(R.string.default_notification_channel_id);
            // The user-visible name of the channel.
            CharSequence name = getApplicationContext().getString(R.string.default_notification_channel_name);
            // The user-visible description of the channel.
            String description = "Notifications regarding our products";
            int importance = NotificationManager.IMPORTANCE_MAX;
            @SuppressLint("WrongConstant") NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            long vibrate = 1000;
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{vibrate, vibrate, vibrate, vibrate, vibrate});
            AudioAttributes att = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                    .build();
            mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), att);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(mChannel);
        }
    }

    public static RxBus bus() {
        return bus;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        Utils.changeLanguage(activity);
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (++activityReferences == 1 && !activityVisible) {
            /*App in foreground*/
            activityVisible = true;
        }

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (--activityReferences == 0) {
            /*App in background*/
            activityVisible = false;
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

    public static boolean isAppVisible() {
        return activityVisible;
    }
}
