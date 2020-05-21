package com.example.familyapp.network;

import android.util.Log;

import com.example.familyapp.model.Direction;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Group;
import com.example.familyapp.model.Member;
import com.example.familyapp.model.Profile;
import com.example.familyapp.model.request.LoginBody;
import com.example.familyapp.model.response.AddGlassResponse;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.CreateGroupResponse;
import com.example.familyapp.model.response.LoginResponse;
import com.example.familyapp.model.response.VerifyCodeResponse;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APIs {

    @POST("users/signin")
    Single<BaseResponse<LoginResponse>> login(@Body LoginBody body);

    /**
     * Request sign up with call back response is Login Response Data
     * @param body
     * @return
     */
    @POST("users")
    Single<BaseResponse<LoginResponse>> signUp(@Body JsonObject body);

    @GET("users")
    Single<BaseResponse<Profile>> getProfile();

    @PUT("users")
    Single<BaseResponse<Profile>> updateProfile(@Header("Authorization") String jwt, @Body JsonObject jsonObject);

    @POST("groups")
    Single<BaseResponse<CreateGroupResponse>> createGroup(@Header("Authorization") String jwt, @Body JsonObject body);

    @PUT("groups")
    Single<BaseResponse<CreateGroupResponse>> updateGroup(@Body JsonObject body);

    @DELETE("groups")
    Single<BaseResponse<Group>> removeGroup(@Query("groupID") String id);

    @GET("groups")
    Single<BaseResponse<ArrayList<Group>>> getGroups();

    @GET("invitations")
    Single<BaseResponse<ArrayList<Group>>> getInvitations();

    @GET("groups")
    Single<BaseResponse<Group>> getGroupsDetails(@Query("groupID") String id);

    @GET("groups/members")
    Single<BaseResponse<ArrayList<Member>>> getGroupMembers(@Query("groupID") String id);

    @GET("groups")
    Single<BaseResponse<Group>> requestJoinGroup(@Query("groupID") String id);

    @PUT("invitations")
    Single<BaseResponse<Group>> acceptJoinGroup(@Query("groupID") String id);

    @DELETE("invitations")
    Single<BaseResponse<Group>> rejectJoinGroup(@Query("groupID") String id);

    @POST("groups/members")
    Single<BaseResponse<Member>> inviteMember(@Body JsonObject body);

    @HTTP(method = "DELETE", path = "groups/members", hasBody = true)
    Single<BaseResponse<Member>> removeMember(@Body JsonObject body);

    @PUT("groups/quit")
    Single<BaseResponse<Member>> leaveGroup(@Body JsonObject body);

    @PUT("groups/members")
    Single<BaseResponse<Member>> changeMemberRole(@Body JsonObject body);

    @POST("glasses")
    Single<BaseResponse<AddGlassResponse>> createGlasses(@Header("Authorization") String jwt, @Body JsonObject body);

    @POST("verify")
    Single<BaseResponse<Glasses>> verifyGlasses(@Body JsonObject body);

    @PUT("glasses")
    Single<BaseResponse<Glasses>> updateGlasses(@Body JsonObject body);

    @DELETE("glasses")
    Single<BaseResponse<Glasses>> removeGlasses(@Query("linkID") String linkID);

    @GET("glasses/data")
    Single<BaseResponse<ArrayList<String>>> getGlassesDetails(@Query("gid") String id,@Query("limit") int limit);

    @GET("glasses")
    Single<BaseResponse<ArrayList<Glasses>>> getGlasses();

    @GET("notifications")
    Single<BaseResponse<String>> getNotifications();

    @GET("https://maps.googleapis.com/maps/api/directions/json")
    Single<Direction> getDirection(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String key
    );

    @PUT("notification")
    Single<BaseResponse<Object>> updateDeviceToken(@Body JsonObject jsonObject);

}
