package com.example.familyapp.viewmodel.signup;

import com.example.familyapp.viewmodel.base.IViewModel;

import androidx.lifecycle.LiveData;

/**
 * Created by Sandy on 09/03/2020.
 */
public interface ISignUpViewModel extends IViewModel {
    LiveData<Boolean> isSignUpSuccess();
    void signUp(String username, String password, String fullName, String phoneNumber);
}
