package com.example.familyapp.view.main.notifications;

import com.example.familyapp.R;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.viewmodel.notification.INotificationViewModel;

import androidx.navigation.Navigation;

public class NotificationActivity extends BaseActivity<INotificationViewModel> {
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_notification;
    }

    @Override
    protected void initView() {
        setNavController(Navigation.findNavController(this, R.id.nav_host_fragment));
    }

    @Override
    protected INotificationViewModel getViewModel() {
        return null;
    }
}
