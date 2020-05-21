package com.example.familyapp.view.signin;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.view.main.MainActivity;
import com.example.familyapp.view.signup.SignUpActivity;
import com.example.familyapp.viewmodel.login.ILoginViewModel;
import com.example.familyapp.viewmodel.login.LoginViewModel;

import java.util.Objects;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.lifecycle.Observer;


public class SignInActivity extends BaseActivity<ILoginViewModel> implements TextWatcher {

    AppCompatEditText userNameEdt, passwordEdt;
    AppCompatButton signInBtn;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_signin;
    }

    @Override
    protected ILoginViewModel getViewModel() {
        return new LoginViewModel(this);
    }

    @Override
    protected void initView() {

        TextView tv_signup = (TextView) findViewById(R.id.tv_sign_up);
        signInBtn = findViewById(R.id.btn_sign_in);
        userNameEdt = findViewById(R.id.edt_user_name);
        passwordEdt = findViewById(R.id.edt_password);
        tv_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.login(Objects.requireNonNull(userNameEdt.getText()).toString().trim(),
                        Objects.requireNonNull(passwordEdt.getText()).toString().trim());
            }
        });
        userNameEdt.addTextChangedListener(this);
        passwordEdt.addTextChangedListener(this);
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.isLoginSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        signInBtn.setEnabled(isValidateInfo());
    }

    private boolean isValidateInfo() {
        return !userNameEdt.getText().toString().trim().isEmpty() && !passwordEdt.getText().toString().trim().isEmpty();
    }
}
