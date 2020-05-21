package com.example.familyapp.data.notification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Notification extends RealmObject {
    @PrimaryKey
    private String id;
    private String type;
    private String referID;
    private String referValue;
    private String title;
    private String message;
    private boolean isRead;
    private long timestamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReferID() {
        return referID;
    }

    public void setReferID(String referID) {
        this.referID = referID;
    }

    public String getReferValue() {
        return referValue;
    }

    public void setReferValue(String referValue) {
        this.referValue = referValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long expirySOSTime() {
        Calendar calendar = Calendar.getInstance();
        long expiryTime = calendar.getTimeInMillis() - getTimestamp();
        return TimeUnit.MILLISECONDS.toMinutes(expiryTime);
    }
}
