package com.example.familyapp.viewmodel.base;

import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.lifecycle.LiveData;

/**
 * Created by Sandy on 09/03/2020.
 */
public interface IViewModel {

    LiveData<Boolean> isLoading();

    LiveData<String> onError();

    void setRxPermissions(RxPermissions rxPermissions);

    RxPermissions getRxPermissions();

    void updateDeviceToken();

    void onCreate();

    void onStart();

    void onPause();

    void onResume();

    void onStop();

    void onDestroy();
}
