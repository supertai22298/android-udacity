package com.example.familyapp.viewmodel.home;

import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Profile;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;

public interface IHomeViewModel extends IViewModel {
    LiveData<ArrayList<Glasses>> myGlasses();
    LiveData<LatLng> myCurrentLocation();
    void getProfile(OnRequestSuccess<Profile> listener);
    void checkPermission();
    void getGlasses();
    void getCurrentLocation();
}
