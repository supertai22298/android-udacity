package com.example.familyapp.viewmodel.login;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.LoginResponse;
import com.example.familyapp.repository.LoginRepository;
import com.example.familyapp.repository.NotificationRepository;
import com.example.familyapp.viewmodel.base.BaseViewModel;
import com.example.familyapp.viewmodel.base.ResponseHandler;
import com.example.familyapp.viewmodel.base.ResponseHandlerListener;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by Sandy on 09/03/2020.
 */
public class LoginViewModel extends BaseViewModel implements ILoginViewModel {
    private LoginRepository repository;
    private MutableLiveData<Boolean> _isLoginSuccess = new MutableLiveData<>();

    public LoginViewModel(Context context) {
        super(context);
        repository = new LoginRepository(context);
    }

    @Override
    public LiveData<Boolean> isLoginSuccess() {
        return _isLoginSuccess;
    }

    @SuppressLint("CheckResult")
    @Override
    public void login(String username, String password) {
        new ResponseHandler<LoginResponse>(this).setRequest(
                repository.login(username, password),
                new ResponseHandlerListener<LoginResponse>() {
                    @Override
                    public void onSuccess(BaseResponse<LoginResponse> response) {
                        Prefs.getInstance(context).saveAuthentication(response.getData().getToken());
                        _isLoginSuccess.postValue(true);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(context.getString(R.string.msg_login_failed));
                    }
                }
        );
    }
}
