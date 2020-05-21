package com.example.familyapp.data.notification;

import android.util.Log;

import com.example.familyapp.data.Constant;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class NotificationDao implements INotificationDao {

    private static final String TIMESTAMP = "timestamp";
    private static final String TYPE = "type";
    private static final String ID = "id";

    private static NotificationDao dao;
    private Realm realm;

    public NotificationDao() {
        realm = Realm.getDefaultInstance();
    }

    public static synchronized INotificationDao getInstance() {
        if (dao == null) {
            dao = new NotificationDao();
        }
        return dao;
    }

    @Override
    public void insertNotification(final Notification notification) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm r) {
                r.insert(notification);
                Log.e("insertNotification", new Gson().toJson(notification));
            }
        });
    }

    @Override
    public void markAsRead(final String id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NonNull Realm r) {
                Notification result = r.where(Notification.class)
                        .equalTo(ID, id)
                        .findFirst();
                if (result != null) {
                    result.setRead(true);
                }
            }
        });
    }

    @Override
    public ArrayList<Notification> getNotifications(String type) {
        RealmResults<Notification> results;
        if (type == null) {
            results = realm.where(Notification.class)
                    .notEqualTo(TYPE, Constant.NOTIFY_SOS)
                    .findAllSorted(TIMESTAMP, Sort.DESCENDING);
        } else {
            results = realm.where(Notification.class)
                    .equalTo(TYPE, Constant.NOTIFY_SOS)
                    .findAllSorted(TIMESTAMP, Sort.DESCENDING);
        }
        return (ArrayList<Notification>) realm.copyFromRealm(results);
    }

    @Override
    public Notification getNotificationById(String id) {
        return realm.where(Notification.class)
                .equalTo("id", id)
                .findFirst();
    }
}
