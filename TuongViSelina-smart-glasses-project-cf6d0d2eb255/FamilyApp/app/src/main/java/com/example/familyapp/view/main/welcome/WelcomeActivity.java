package com.example.familyapp.view.main.welcome;

import com.example.familyapp.R;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.viewmodel.base.IViewModel;

public class WelcomeActivity extends BaseActivity<IViewModel> {
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected IViewModel getViewModel() {
        return null;
    }
}
