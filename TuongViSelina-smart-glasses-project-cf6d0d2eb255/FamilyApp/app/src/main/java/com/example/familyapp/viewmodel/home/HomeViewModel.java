package com.example.familyapp.viewmodel.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Direction;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Profile;
import com.example.familyapp.model.response.BaseResponse;
import com.example.familyapp.repository.GlassesRespository;
import com.example.familyapp.repository.MapRepository;
import com.example.familyapp.repository.ProfileRepository;
import com.example.familyapp.utils.Utils;
import com.example.familyapp.view.main.sos.DownloadGlassesGPSAsyncTask;
import com.example.familyapp.viewmodel.base.BaseViewModel;
import com.example.familyapp.viewmodel.base.ResponseHandler;
import com.example.familyapp.viewmodel.base.ResponseHandlerListener;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends BaseViewModel implements IHomeViewModel {

    private GlassesRespository glassesRespository;
    private ProfileRepository profileRepository;
    private MutableLiveData<ArrayList<LatLng>> _directions = new MutableLiveData<>();
    private MutableLiveData<ArrayList<Glasses>> _glasses = new MutableLiveData<>();
    private MutableLiveData<LatLng> _currentLocation = new MutableLiveData<>();

    public HomeViewModel(Context context) {
        super(context);
        profileRepository = new ProfileRepository(context);
        glassesRespository = new GlassesRespository(context);
    }

    @Override
    public LiveData<ArrayList<Glasses>> myGlasses() {
        return _glasses;
    }

    @Override
    public LiveData<LatLng> myCurrentLocation() {
        return _currentLocation;
    }

    @Override
    public void checkPermission() {
        if (rxPermissions != null) {
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(new io.reactivex.Observer<Boolean>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            addDisposable(d);
                        }

                        @Override
                        public void onNext(Boolean aBoolean) {
                            if (aBoolean) {
                                getCurrentLocation();
                                getGlasses();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    @Override
    public void getGlasses() {
        glassesRespository.getGlasses()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<BaseResponse<ArrayList<Glasses>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        isLoading.postValue(_glasses.getValue() == null);
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(BaseResponse<ArrayList<Glasses>> response) {
                        if (response.getData() != null) {
                            new DownloadGlassesGPSAsyncTask(context, response.getData(), new DownloadGlassesGPSAsyncTask.DownloadImageListener() {
                                @Override
                                public void onSuccess(ArrayList<Glasses> data) {
                                    isLoading.postValue(false);
                                    _glasses.postValue(data);
                                }
                            }).execute();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    @Override
    public void getProfile(final OnRequestSuccess<Profile> listener) {
        new ResponseHandler<Profile>(this).setBackgroundRequest(
                profileRepository.getProfile(),
                new ResponseHandlerListener<Profile>() {
                    @Override
                    public void onSuccess(BaseResponse<Profile> response) {
                        Prefs.getInstance(context).saveProfile(new Gson().toJson(response.getData()));
                        listener.onSuccess(response.getData());
                    }

                    @Override
                    public void onApiFailed(Throwable t) {
                        /*TODO nothing*/
                    }
                }
        );
    }

    @Override
    public void getCurrentLocation() {
        Utils.getLatestLocation(context)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<LatLng>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(LatLng latLng) {
                        Log.e("LatLng", latLng.toString());
                        _currentLocation.postValue(latLng);
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
    }

    private void handleDataDirection(ArrayList<Direction.Route> routers) {
        ArrayList<LatLng> routeList = new ArrayList<LatLng>();
        if (routers != null && !routers.isEmpty()) {
            Direction.Route routeA = routers.get(0);
            if (routeA.getLegs() != null && !routeA.getLegs().isEmpty()) {
                routeList.addAll(routeA.getLegs().get(0).getDirectionSteps());
            }
        }
        _directions.postValue(routeList);
    }
}
