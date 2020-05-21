package com.example.familyapp.repository;

import android.content.Context;

import com.example.familyapp.model.request.LoginBody;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.LoginResponse;
import com.google.gson.JsonObject;

import io.reactivex.Single;

/**
 * Created by Sandy on 09/03/2020.
 */
public class LoginRepository extends BaseRepository {

    public LoginRepository(Context context) {
        super(context);
    }

    public Single<BaseResponse<LoginResponse>> login(String username, String password) {
        return service.login(new LoginBody(username, password));
    }
}
