package com.example.familyapp.model;

import android.util.Log;

import com.example.familyapp.data.Constant;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Member implements Serializable {

    public static final int ADMIN = 1;
    public static final int MEMBER = 0;
    public static final int IVITING_MEMBER = -1;
    public static final int IS_NOT_A_MEMBER = -2;

    @SerializedName("user_id")
    @Expose
    private String id;
    @SerializedName("fullname")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("avartar")
    @Expose
    private String avatar;
    @SerializedName("role")
    @Expose
    private int role;

    public Member(String id, String name, String phone, int role) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        Log.e("getAvatar", Constant.BASE_PHOTO_URL + avatar);
        return Constant.BASE_PHOTO_URL + avatar;
    }

    public void setAvartar(String avartar) {
        this.avatar = avartar;
    }
}
