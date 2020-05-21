package com.example.familyapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Blind implements Serializable {
    @SerializedName("BlindName")
    @Expose
    private String name;
    @SerializedName("BlindYearBorn")
    @Expose
    private String age;
    @SerializedName("BlindAddress")
    @Expose
    private String address;

    public Blind() {
    }

    public Blind(String name, String address, String age) {
        this.name = name;
        this.address = address;
        this.age = age;
    }

    public String getName() {
        return name != null ? name : "No-name";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
