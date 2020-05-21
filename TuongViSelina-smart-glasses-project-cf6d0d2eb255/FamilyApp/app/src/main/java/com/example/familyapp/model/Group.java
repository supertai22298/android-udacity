package com.example.familyapp.model;

import android.content.Context;

import com.example.familyapp.R;
import com.example.familyapp.data.Constant;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.response.BaseResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Group implements Serializable {

    @SerializedName("GroupID")
    @Expose
    private String id;

    @SerializedName("GroupName")
    @Expose
    private String name;

    @SerializedName("MemberCount")
    @Expose
    private int count = 1;

    @SerializedName("CountGlasses")
    @Expose
    private int countGlass = 0;

    @SerializedName("AdminName")
    @Expose
    private String adminName = null;

    @SerializedName("members")
    @Expose
    private ArrayList<Member> members;

    @SerializedName("glasses")
    @Expose
    private ArrayList<Glasses> glasses;

    public Group(String id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public Group(String name, ArrayList<Member> members) {
        this.name = name;
        this.members = members;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCountGlass() {
        return countGlass;
    }

    public void setCountGlass(int countGlass) {
        this.countGlass = countGlass;
    }

    public ArrayList<Glasses> getGlasses() {
        if (glasses == null) {
            glasses = new ArrayList<>();
        }
        return glasses;
    }

    public void setGlasses(ArrayList<Glasses> glasses) {
        this.glasses = glasses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Member> getMembers() {
        if (members == null) {
            return new ArrayList<>();
        }
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public String getName(){
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountMemberText(Context context) {
        if (adminName != null) {
            return String.format(context.getString(R.string.tv_admin_invitation), adminName);
        } else {
            String content = count + " " + context.getString(R.string.tv_role_member).toLowerCase();
            if (count != 1 && Prefs.getInstance(context).getLanguage().equals(Constant.LANGUAGE_EN)) {
                content = content.concat("s");
            }
            return content;
        }
    }

    public String getFirstCharacter() {
        if (name != null && name.length() > 0) {
            return name.substring(0, 1);
        }
        return "#";
    }

    public boolean isHasGlass() {
        return countGlass != 0;
    }
}
