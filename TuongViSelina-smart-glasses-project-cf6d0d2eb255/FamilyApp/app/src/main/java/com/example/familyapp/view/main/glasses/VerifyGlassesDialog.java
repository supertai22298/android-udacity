package com.example.familyapp.view.main.glasses;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.familyapp.R;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.utils.Utils;
import com.example.familyapp.view.base.BaseDialog;
import com.example.familyapp.view.main.group.GroupDetailsActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class VerifyGlassesDialog extends BaseDialog {
    private OnVerifyGlassesListener listener;

    public VerifyGlassesDialog(@NonNull Context context, OnVerifyGlassesListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_verify_add_glasses;
    }

    @Override
    protected void initView() {
        final RecyclerView list = findViewById(R.id.list_codes);
        final AppCompatEditText edtCode = findViewById(R.id.edt_code);
        final VerifyCodesAdapter adapter = new VerifyCodesAdapter(new ArrayList<String>());
        AppCompatButton btnVerify = findViewById(R.id.btn_confirm);

        list.setLayoutManager(new GridLayoutManager(getContext(), 4));
        list.setAdapter(adapter);

        edtCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    String[] a = s.toString().split("");
                    ArrayList<String> temp = new ArrayList<>();
                    for (int i = 0; i < a.length; i++) {
                        if (!a[i].isEmpty()) temp.add(a[i]);
                    }
                    adapter.setCodes(temp);
                }
            }
        });

        edtCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showKeyboard(true, v);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                edtCode.dispatchTouchEvent(
                        MotionEvent.obtain(
                                SystemClock.uptimeMillis(),
                                SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_DOWN,
                                0f,
                                0f,
                                0
                        )
                );
                edtCode.dispatchTouchEvent(
                        MotionEvent.obtain(
                                SystemClock.uptimeMillis(),
                                SystemClock.uptimeMillis(),
                                MotionEvent.ACTION_UP,
                                0f,
                                0f,
                                0
                        )
                );
            }
        }, 300);


        btnVerify.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String ID = edtCode.getText().toString().trim();
                if (!ID.isEmpty() && ID.length() == 4) {
                    listener.onSendVerifyCode(ID, VerifyGlassesDialog.this);
                }
            }
        });
    }

    public interface OnVerifyGlassesListener {
        void onSendVerifyCode(String code, Dialog dialog);
    }
}
