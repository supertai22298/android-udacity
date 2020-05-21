package com.example.familyapp.viewmodel.profile;

import com.example.familyapp.viewmodel.base.IViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;

import androidx.lifecycle.LiveData;

public interface IProfileViewModel extends IViewModel {

    LiveData<Boolean> isRequestSuccess();

    void updateProfile(String name, String phone, Boolean isUploadAvatar, String file);

    void checkPermission(OnRequestSuccess<Boolean> listener);

    void logout(OnRequestSuccess<Boolean> listener);
}
