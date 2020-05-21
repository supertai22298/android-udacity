package com.example.familyapp.viewmodel.profile;

import android.Manifest;
import android.content.Context;
import android.os.Build;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Group;
import com.example.familyapp.model.Member;
import com.example.familyapp.model.Profile;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.LoginResponse;
import com.example.familyapp.repository.NotificationRepository;
import com.example.familyapp.repository.ProfileRepository;
import com.example.familyapp.viewmodel.base.BaseViewModel;
import com.example.familyapp.viewmodel.base.ResponseHandler;
import com.example.familyapp.viewmodel.base.ResponseHandlerListener;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.SingleSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends BaseViewModel implements IProfileViewModel {
    private ProfileRepository repository;
    private NotificationRepository notificationRepository;

    public ProfileViewModel(Context context) {
        super(context);
        repository = new ProfileRepository(context);
        notificationRepository = new NotificationRepository(context);
    }

    private MutableLiveData<Boolean> _isRequestSuccess = new MutableLiveData<>();

    @Override
    public LiveData<Boolean> isRequestSuccess() {
        return _isRequestSuccess;
    }

    @Override
    public void updateProfile(String name, String phone, Boolean isUploadAvatar, String file) {
        repository.updateProfile(name, phone, isUploadAvatar, file).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<BaseResponse<Profile>, SingleSource<BaseResponse<Profile>>>() {
                    @Override
                    public SingleSource<BaseResponse<Profile>> apply(BaseResponse<Profile> groupBaseResponse) {
                        return repository.getProfile()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());
                    }
                })
                .subscribe(new SingleObserver<BaseResponse<Profile>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        /* Notify that view should show loading dialog */
                        isLoading.postValue(true);
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(BaseResponse<Profile> response) {
                        isLoading.postValue(false);
                        Prefs.getInstance(context).saveProfile(new Gson().toJson(response.getData()));
                        _isRequestSuccess.postValue(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.postValue(e.getMessage());
                    }
                });
    }

    @Override
    public void checkPermission(final OnRequestSuccess<Boolean> listener) {
        if (Build.VERSION.SDK_INT >= 23) {
            rxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            listener.onSuccess(true);
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            listener.onSuccess(true);
        }
    }

    @Override
    public void logout(final OnRequestSuccess<Boolean> listener) {
        new ResponseHandler<Object>(this).setRequest(
                notificationRepository.updateDeviceToken(""),
                new ResponseHandlerListener<Object>() {
                    @Override
                    public void onSuccess(BaseResponse<Object> response) {
                        isLoading.postValue(false);
                        listener.onSuccess(true);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        isLoading.postValue(false);
                        listener.onSuccess(true);
                    }
                });
    } }
