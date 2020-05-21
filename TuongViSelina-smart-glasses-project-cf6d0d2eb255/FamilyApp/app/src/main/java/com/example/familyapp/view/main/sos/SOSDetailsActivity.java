package com.example.familyapp.view.main.sos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.familyapp.R;
import com.example.familyapp.data.notification.Notification;
import com.example.familyapp.data.notification.NotificationDao;
import com.example.familyapp.view.base.BaseActivity;
import com.example.familyapp.viewmodel.glasses.GlassesViewModel;
import com.example.familyapp.viewmodel.glasses.IGlassesViewModel;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import static com.example.familyapp.notification.OutAppNotificationActivity.DATA;

public class SOSDetailsActivity extends BaseActivity<IGlassesViewModel> implements OnMapReadyCallback,
        DownloadImageGPSAsyncTask.DownloadImageListener {

    private AppCompatTextView tvGlassesName, tvAddress;
    private RoundedImageView imgGPS;
    private GoogleMap mMap;
    private Handler handler = new Handler();
    private Runnable runnable;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_sos_details;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapView mapView = findViewById(R.id.mv_map_view);
        tvGlassesName = findViewById(R.id.tv_glass_name);
        tvAddress = findViewById(R.id.tv_time_stamp);
        imgGPS = findViewById(R.id.img_glass_gps);
        AppCompatImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        setDetails(getIntent().getStringExtra(DATA));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setDetails(intent.getStringExtra(DATA));
    }

    private void setDetails(String id) {
        if (id == null) {
            return;
        }
        final Notification mNotification = NotificationDao.getInstance().getNotificationById(id);
        if (mNotification == null) {
            return;
        }

        String glassesName = String.format(getString(R.string.notify_glasses_name), mNotification.getReferID());
        tvGlassesName.setText(glassesName);

        cancelReload();

        runnable = new Runnable() {
            @Override
            public void run() {
                mViewModel.getSOSImages(mNotification.getReferID(), new OnRequestSuccess<ArrayList<String>>() {
                    @Override
                    public void onSuccess(ArrayList<String> data) {
                        showLoading(true);
                        new DownloadImageGPSAsyncTask(SOSDetailsActivity.this, data.get(data.size() - 1), SOSDetailsActivity.this).execute();
                    }
                });
                if (mNotification.expirySOSTime() > 30) {
                    handler.removeCallbacks(this);
                }
            }
        };
        runnable.run();
    }

    private void cancelReload() {
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onSuccess(GPSImage data) {
        if (mMap != null && data != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(data.getLat(), data.getLng()), 15));
            imgGPS.setImageBitmap(data.getBitmap());
            tvAddress.setText(data.getAddress());
            handler.postDelayed(runnable, 60 * 1000);
        }
        showLoading(false);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected IGlassesViewModel getViewModel() {
        return new GlassesViewModel(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelReload();
    }
}
