package com.example.familyapp.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.familyapp.application.App;
import com.example.familyapp.data.Constant;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.view.main.SplashActivity;
import com.example.familyapp.view.main.glasses.GlassesDetailsActivity;
import com.example.familyapp.view.main.group.GroupDetailsActivity;
import com.example.familyapp.view.main.sos.SOSDetailsActivity;
import com.example.familyapp.view.main.sos.SOSNotificationActivity;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OutAppNotificationActivity extends AppCompatActivity {

    public static final String DATA = "data";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            Event.Notification data = (Event.Notification) getIntent().getSerializableExtra(DATA);
            if (data != null) {
                if (App.isAppVisible()) {
                    startNotificationEvent(this, data);
                } else {
                    Prefs.getInstance(this).saveNotification(data);
                    startActivity(new Intent(this, SplashActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | Intent.FLAG_ACTIVITY_NEW_TASK
                                    | Intent.FLAG_ACTIVITY_NO_HISTORY
                            ));
                }
            }
        }
        finish();
    }

    public static void startNotificationEvent(Context context, Event.Notification data) {
        switch (data.getType()) {
            case Constant.NOTIFY_SOS:
                context.startActivity(new Intent(context, SOSDetailsActivity.class)
                        .putExtra(DATA, data.getReferID(Constant.GLASSES_ID_KEY))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
            default:
                context.startActivity(new Intent(context, GroupDetailsActivity.class)
                        .putExtra(GroupDetailsActivity.GROUP_ID, data.getReferID(Constant.GROUP_ID_KEY))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                break;
        }
    }
}
