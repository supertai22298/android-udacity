package com.example.familyapp.viewmodel.notification;

import android.content.Context;
import android.util.Log;

import com.example.familyapp.application.App;
import com.example.familyapp.data.notification.Notification;
import com.example.familyapp.data.notification.NotificationDao;
import com.example.familyapp.notification.Event;
import com.example.familyapp.notification.RxBus;
import com.example.familyapp.repository.NotificationRepository;
import com.example.familyapp.viewmodel.base.BaseViewModel;

import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NotificationViewModel extends BaseViewModel implements INotificationViewModel {

    private NotificationRepository repository;
    private String type;

    public NotificationViewModel(Context context) {
        super(context);
        repository = new NotificationRepository(context);
    }

    private MutableLiveData<ArrayList<Notification>> _notifications = new MutableLiveData<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public LiveData<ArrayList<Notification>> myNotifications() {
        return _notifications;
    }

    @Override
    public void getNotifications(String type) {
        this.type = type;
        ArrayList<Notification> notifications = NotificationDao.getInstance().getNotifications(type);
        _notifications.postValue(notifications);
    }

    @Override
    protected void onReceivedNotification(Notification notification) {
        super.onReceivedNotification(notification);
        getNotifications(type);
    }
}
