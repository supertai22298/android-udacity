package com.example.familyapp.view.profile;

import android.view.View;
import android.widget.Button;

import com.example.familyapp.R;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.viewmodel.base.IViewModel;

public class ProfileActivity extends BaseActivity<IViewModel> {
    Button btn_save;
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_profile;
        }

    @Override
    protected void initView() {
        Button btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected IViewModel getViewModel() {
        return null;
    }

}
