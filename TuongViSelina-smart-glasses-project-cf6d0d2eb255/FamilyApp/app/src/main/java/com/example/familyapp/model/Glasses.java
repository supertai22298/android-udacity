package com.example.familyapp.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Glasses implements Serializable {

    @SerializedName("LinkID")
    @Expose
    private String linkID;

    @SerializedName("GID")
    @Expose
    private String glassesId;

    @SerializedName("GlassesName")
    @Expose
    private String glassesName;

    @SerializedName("Group")
    @Expose
    private Group group;

    @SerializedName("Blind")
    @Expose
    private Blind blind;

    @SerializedName("GPS")
    @Expose
    private GPS gps;

    private boolean isSelected;

    public Glasses() {
    }

    public Glasses(String glassesName, String blindName, String blindAddress, String blindAge) {
        this.glassesName = glassesName;
        blind = new Blind(blindName, blindAddress, blindAge);
    }

    public String getLinkID() {
        return linkID;
    }

    public void setLinkID(String linkID) {
        this.linkID = linkID;
    }

    public String getGlassesId() {
        return glassesId;
    }

    public void setGlassesId(String glassesId) {
        this.glassesId = glassesId;
    }

    public String getGlassesName() {
        return glassesName;
    }

    public String getGlassesShortName() {
        return glassesName != null && glassesName.length() > 0 ? glassesName.substring(0, 1) : "#";
    }

    public void setGlassesName(String glassesName) {
        this.glassesName = glassesName;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Blind getBlind() {
        return blind;
    }

    public void setBlind(Blind blind) {
        this.blind = blind;
    }

    public GPS getGps() {
        return gps != null ? gps : new GPS();
    }

    public void setGps(GPS gps) {
        this.gps = gps;
    }

    @Override
    public String toString() {
        return "Glasses{" +
                "glassName='" + glassesName + '\'' +
                ", blindName='" + blind.getName() + '\'' +
                ", blindAddress='" + blind.getAddress() + '\'' +
                ", blindAge='" + blind.getAge() + '\'' +
                '}';
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public LatLng getPosition() {
        if (gps != null && gps.getLocation() != null) {
            return new LatLng(gps.getLocation().getLat(), gps.getLocation().getLng());
        }
        return new LatLng(0.0, 0.0);
    }
}
