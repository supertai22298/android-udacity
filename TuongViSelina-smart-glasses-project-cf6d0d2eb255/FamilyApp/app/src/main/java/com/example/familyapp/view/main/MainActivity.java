package com.example.familyapp.view.main;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.familyapp.R;
import com.example.familyapp.data.Prefs;
import com.example.familyapp.notification.Event;
import com.example.familyapp.notification.OutAppNotificationActivity;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.view.main.account.AccountFragment;
import com.example.familyapp.viewmodel.MainViewModel;
import com.example.familyapp.viewmodel.base.BaseViewModel;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<IViewModel> {

    private MainPagerAdapter adapter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected IViewModel getViewModel() {
        return new MainViewModel(this);
    }

    @Override
    protected void initView() {
        Log.e("Language", getResources().getConfiguration().locale.getDisplayLanguage());
        final BottomNavigationView navView = findViewById(R.id.nav_view);
        final ViewPager viewPager = findViewById(R.id.view_pager);
        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = 0;
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        index = 0;
                        break;
                    case R.id.navigation_group:
                        index = 1;
                        break;
                    case R.id.navigation_notifications:
                        index = 2;
                        break;
                    case R.id.navigation_account:
                        index = 3;
                        break;
                }
                navView.getMenu().getItem(index).setChecked(true);
                viewPager.setCurrentItem(index);
                return false;
            }
        });
        Event.Notification notification = Prefs.getInstance(this).getNotification();
        if (notification != null) {
            Prefs.getInstance(this).saveNotification(null);
            OutAppNotificationActivity.startNotificationEvent(this, notification);
        }
        mViewModel.updateDeviceToken();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.getItem(3).onActivityResult(requestCode, resultCode, data);
    }
}
