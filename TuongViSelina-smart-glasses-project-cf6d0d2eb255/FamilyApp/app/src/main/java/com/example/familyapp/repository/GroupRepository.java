package com.example.familyapp.repository;

import android.content.Context;

import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Group;
import com.example.familyapp.model.Member;
import com.example.familyapp.model.Profile;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.CreateGroupResponse;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Sandy on 09/03/2020.
 */
public class GroupRepository extends BaseRepository {
    public GroupRepository(Context context) {
        super(context);
    }

    public Single<BaseResponse<Group>> getGroupDetails(String groupID) {
        return service.getGroupsDetails(groupID);
    }

    public Single<BaseResponse<ArrayList<Group>>> getGroups() {
        return service.getGroups();
    }

    public Single<BaseResponse<ArrayList<Group>>> getInvitations() {
        return service.getInvitations();
    }

    public Single<BaseResponse<ArrayList<Member>>> getGroupMembers(String groupID) {
        return service.getGroupMembers(groupID);
    }

    public Single<BaseResponse<ArrayList<Glasses>>> getGlasses(String groupID) {
        return service.getGlasses();
    }

    public Single<BaseResponse<CreateGroupResponse>> createGroup(String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("groupName", name);
        return service.createGroup(Prefs.getInstance(context).getAuthentication(), jsonObject);
    }
    public Single<BaseResponse<CreateGroupResponse>> updateGroup(String groupID, String name) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("groupID", groupID);
        jsonObject.addProperty("groupName", name);
        return service.updateGroup(jsonObject);
    }

    public Single<BaseResponse<Group>> removeGroup(String groupId) {
        return service.removeGroup(groupId);
    }

    public Single<BaseResponse<Member>> inviteMember(String groupId, String memberName, String groupName) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("groupID", groupId);
        jsonObject.addProperty("memberUsername", memberName);
        jsonObject.addProperty("groupName", groupName);
        return service.inviteMember(jsonObject);
    }

    public Single<BaseResponse<Member>> removeMember(String groupId, String memberID) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("groupID", groupId);
        jsonObject.addProperty("memberID", memberID);
        return service.removeMember(jsonObject);
    }

    public Single<BaseResponse<Member>> leaveGroup(String groupId) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("groupID", groupId);
        return service.leaveGroup(jsonObject);
    }

    public Single<BaseResponse<Member>> changeMemberRole(String groupId, String memberID, int role) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("groupID", groupId);
        jsonObject.addProperty("memberID", memberID);
        jsonObject.addProperty("role", role);
        return service.changeMemberRole(jsonObject);
    }

    public Single<BaseResponse<Group>> confirmInvitation(String groupID, boolean isAccepted) {
        return isAccepted ? service.acceptJoinGroup(groupID) : service.rejectJoinGroup(groupID);
    }

    public Single<BaseResponse<Group>> requestJoinGroup(String groupID) {
        return service.requestJoinGroup(groupID);
    }
}
