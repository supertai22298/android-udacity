package com.example.familyapp.viewmodel.glasses;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Member;
import com.example.familyapp.model.response.AddGlassResponse;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.model.response.VerifyCodeResponse;
import com.example.familyapp.repository.GlassesRespository;
import com.example.familyapp.viewmodel.base.BaseViewModel;
import com.example.familyapp.viewmodel.base.ResponseHandler;
import com.example.familyapp.viewmodel.base.ResponseHandlerListener;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;

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

public class GlassesViewModel extends BaseViewModel implements IGlassesViewModel {
    private GlassesRespository repository;
    private MutableLiveData<Glasses> _myRequestGlasses = new MutableLiveData<>();
    private MutableLiveData<String> _isCreateGlassesSuccess = new MutableLiveData<>();

    public GlassesViewModel(Context context) {
        super(context);
        repository = new GlassesRespository(context);
    }

    @Override
    public LiveData<String> isCreateGlassesSuccess() {
        return _isCreateGlassesSuccess;
    }

    @Override
    public LiveData<Glasses> myRequestGlasses() {
        return _myRequestGlasses;
    }

    @Override
    public void setGlasses(Glasses glasses) {
        _myRequestGlasses.postValue(glasses);
    }

    @Override
    public void getSOSImages(String glassesID, final OnRequestSuccess<ArrayList<String>> onRequestSuccess) {
        new ResponseHandler<ArrayList<String>>(this).setRequest(
                repository.getSOSImages(glassesID),
                new ResponseHandlerListener<ArrayList<String>>() {
                    @Override
                    public void onSuccess(BaseResponse<ArrayList<String>> response) {
                        if (response.getData() != null && !response.getData().isEmpty()) {
                            onRequestSuccess.onSuccess(response.getData());
                        }
                    }

                    @Override
                    public void onApiFailed(Throwable t) {

                    }
                }
        );
    }

    @SuppressLint("CheckResult")
    @Override
    public void createGlasses(String groupID, final String glassesID, String glassesName, String blindName, String blindAddress, String blindAge) {
        new ResponseHandler<AddGlassResponse>(this).setRequest(
                repository.createGlasses(groupID, glassesID, glassesName, blindName, blindAddress, blindAge),
                new ResponseHandlerListener<AddGlassResponse>() {
                    @Override
                    public void onSuccess(BaseResponse<AddGlassResponse> response) {
                        _isCreateGlassesSuccess.postValue(response.getData().getLinkID());
                    }

                    @Override
                    public void onApiFailed(Throwable t) {

                    }
                }
        );
    }

    @Override
    public void verifyGlasses(String linkID, String code, final OnRequestSuccess<Boolean> listener) {
        new ResponseHandler<Glasses>(this).setRequest(
                repository.verifyGlasses(linkID, code),
                new ResponseHandlerListener<Glasses>() {
                    @Override
                    public void onSuccess(BaseResponse<Glasses> response) {
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
    public void removeGlasses(String linkID) {
        new ResponseHandler<Glasses>(this)
                .setRequest(repository.removeGlasses(linkID), new ResponseHandlerListener<Glasses>() {
                    @Override
                    public void onSuccess(BaseResponse<Glasses> response) {
                        _myRequestGlasses.postValue(null);
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        error.postValue(t.getMessage());
                    }
                });
    }

    @Override
    public void updateGlasses(final Glasses glasses) {
        new ResponseHandler<Glasses>(this)
                .setRequest(repository.updateGlasses(glasses),
                        new ResponseHandlerListener<Glasses>() {
                            @Override
                            public void onSuccess(BaseResponse<Glasses> response) {
                                _myRequestGlasses.postValue(glasses);
                            }

                            @Override
                            public void onApiFailed(Throwable t) {
                                error.postValue(t.getMessage());
                            }
                        });
    }
}
