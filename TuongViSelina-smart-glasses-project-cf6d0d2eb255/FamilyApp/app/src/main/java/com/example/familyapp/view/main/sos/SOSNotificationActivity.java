package com.example.familyapp.view.main.sos;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.data.Constant;
import com.example.familyapp.data.notification.INotificationDao;
import com.example.familyapp.data.notification.Notification;
import com.example.familyapp.data.notification.NotificationDao;
import com.example.familyapp.notification.Event;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.viewmodel.base.IViewModel;
import com.example.familyapp.viewmodel.notification.INotificationViewModel;
import com.example.familyapp.viewmodel.notification.NotificationViewModel;

import java.util.ArrayList;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SOSNotificationActivity extends BaseActivity<INotificationViewModel> {

    private SOSAdapter adapter;
    private MediaPlayer player;
    private Vibrator vibrator;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_sos;
    }

    @Override
    protected void initView() {
        AppCompatButton btnClose = findViewById(R.id.btn_close);
        AppCompatImageButton btnBack = findViewById(R.id.btn_back);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSOS();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RecyclerView recyclerView = findViewById(R.id.notiy_list);
        adapter = new SOSAdapter(this, new ArrayList<Notification>(), Constant.NOTIFY_SOS);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrator.vibrate(500);
                }
                handler.postDelayed(this, 3000);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSOS();
    }

    private void startSOS() {
        player = MediaPlayer.create(this, R.raw.sos);
        player.setLooping(true);
        player.start();
        runnable.run();
    }

    private void stopSOS() {
        vibrator.cancel();
        handler.removeCallbacks(runnable);
        player.stop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mViewModel.getNotifications(Constant.NOTIFY_SOS);
    }

    @Override
    protected INotificationViewModel getViewModel() {
        return new NotificationViewModel(this);
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.myNotifications().observe(this, new Observer<ArrayList<Notification>>() {
            @Override
            public void onChanged(ArrayList<Notification> notifications) {
                adapter.setList(notifications != null ? notifications : new ArrayList<Notification>());
            }
        });

        mViewModel.getNotifications(Constant.NOTIFY_SOS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSOS();
    }
}
