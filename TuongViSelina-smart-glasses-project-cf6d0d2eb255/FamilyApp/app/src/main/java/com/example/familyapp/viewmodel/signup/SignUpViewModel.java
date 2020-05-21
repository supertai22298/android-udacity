package com.example.familyapp.viewmodel.signup;

import android.annotation.SuppressLint;
import android.content.Context;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.LoginResponse;
import com.example.familyapp.repository.SignUpRepository;
import com.example.familyapp.viewmodel.base.BaseViewModel;
import com.example.familyapp.viewmodel.base.ResponseHandler;
import com.example.familyapp.viewmodel.base.ResponseHandlerListener;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by Sandy on 09/03/2020.
 */
public class SignUpViewModel extends BaseViewModel implements ISignUpViewModel {
    private SignUpRepository repository;
    private MutableLiveData<Boolean> _isSignUpSuccess = new MutableLiveData<>();

    public SignUpViewModel(Context context) {
        super(context);
        repository = new SignUpRepository(context);
    }

    /**
     * This method will be listened the notification for success request API of sign of.
     * We will call and listener it from the view to handle the next step need to do after sign up
     * success
     * @return
     */
    @Override
    public LiveData<Boolean> isSignUpSuccess() {
        return _isSignUpSuccess;
    }

    /**
     * This method is defined from interface implementation.
     * ISignupViewModel
     * We will define all action we need to do for ViewModel and call it from theirs view
     * @param username
     * @param password
     * @param fullName
     * @param phoneNumber
     */
    @SuppressLint("CheckResult")
    @Override
    public void signUp(String username, String password, String fullName, String phoneNumber) {
        /**
         * We setup to send a request API here
         */
        new ResponseHandler<LoginResponse>(this).setRequest(
                repository.signUp(username, password, fullName, phoneNumber),
                new ResponseHandlerListener<LoginResponse>() {
                    @Override
                    public void onSuccess(BaseResponse<LoginResponse> response) {
                        /*Save authorization down to the local, it's used for request any API, which
                        was required authorization header like get groups/profile/...*/

                        Prefs.getInstance(context).saveAuthentication(response.getData().getToken());

                        /*Notify that sign up request is successfully*/
                        _isSignUpSuccess.postValue(true);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(context.getString(R.string.msg_signup_failed));
                    }
                }
        );
    }
}
