package com.example.familyapp.data.notification;

import java.util.ArrayList;

public interface INotificationDao {
    void insertNotification(Notification notification);
    Notification getNotificationById(String id);
    void markAsRead(String id);
    ArrayList<Notification> getNotifications(String type);
}
