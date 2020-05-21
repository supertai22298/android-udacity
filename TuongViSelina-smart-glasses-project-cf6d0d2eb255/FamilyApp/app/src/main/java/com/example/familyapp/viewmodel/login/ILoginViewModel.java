package com.example.familyapp.viewmodel.login;

import com.example.familyapp.viewmodel.base.IViewModel;

import androidx.lifecycle.LiveData;

/**
 * Created by Sandy on 09/03/2020.
 */
public interface ILoginViewModel extends IViewModel {
    LiveData<Boolean> isLoginSuccess();
    void login(String username, String password);
}
