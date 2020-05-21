package com.example.familyapp.viewmodel.group;

import android.content.Context;

import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Group;
import com.example.familyapp.model.Member;
import com.example.familyapp.model.Profile;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.CreateGroupResponse;
import com.example.familyapp.repository.GroupRepository;
import com.example.familyapp.view.main.group.GroupAdapter;
import com.example.familyapp.viewmodel.base.BaseViewModel;
import com.example.familyapp.viewmodel.base.ResponseHandler;
import com.example.familyapp.viewmodel.base.ResponseHandlerListener;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GroupViewModel extends BaseViewModel implements IGroupViewModel {

    private GroupRepository repository;
    private MutableLiveData<Group> _myRequestGroup = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Group>> _myGroups = new MutableLiveData<>();
    private MutableLiveData<Boolean> _isRequestSuccess = new MutableLiveData<>();

    public GroupViewModel(Context context) {
        super(context);
        repository = new GroupRepository(context);
    }

    @Override
    public LiveData<Group> myRequestGroup() {
        return _myRequestGroup;
    }

    @Override
    public LiveData<ArrayList<Group>> myGroups() {
        return _myGroups;
    }

    @Override
    public LiveData<Boolean> isRequestSuccess() {
        return _isRequestSuccess;
    }

    @Override
    public void updateGroupName(String name) {
        new ResponseHandler<CreateGroupResponse>(this).setRequest(
                repository.updateGroup(_myRequestGroup.getValue().getId(), name),
                null
        );
        Group group = _myRequestGroup.getValue();
        if (group != null) {
            group.setName(name);
            _myRequestGroup.postValue(group);
        }
    }

    @Override
    public void getGroupDetails(final String groupID) {
        repository.getGroupDetails(groupID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<BaseResponse<Group>, SingleSource<BaseResponse<ArrayList<Member>>>>() {
                    @Override
                    public SingleSource<BaseResponse<ArrayList<Member>>> apply(BaseResponse<Group> groupBaseResponse) {
                        _myRequestGroup.postValue(groupBaseResponse.getData());
                        return repository.getGroupMembers(groupID)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .flatMap(new Function<BaseResponse<ArrayList<Member>>, SingleSource<BaseResponse<ArrayList<Glasses>>>>() {
                    @Override
                    public SingleSource<BaseResponse<ArrayList<Glasses>>> apply(BaseResponse<ArrayList<Member>> membersResponse) throws Exception {
                        updateMembers(membersResponse.getData(), true);
                        return repository.getGlasses(groupID)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .doFinally(new Action() {
                                    @Override
                                    public void run() throws Exception {
                                        isLoading.postValue(false);
                                    }
                                });
                    }
                })
                .subscribe(new SingleObserver<BaseResponse<ArrayList<Glasses>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        /* Notify that view should show loading dialog */
                        isLoading.postValue(true);
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(BaseResponse<ArrayList<Glasses>> response) {
                        updateGlasses(response.getData());
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.postValue(e.getMessage());
                    }
                });
    }

    @Override
    public void getMembers(String groupID) {
        new ResponseHandler<ArrayList<Member>>(this).setRequest(
                repository.getGroupMembers(groupID),
                new ResponseHandlerListener<ArrayList<Member>>() {

                    @Override
                    public void onSuccess(BaseResponse<ArrayList<Member>> response) {
                        updateMembers(response.getData(), true);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {

                    }
                });
    }

    @Override
    public void getGlasses(String groupID) {
        new ResponseHandler<ArrayList<Glasses>>(this).setRequest(
                repository.getGlasses(groupID),
                new ResponseHandlerListener<ArrayList<Glasses>>() {

                    @Override
                    public void onSuccess(BaseResponse<ArrayList<Glasses>> response) {
                        updateGlasses(response.getData());
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                    }
                });
    }

    @Override
    public void updateMembers(ArrayList<Member> members, boolean isNewList) {
        Group group = _myRequestGroup.getValue();
        if (group != null) {
            group.setMembers(members);
            _myRequestGroup.postValue(group);
        }
    }

    private void updateGlasses(ArrayList<Glasses> glasses) {
        Group group = _myRequestGroup.getValue();
        if (group != null && glasses != null) {
            ArrayList<Glasses> list = new ArrayList<>();
            for (int i = 0; i < glasses.size(); i ++) {
                if (glasses.get(i).getGroup().getId().equals(group.getId())) {
                    list.add(glasses.get(i));
                }
            }
            group.setGlasses(list);
            _myRequestGroup.postValue(group);
        }
    }

    private void removeGroup(String groupID) {
        ArrayList<Group> groups = _myGroups.getValue();
        if (groups != null && !groups.isEmpty()) {
            int index = -1;
            for (int i = 0; i < groups.size(); i ++) {
                if (groups.get(i).getId().equals(groupID)) {
                    index = i;
                    break;
                }
            }
            if (index != -1) {
                groups.remove(index);
                _myGroups.postValue(groups);
            }
        }
    }

    @Override
    public void getGroups(int type) {
        new ResponseHandler<ArrayList<Group>>(this).setRequest(
                type == GroupAdapter.MY_GROUP ? repository.getGroups(): repository.getInvitations(),
                new ResponseHandlerListener<ArrayList<Group>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<Group>> response) {
                        _myGroups.postValue(response.getData());
                    }

                    @Override
                    public void onApiFailed(Throwable t) {

                    }
                });
    }

    @Override
    public void createGroup(final String groupID, final String groupName) {
        new ResponseHandler<CreateGroupResponse>(this).setRequest(
                        groupID != null ? repository.updateGroup(groupID, groupName) : repository.createGroup(groupName),
                        new ResponseHandlerListener<CreateGroupResponse>() {
                    @Override
                    public void onSuccess(BaseResponse<CreateGroupResponse> response) {
                        Group group = new Group(groupID != null ? groupID: response.getData().getGroupID(), groupName, 1);
                        _myRequestGroup.postValue(group);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(t.getMessage());
                    }
                });
    }

    @Override
    public void removeGroup(final Group group) {
        new ResponseHandler<Group>(this)
                .setRequest(repository.removeGroup(group.getId()), new ResponseHandlerListener<Group>() {
                    @Override
                    public void onSuccess(BaseResponse<Group> response) {
                        _myRequestGroup.postValue(null);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(t.getMessage());
                    }
                });
    }

    @Override
    public void confirmInvitations(final String groupID, final boolean isAccepted) {
        new ResponseHandler<Group>(this)
                .setRequest(repository.confirmInvitation(groupID, isAccepted), new ResponseHandlerListener<Group>() {
                    @Override
                    public void onSuccess(BaseResponse<Group> response) {
                        Group group = _myRequestGroup.getValue();
                        if (group != null) {
                            if (isAccepted) {
                                Profile profile = Prefs.getInstance(context).getProfile();
                                if (profile != null) {
                                    for (Member member : group.getMembers()) {
                                        if (member.getId().equals(profile.getId())) {
                                            member.setRole(Member.MEMBER);
                                            break;
                                        }
                                    }
                                    _myRequestGroup.postValue(group);
                                }
                            } else {
                                _myRequestGroup.postValue(null);
                            }
                            return;
                        }
                        removeGroup(groupID);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(t.getMessage());
                    }
                });
    }

    @Override
    public void requestJoinGroup(final String groupID) {
        new ResponseHandler<Group>(this)
                .setRequest(repository.requestJoinGroup(groupID), new ResponseHandlerListener<Group>() {
                    @Override
                    public void onSuccess(BaseResponse<Group> response) {
                        removeGroup(groupID);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(t.getMessage());
                    }
                });
    }

    @Override
    public void inviteMember(String groupID, String memberName, String groupName) {
        new ResponseHandler<Member>(this)
                .setRequest(repository.inviteMember(groupID, memberName, groupName), new ResponseHandlerListener<Member>() {
                    @Override
                    public void onSuccess(BaseResponse<Member> response) {
                        _isRequestSuccess.postValue(true);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(t.getMessage());
                    }
                });
    }

    @Override
    public void removeMember(String groupID, String memberID, final OnRequestSuccess<Boolean> listener) {
        if (listener == null) {
            Group group = _myRequestGroup.getValue();
            if (group != null) {
                ArrayList<Member> members = group.getMembers();
                if (members != null && !members.isEmpty()) {
                    int index = -1;
                    for (int i = 0; i < members.size(); i ++) {
                        if (members.get(i).getId().equals(memberID)) {
                            index = i; break;
                        }
                    }
                    if (index != -1) {
                        members.remove(index);
                        group.setMembers(members);
                        _myRequestGroup.postValue(group);
                    }
                }
            }
            return;
        }
        new ResponseHandler<Member>(this)
                .setRequest(repository.removeMember(groupID, memberID), new ResponseHandlerListener<Member>() {
                    @Override
                    public void onSuccess(BaseResponse<Member> response) {
                        isLoading.postValue(false);
                        listener.onSuccess(true);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(t.getMessage());
                    }
                });
    }

    @Override
    public void leaveGroup(String groupID) {
        new ResponseHandler<Member>(this)
                .setRequest(repository.leaveGroup(groupID), new ResponseHandlerListener<Member>() {
                    @Override
                    public void onSuccess(BaseResponse<Member> response) {
                        _myRequestGroup.postValue(null);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(t.getMessage());
                    }
                });
    }

    @Override
    public void changeMemberRole(String groupID, String memberID, final int role, final OnRequestSuccess<Integer> listener) {
        if (listener == null) {
            Group group = _myRequestGroup.getValue();
            if (group != null) {
                ArrayList<Member> members = group.getMembers();
                if (members != null && !members.isEmpty()) {
                    for (int i = 0; i < members.size(); i ++) {
                        if (members.get(i).getId().equals(memberID)) {
                            members.get(i).setRole(role);
                            _myRequestGroup.postValue(group);
                            break;
                        }
                    }
                }
            }
            return;
        }
        new ResponseHandler<Member>(this)
                .setRequest(repository.changeMemberRole(groupID, memberID, role), new ResponseHandlerListener<Member>() {
                    @Override
                    public void onSuccess(BaseResponse<Member> response) {
                        listener.onSuccess(role);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(t.getMessage());
                    }
                });
    }
}
