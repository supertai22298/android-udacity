package com.example.familyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable {
    @SerializedName("Latitude")
    @Expose
    private String lat;

    @SerializedName("Longtitude")
    @Expose
    private String lng;

    public double getLat() {
        return lat != null ? Double.parseDouble(lat) : 0.0;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng != null ? Double.parseDouble(lng) : 0.0;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
