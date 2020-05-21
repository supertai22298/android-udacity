package com.example.familyapp.notification;

import com.example.familyapp.data.notification.Notification;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxBus {

    public RxBus() {
    }

    private PublishSubject<Notification> bus = PublishSubject.create();

    public void send(Notification o) {
        bus.onNext(o);
    }

    public Observable<Notification> listener() {
        return bus;
    }
}
