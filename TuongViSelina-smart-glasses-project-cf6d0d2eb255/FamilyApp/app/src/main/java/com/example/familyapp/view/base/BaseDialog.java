package com.example.familyapp.view.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

public abstract class BaseDialog extends Dialog {
    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initView();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void initView();
}
