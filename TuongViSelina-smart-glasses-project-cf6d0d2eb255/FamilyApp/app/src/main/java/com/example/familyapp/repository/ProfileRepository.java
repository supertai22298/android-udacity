package com.example.familyapp.repository;

import android.content.Context;

import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Profile;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.LoginResponse;
import com.google.gson.JsonObject;

import io.reactivex.Single;
import retrofit2.http.Body;

/**
 * Created by Sandy on 09/03/2020.
 */
public class ProfileRepository extends BaseRepository {

    public ProfileRepository(Context context) {
        super(context);
    }

    public Single<BaseResponse<Profile>> getProfile() {
        return service.getProfile();
    }

    public Single<BaseResponse<Profile>> updateProfile(String name, String phone, Boolean isUploadAvatar, String file) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("Name", name);
        jsonObject.addProperty("Phone", phone);
        if (isUploadAvatar) {
            jsonObject.addProperty("isUploadAvatar", true);
            jsonObject.addProperty("filetype", "jpg");
            jsonObject.addProperty("filedata", file);
        }
        return service.updateProfile(Prefs.getInstance(context).getAuthentication(), jsonObject);
    }
}
