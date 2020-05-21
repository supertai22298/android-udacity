package com.example.familyapp.notification;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.contentcapture.ContentCaptureCondition;

import com.example.familyapp.R;
import com.example.familyapp.application.App;
import com.example.familyapp.data.Constant;
import com.example.familyapp.data.notification.INotificationDao;
import com.example.familyapp.data.notification.Notification;
import com.example.familyapp.data.notification.NotificationDao;
import com.example.familyapp.view.Banner;
import com.example.familyapp.view.main.sos.SOSNotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String data = remoteMessage.getData().get(Constant.NOTIFY_DATA_KEY);
        if (data != null && !data.isEmpty()) {
            try {
                showNotification(getApplicationContext(), data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @SuppressLint("RestrictedApi")
    private void showNotification(Context context, String data) throws JSONException {

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject == null) {
            return;
        }

        String title, message;
        String type = jsonObject.getString(Constant.NOTIFY_TYPE_KEY);
        Notification notification = new Notification();
        notification.setType(type);
        notification.setTimestamp(System.currentTimeMillis());
        notification.setId(new Random().nextInt() + "_" + System.currentTimeMillis());
        Resources resources = getBaseContext().getResources();
        resources.getConfiguration().setLocale(new Locale("vi"));
        if (type.equals(Constant.NOTIFY_SOS)) {
            title = resources.getString(R.string.notify_sos_title);
            message = resources.getString(R.string.notify_sos_message);
            String glassesID = jsonObject.getString(Constant.GLASSES_ID_KEY);
            message = String.format(message, glassesID);
            notification.setReferID(glassesID);
        } else {
            title = resources.getString(R.string.notify_group_title);
            String groupID = jsonObject.getString(Constant.GROUP_ID_KEY);
            notification.setReferID(groupID);
            title = String.format(title, groupID);
            if (type.equals(Constant.NOTIFY_UPDATE_GLASSES)) {
                message = resources.getString(R.string.notify_group_update_glasses_message);
                String glassesName = jsonObject.getString(Constant.GLASSES_NAME_KEY);
                message = String.format(message, glassesName);
                notification.setReferValue(glassesName);
            } else if (type.equals(Constant.NOTIFY_INVITATION)) {
                message = resources.getString(R.string.notify_group_invitation_message);
                String groupName = jsonObject.getString(Constant.GROUP_NAME_KEY);
                message = String.format(message, groupName);
                notification.setReferValue(groupName);
            } else {
                switch (type) {
                    case Constant.NOTIFY_ADD_GLASSES:
                        message = resources.getString(R.string.notify_group_add_glasses_message);
                        break;
                    case Constant.NOTIFY_REMOVE_GLASSES:
                        message = resources.getString(R.string.notify_group_remove_glasses_message);
                        break;
                    case Constant.NOTIFY_UPDATE_GROUP:
                        message = resources.getString(R.string.notify_group_update_message);
                        break;
                    case Constant.NOTIFY_QUIT_GROUP:
                        message = resources.getString(R.string.notify_group_quit_message);
                        break;
                    case Constant.NOTIFY_CHANGE_ROLE:
                        message = resources.getString(R.string.notify_group_change_role_message);
                        break;
                    case Constant.NOTIFY_REMOVE_MEMBER:
                        message = resources.getString(R.string.notify_group_remove_member_message);
                        break;
                    default:
                        message = resources.getString(R.string.notify_group_default_message);
                        break;
                }
                Log.e("Message", message);
                message = String.format(message, groupID);
            }
        }
        notification.setTitle(title);
        notification.setMessage(message);
        new NotificationDao().insertNotification(notification);
        if (type.equals(Constant.NOTIFY_SOS)) {
            context.startActivity(new Intent(context, SOSNotificationActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        App.bus().send(notification);
        String channelID = context.getString(R.string.default_notification_channel_id);//rename
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(getApplicationContext(), OutAppNotificationActivity.class);
        intent1.putExtra(OutAppNotificationActivity.DATA, new Event.Notification(type, data));
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), new Random().nextInt(),
                intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        long vibrate = 1000;
        @SuppressLint("WrongConstant") NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setSmallIcon(R.drawable.ic_notification_smart_glasses) //your app icon
                .setBadgeIconType(R.drawable.ic_notification_smart_glasses) //your app icon
                .setChannelId(channelID)
                .setContentTitle(title)
                .setAutoCancel(true).setContentIntent(pendingIntent)
                .setNumber(1)
                .setColor(255)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setVibrate(new long[]{vibrate, vibrate, vibrate, vibrate, vibrate})
                .setLights(Color.RED, 3000, 3000)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentText(message)
                .setWhen(System.currentTimeMillis());

        notificationBuilder.setColor(ContextCompat.getColor(getApplicationContext(), R.color.centerGradient));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        }

        if (notificationManager != null) {
            notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
        }
    }
}
