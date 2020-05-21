package com.example.familyapp.view.main.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.familyapp.R;
import com.example.familyapp.data.Constant;
import com.example.familyapp.data.notification.Notification;
import com.example.familyapp.notification.Event;
import com.example.familyapp.view.base.BaseFragment;
import com.example.familyapp.view.main.sos.SOSAdapter;
import com.example.familyapp.view.main.sos.SOSNotificationActivity;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.example.familyapp.viewmodel.notification.INotificationViewModel;
import com.example.familyapp.viewmodel.notification.NotificationViewModel;

import java.util.ArrayList;

public class NotificationsFragment extends BaseFragment<INotificationViewModel> {

    private SOSAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_notifications;
    }

    @Override
    protected INotificationViewModel getViewModel() {
        return new NotificationViewModel(getContext());
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.rv_notifications);
        rv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter = new SOSAdapter(getContext(), new ArrayList<Notification>(), null);
        rv.setAdapter(adapter);
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.myNotifications().observe(getViewLifecycleOwner(), new Observer<ArrayList<Notification>>() {
            @Override
            public void onChanged(ArrayList<Notification> notifications) {
                adapter.setList(notifications != null ? notifications : new ArrayList<Notification>());
            }
        });
        mViewModel.getNotifications(null);
    }
}
