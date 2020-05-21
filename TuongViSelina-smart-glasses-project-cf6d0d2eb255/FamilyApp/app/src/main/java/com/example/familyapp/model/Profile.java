package com.example.familyapp.model;

import android.util.Log;

import com.example.familyapp.data.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Profile {
    @SerializedName("UID")
    @Expose
    private String id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Username")
    @Expose
    private String username;
    @SerializedName("Phone")
    @Expose
    private String phone;
    @SerializedName("Avatar")
    @Expose
    private String avatar;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        Log.e("getAvatar", Constant.BASE_PHOTO_URL + avatar);
        return Constant.BASE_PHOTO_URL + avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
