package com.example.familyapp.view.base;

import android.content.Context;
import android.os.Bundle;

import com.example.familyapp.R;

import androidx.annotation.NonNull;

public class LoadingDialog extends BaseDialog {
    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.transparent);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_loading;
    }

    @Override
    protected void initView() {

    }
}
