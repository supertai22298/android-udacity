package com.example.familyapp.repository;

import android.content.Context;

import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.response.AddGlassResponse;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.VerifyCodeResponse;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.reactivex.Single;

public class GlassesRespository extends BaseRepository {
    public GlassesRespository(Context context) {
        super(context);
    }

    public Single<BaseResponse<AddGlassResponse>> createGlasses(String groupID, String glassesID, String glassesName,
                                                                String blindName, String blindAddress, String blindAge) {
        JsonObject body = new JsonObject();
        body.addProperty("groupID", groupID);
        body.addProperty("glassesID", glassesID);
        body.addProperty("glassesName", glassesName);
        body.addProperty("blindName", blindName);
        body.addProperty("blindAddress", blindAddress);
        body.addProperty("blindYearBorn", blindAge);
        return service.createGlasses(Prefs.getInstance(context).getAuthentication(), body);
    }

    public Single<BaseResponse<Glasses>> verifyGlasses(String linkID, String code) {
        JsonObject body = new JsonObject();
        body.addProperty("linkID", linkID);
        body.addProperty("verifyCode", code);
        return service.verifyGlasses(body);
    }

    public Single<BaseResponse<Glasses>> updateGlasses(Glasses glasses) {
        JsonObject body = new JsonObject();
        body.addProperty("linkID", glasses.getLinkID());
        body.addProperty("glassesName", glasses.getGlassesName());
        body.addProperty("blindName", glasses.getBlind().getName());
        body.addProperty("blindAddress", glasses.getBlind().getAddress());
        body.addProperty("blindYearBorn", glasses.getBlind().getAge());
        return  service.updateGlasses(body);
    }

    public Single<BaseResponse<ArrayList<Glasses>>> getGlasses() {
        return service.getGlasses();
    }

    public Single<BaseResponse<ArrayList<String>>> getSOSImages(String glassesID) {
        return service.getGlassesDetails(glassesID, 1);
    }

    public Single<BaseResponse<Glasses>> removeGlasses(String linkID) {
        return service.removeGlasses(linkID);
    }
}
