package com.example.familyapp.view.main;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.utils.LanguageUtil;
import com.example.familyapp.utils.Utils;
import com.example.familyapp.view.signin.SignInActivity;
import com.example.familyapp.view.tutorial.TutorialActivity;

import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.ic_logo).animate().alpha(1.0f).setDuration(2000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent;
                if (Prefs.getInstance(SplashActivity.this).getAuthentication() != null) {
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else if (Prefs.getInstance(SplashActivity.this).isFirstUsed()) {
                    intent = new Intent(SplashActivity.this, TutorialActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, SignInActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}
