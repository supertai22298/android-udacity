package com.example.familyapp.view.base;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.model.Language;
import com.example.familyapp.utils.LanguageUtil;
import com.example.familyapp.utils.MyContextWrapper;
import com.example.familyapp.utils.Utils;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Locale;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;

public abstract class
BaseActivity<T extends IViewModel> extends AppCompatActivity {
    protected T mViewModel;
    protected NavController navController;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        initViewModel();
        initView();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void initView();

    protected abstract T getViewModel();

    protected void setNavController(NavController navController) {
        this.navController = navController;
    }

    protected void initViewModel() {
        mViewModel = getViewModel();
        if (mViewModel != null) {
            getLifecycle().addObserver((LifecycleObserver) mViewModel);
            mViewModel.setRxPermissions(new RxPermissions(this));
            mViewModel.isLoading().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    showLoading(aBoolean);
                }
            });
            mViewModel.onError().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String t) {
                    handelError(t);
                }
            });
        }
    }

    protected void showLoading(boolean isShow) {
        if (isShow) {
            if (loadingDialog == null) {
                loadingDialog = new LoadingDialog(this);
                loadingDialog.setCancelable(false);
            } if(!loadingDialog.isShowing()){loadingDialog.show();}

        } else {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void handelError(String t) {
        Toast.makeText(this, getString(R.string.msg_error), Toast.LENGTH_LONG).show();
    }
}
