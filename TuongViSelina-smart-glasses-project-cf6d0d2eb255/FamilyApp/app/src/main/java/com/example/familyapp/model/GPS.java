package com.example.familyapp.model;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GPS implements Serializable {
    @SerializedName("Time")
    @Expose
    private String timeStamp;

    @SerializedName("Location")
    @Expose
    private Location location;

    @SerializedName("Image")
    @Expose
    private String image;

    private String address;
    private Bitmap bitmap;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Location getLocation() {
        return location != null ? location : new Location();
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getImage() {
        Log.e("getImage", image != null ? image : "Temp");
        return image != null ? image : "Temp";
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address != null ? address : "Unknown location";
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
