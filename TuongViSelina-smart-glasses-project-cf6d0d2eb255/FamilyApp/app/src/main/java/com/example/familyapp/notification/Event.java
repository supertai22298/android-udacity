package com.example.familyapp.notification;

import com.example.familyapp.data.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Event {
    public static class Notification implements Serializable {
        private String type;
        private String data;

        public Notification() {
        }

        public Notification(String type, String data) {
            this.type = type;
            this.data = data;
        }
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return "Notification{" +
                    "type='" + type + '\'' +
                    ", data='" + data + '\'' +
                    '}';
        }

        public String getReferID(String key) {
            if (data == null) {
                return "";
            }
            String id = "";
            try {
                JSONObject object = new JSONObject(data);
                id = object.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return id;
        }
    }
}
