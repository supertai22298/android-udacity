package com.example.familyapp.repository;

import android.content.Context;

import com.example.familyapp.model.Profile;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.LoginResponse;
import com.google.gson.JsonObject;

import io.reactivex.Single;

/**
 * Created by Sandy on 09/03/2020.
 */
public class SignUpRepository extends BaseRepository {

    public SignUpRepository(Context context) {
        super(context);
    }

    /**
     * Define request body for sign up
     * @param username
     * @param password
     * @param fullName
     * @param phoneNumber
     * @return
     */
    public Single<BaseResponse<LoginResponse>> signUp(String username, String password, String fullName, String phoneNumber) {
        JsonObject body = new JsonObject();
        body.addProperty("Username", username);
        body.addProperty("Password", password);
        body.addProperty("Name", fullName);
        body.addProperty("Phone", phoneNumber);
        return service.signUp(body);
    }

}
