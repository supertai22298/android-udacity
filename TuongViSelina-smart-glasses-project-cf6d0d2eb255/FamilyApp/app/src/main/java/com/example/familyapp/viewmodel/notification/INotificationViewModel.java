package com.example.familyapp.viewmodel.notification;

import com.example.familyapp.data.notification.Notification;
import com.example.familyapp.viewmodel.base.IViewModel;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;

public interface INotificationViewModel extends IViewModel {

    LiveData<ArrayList<Notification>> myNotifications();

    void getNotifications(String type);
}
