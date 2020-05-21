package com.example.familyapp.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.familyapp.model.Profile;
import com.example.familyapp.notification.Event;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Locale;

public class Prefs {
    private static Prefs prefs;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String PRE_PROFILE = "profile";
    private static final String PRE_IS_FIRST_USED = "is_first_used";
    private static final String PRE_LANGUAGE = "language";
    private static final String PRE_AUTHENTICATION = "authentication";
    private static final String PRE_NOTIFICATION = "notification";


    public Prefs(Context context) {
        preferences = context.getSharedPreferences("FamilyApp", 0);
        editor = preferences.edit();
    }

    public static synchronized Prefs getInstance(Context context) {
        if (prefs == null) {
            prefs = new Prefs(context);
        }
        return prefs;
    }

    public void saveProfile(String profile) {
        editor.putString(PRE_PROFILE, profile);
        editor.commit();
    }

    public Profile getProfile() {
        return new Gson().fromJson(preferences.getString(PRE_PROFILE, null), new TypeToken<Profile>(){}.getType());
    }

    public void saveLanguage(String language) {
        editor.putString(PRE_LANGUAGE, language);
        editor.commit();
    }

    public String getLanguage() {
        return preferences.getString(PRE_LANGUAGE, Locale.getDefault().getDisplayLanguage());
    }

    public void removeFirstUsed() {
        editor.putBoolean(PRE_IS_FIRST_USED, false);
        editor.commit();
    }

    public boolean isFirstUsed() {
        return preferences.getBoolean(PRE_IS_FIRST_USED, true);
    }

    public void saveAuthentication(String authentication) {
        editor.putString(PRE_AUTHENTICATION, authentication);
        editor.commit();
    }

    public String getAuthentication() {
        return preferences.getString(PRE_AUTHENTICATION, null);
    }

    public void saveNotification(Event.Notification data) {
        editor.putString(PRE_NOTIFICATION, data != null ? new Gson().toJson(data) : null);
        editor.commit();
    }

    public Event.Notification getNotification() {
        String data = preferences.getString(PRE_NOTIFICATION, null);
        if (data != null) {
            return new Gson().fromJson(data, new TypeToken<Event.Notification>(){}.getType());
        }
        return null;
    }

    public void clear() {
        editor.clear().apply();
    }
}
