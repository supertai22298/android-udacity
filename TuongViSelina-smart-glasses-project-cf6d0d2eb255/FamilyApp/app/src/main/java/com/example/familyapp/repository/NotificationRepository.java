package com.example.familyapp.repository;

import android.content.Context;

import com.example.familyapp.model.request.LoginBody;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.LoginResponse;
import com.google.gson.JsonObject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Sandy on 09/03/2020.
 */
public class NotificationRepository extends BaseRepository {

    public NotificationRepository(Context context) {
        super(context);
    }

    public Single<BaseResponse<Object>> updateDeviceToken(String token) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("notificationToken", token);
        return service.updateDeviceToken(jsonObject);
    }
}
