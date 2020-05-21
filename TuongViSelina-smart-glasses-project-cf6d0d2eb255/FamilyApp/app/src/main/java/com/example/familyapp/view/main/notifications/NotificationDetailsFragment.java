package com.example.familyapp.view.main.notifications;

import android.os.Bundle;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.view.base.BaseFragment;
import com.example.familyapp.viewmodel.base.IViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NotificationDetailsFragment extends BaseFragment<IViewModel> {

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_notification_details;
    }

    @Override
    protected IViewModel getViewModel() {
        return null;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
    }
}
