package com.example.familyapp.view.base;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.familyapp.R;
import com.example.familyapp.viewmodel.base.IViewModel;

import java.util.Objects;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.Observer;

public abstract class BaseDialogFragment<T extends IViewModel> extends DialogFragment {
    protected T mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = getView();
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutRes(), container);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        initView(view, savedInstanceState);
        initViewModel();
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    protected abstract void initView(@NonNull View view, @Nullable Bundle savedInstanceState);

    protected abstract T getViewModel();

    protected void initViewModel() {
        mViewModel = getViewModel();
        if (mViewModel != null) {
            getLifecycle().addObserver((LifecycleObserver) mViewModel);
            mViewModel.isLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    showLoading(aBoolean);
                }
            });
            mViewModel.onError().observe(getViewLifecycleOwner(), new Observer<String>() {
                @Override
                public void onChanged(String t) {
                    handelError(t);
                }
            });
        }
    }

    protected void showLoading(boolean isShow) {
        if (getActivity() != null && isAdded()) {
            ((BaseActivity) getActivity()).showLoading(isShow);
        }
    }

    protected void handelError(String t) {
        if (getActivity() != null && isAdded()) {
            ((BaseActivity) getActivity()).handelError(t);
        }
    }
}
