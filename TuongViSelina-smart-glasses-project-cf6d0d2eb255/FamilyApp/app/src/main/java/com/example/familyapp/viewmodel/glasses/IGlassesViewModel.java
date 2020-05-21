package com.example.familyapp.viewmodel.glasses;

import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Group;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;

public interface IGlassesViewModel extends IViewModel {
    LiveData<String> isCreateGlassesSuccess();
    LiveData<Glasses> myRequestGlasses();
    void getSOSImages(String glassesID, OnRequestSuccess<ArrayList<String>> onRequestSuccess);
    void createGlasses(String groupID, String glassesID, String glassesName, String blindName, String blindAddress, String blindAge);
    void updateGlasses(Glasses glasses);
    void verifyGlasses(String linkID, String code, OnRequestSuccess<Boolean> listener);
    void setGlasses(Glasses glasses);
    void removeGlasses(String linkID);
}
