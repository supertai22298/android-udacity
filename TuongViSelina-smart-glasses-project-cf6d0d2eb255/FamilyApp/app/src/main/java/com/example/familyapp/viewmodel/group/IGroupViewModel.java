package com.example.familyapp.viewmodel.group;

import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Group;
import com.example.familyapp.model.Member;
import com.example.familyapp.viewmodel.base.IViewModel;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;

public interface IGroupViewModel extends IViewModel {
    LiveData<Group> myRequestGroup();
    LiveData<ArrayList<Group>> myGroups();
    LiveData<Boolean> isRequestSuccess();
    void updateMembers(ArrayList<Member> members, boolean isNewList);
    void getGroups(int type);
    void getGroupDetails(String groupID);
    void getMembers(String groupID);
    void getGlasses(String groupID);
    void createGroup(String groupID, String groupName);
    void updateGroupName(String name);
    void removeGroup(Group group);
    void confirmInvitations(String groupID, boolean isAccepted);
    void requestJoinGroup(String groupID);
    void inviteMember(String groupID, String memberName, String groupName);
    void removeMember(String groupID, String memberID, OnRequestSuccess<Boolean> listener);
    void leaveGroup(String groupID);
    void changeMemberRole(String groupId, String memberID, int role, OnRequestSuccess<Integer> listener);
}
