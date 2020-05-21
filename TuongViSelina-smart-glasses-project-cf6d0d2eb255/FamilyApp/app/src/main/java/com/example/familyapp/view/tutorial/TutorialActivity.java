package com.example.familyapp.view.tutorial;

import android.content.Intent;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.view.signin.SignInActivity;
import com.example.familyapp.view.signup.SignUpActivity;
import com.example.familyapp.viewmodel.base.IViewModel;

import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class TutorialActivity extends BaseActivity<IViewModel> {
    @Override
    protected int getLayoutRes() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        ViewPager viewPager = findViewById(R.id.view_pager);
        RecyclerView listDot = findViewById(R.id.dot_list);
        final AppCompatTextView tvTitle = findViewById(R.id.tv_title);
        final AppCompatTextView tvDescription = findViewById(R.id.tv_description);
        final DotAdapter dotAdapter = new DotAdapter();
        final AppCompatButton btnLogin = findViewById(R.id.btn_login);
        final AppCompatButton btnRegister = findViewById(R.id.btn_register);
        listDot.setLayoutManager(new GridLayoutManager(this, 3));
        listDot.setAdapter(dotAdapter);

        final ArrayList<String> titles = new ArrayList<String>() {{
            add(getString(R.string.title_wel_01));
            add(getString(R.string.title_wel_02));
            add(getString(R.string.title_wel_03));
        }};

        final ArrayList<String> description = new ArrayList<String>() {{
            add(getString(R.string.wel_01));
            add(getString(R.string.wel_02));
            add(getString(R.string.wel_03));
        }};

        tvDescription.setText(description.get(0));
        tvTitle.setText(titles.get(0));
        viewPager.setAdapter(new TutorialPagerAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                dotAdapter.setCurrentFocus(position);
                tvDescription.setText(description.get(position));
                tvTitle.setText(titles.get(position));
                if (position == 2) {
                    btnLogin.setVisibility(View.VISIBLE);
                    btnRegister.setVisibility(View.VISIBLE);
                    Prefs.getInstance(getApplicationContext()).removeFirstUsed();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutorialActivity.this, SignInActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutorialActivity.this, SignUpActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
    }

    @Override
    protected IViewModel getViewModel() {
        return null;
    }
}
