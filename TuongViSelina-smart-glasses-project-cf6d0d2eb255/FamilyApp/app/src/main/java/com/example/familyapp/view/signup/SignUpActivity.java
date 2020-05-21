package com.example.familyapp.view.signup;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.familyapp.R;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.view.main.MainActivity;
import com.example.familyapp.view.signin.SignInActivity;
import com.example.familyapp.viewmodel.signup.ISignUpViewModel;
import com.example.familyapp.viewmodel.signup.SignUpViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;

public class SignUpActivity extends BaseActivity<ISignUpViewModel /*The interface model, which was defined for each view*/> implements TextWatcher {

    private AppCompatEditText edtUserName;
    private AppCompatEditText edtPassword;
    private AppCompatEditText edtConfirmPassword;
    private AppCompatButton btnRegister;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_signup;
    }

    /**
     * Return new constructor for view model class implement the interface model, which was defined for each view
     * @return
     */
    @Override
    protected ISignUpViewModel getViewModel() {
        return new SignUpViewModel(this);
    }

    @Override
    protected void initView() {

        AppCompatTextView tvSignin = findViewById(R.id.tv_sign_in);
        btnRegister = findViewById(R.id.btn_register);

        edtUserName = findViewById(R.id.edt_register_name);
        edtPassword = findViewById(R.id.edt_register_pass);
        edtConfirmPassword = findViewById(R.id.edt_register_repass);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String password = com.example.familyapp.view.signup.SignUpActivity.this.edtPassword.getText().toString();
                String username = edtUserName.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();

                if(username.length() <2 || !validateUseName(username)) {
                    edtUserName.setError(getString(R.string.msg_register_name_failed));
                    return;
                }
                if (password.length() < 8) {
                    edtPassword.setError(getString(R.string.msg_password_invalid_lenght));
                    return;
                }
                if (password.contains(" ")) {
                    edtPassword.setError(getString(R.string.msg_password_invalid_space));
                    return;
                }
                if(!validatePassword(password)){
                    edtPassword.setError(getString(R.string.msg_password_invalid));
                    return;
                } else{
                    System.out.println("Valid");
                }
                if (!confirmPassword.equals(password)) {
                    edtConfirmPassword.setError(getString(R.string.msg_confirm_pass));
                    return;
                }
                /* Say that we will request ISignUpViewModel call sign up with the required value
                * at the same below */
                mViewModel.signUp(username, password, "None-name", "Unknown");
            }
        });
        tvSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignInActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
        edtUserName.addTextChangedListener(this);
        edtConfirmPassword.addTextChangedListener(this);
        edtPassword.addTextChangedListener(this);
    }

    private boolean validateUseName(String name){
        String namePattern = "^[a-zA-z0-9._-]{3,15}$";
        Pattern pattern = Pattern.compile(namePattern);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
    /*
    (?=.*[0-9])       # a digit must occur at least once
    (?=.*[a-z])       # a lower case letter must occur at least once
    (?=.*[A-Z])       # an upper case letter must occur at least once
    (?=.*[@#$%^&+=])  # a special character must occur at least once
    (?=\S+$)          # no whitespace allowed in the entire string
    .{6,}             # anything, at least eight places though
*/
    public static boolean validatePassword(String password){
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[@#$%^&+=!])(?=\\S+$).*$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {
    }
    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
    }
    @Override
    public void afterTextChanged(Editable s) {
        btnRegister.setEnabled(isValidateInfo());
    }
    private boolean isValidateInfo() {
        return !edtUserName.getText().toString().trim().isEmpty()
                && !edtPassword.getText().toString().trim().isEmpty()
                && !edtConfirmPassword.getText().toString().trim().isEmpty();
    }
    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.isSignUpSuccess().observe(this, new Observer<Boolean>() {
            /**
             * Listener is registered here. Sign up is success, so we will move to MainActivity.
             * @param aBoolean
             */
            @Override
            public void onChanged(Boolean aBoolean) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                finish();
            }
        });
    }
}
