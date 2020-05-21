package com.example.familyapp.view.main.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.familyapp.R;
import com.example.familyapp.model.Glasses;
import com.example.familyapp.model.Member;
import com.example.familyapp.model.Profile;
import com.example.familyapp.utils.Utils;
import com.example.familyapp.view.base.BaseFragment;
import com.example.familyapp.view.main.glasses.GlassesDetailsActivity;
import com.example.familyapp.view.main.notifications.NotificationActivity;
import com.example.familyapp.viewmodel.group.OnRequestSuccess;
import com.example.familyapp.viewmodel.home.HomeViewModel;
import com.example.familyapp.viewmodel.home.IHomeViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.disposables.Disposable;

import static com.example.familyapp.view.main.group.GroupDetailsActivity.IS_FROM_ADMIN;

public class HomeFragment extends BaseFragment<IHomeViewModel> implements OnMapReadyCallback {

    private HomeGlassesAdapter mAdapter;
    private CircleImageView imgAvatar;
    private GoogleMap mMap;
    private ArrayList<Marker> markers;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(@NonNull View view, @Nullable Bundle savedInstanceState) {
        View btnNotifications = view.findViewById(R.id.btn_notifications);
        RecyclerView listGlasses = view.findViewById(R.id.list_glasses);
        View btnOpenGlasses = view.findViewById(R.id.btn_open_glasses_list);
        final View layoutGlassesList = view.findViewById(R.id.layout_maker_info);
        imgAvatar = view.findViewById(R.id.ic_image);

        btnOpenGlasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layoutGlassesList.getVisibility() == View.VISIBLE) {
                    Utils.collapse(layoutGlassesList);
                } else {
                    Utils.expand(layoutGlassesList);
                }
            }
        });

        listGlasses.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mAdapter = new HomeGlassesAdapter(new ArrayList<Glasses>(), new HomeGlassesAdapter.GlassesSelectListener() {
            @Override
            public void onSelected(Glasses glasses, int position) {
                if (glasses.getPosition() != null) {
                    markers.get(position).showInfoWindow();
                    moveMapCamera(glasses.getPosition());
                }
            }
        });
        listGlasses.setAdapter(mAdapter);

        MapView mapView = view.findViewById(R.id.mv_map_view);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        AppCompatImageButton btnDirection = view.findViewById(R.id.btn_finder_location);
        btnDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng currentLocation = mViewModel.myCurrentLocation().getValue();
                Glasses glasses = mAdapter.getSelected();
                if (currentLocation != null && glasses != null) {
                    LatLng latLng = glasses.getPosition();
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse("http://maps.google.com/maps?saddr" + currentLocation.latitude + "," + currentLocation.longitude + "&daddr=" + latLng.latitude + "," + latLng.longitude));
                    startActivity(intent);
                }
            }
        });

        btnNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NotificationActivity.class));
            }
        });
    }

    @Override
    protected IHomeViewModel getViewModel() {
        return new HomeViewModel(getContext());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int index = markers.indexOf(marker);
                Glasses glasses = mAdapter.getGlasses(index);
                if (glasses != null) {
                    glasses.setGps(null);
                    Intent intent = new Intent(getContext(), GlassesDetailsActivity.class);
                    intent.putExtra(GlassesDetailsActivity.GLASSES_DATA, glasses);
                    startActivity(intent);
                }
                return false;
            }
        });
        mViewModel.setRxPermissions(new RxPermissions(this));
        mViewModel.checkPermission();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMap != null && mViewModel.myGlasses().getValue() != null) {
            mViewModel.getGlasses();
        }
    }

    private Bitmap loadBitmapFromView(Glasses glasses) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_glasses, null);
        AppCompatTextView tvGlassName = v.findViewById(R.id.tv_glass_name);
        AppCompatTextView tvTimeStamp = v.findViewById(R.id.tv_time_stamp);
        RoundedImageView imgGlassesGPS = v.findViewById(R.id.img_glass_gps);

        if (glasses.getGps().getBitmap() != null) {
            imgGlassesGPS.setImageBitmap(glasses.getGps().getBitmap());
            tvTimeStamp.setText(glasses.getGps().getAddress());
        }

        tvGlassName.setText(glasses.getGlassesName());

        if (v.getMeasuredHeight() <= 0) {
            v.measure(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        return null;
    }

    @Override
    protected void initViewModel() {
        super.initViewModel();
        mViewModel.myGlasses().observe(this, new Observer<ArrayList<Glasses>>() {
            @Override
            public void onChanged(ArrayList<Glasses> myGlasses) {
                if (myGlasses != null && !myGlasses.isEmpty()) {
                    mAdapter.setGlasses(myGlasses);
                    addMarkers(myGlasses);
                } else {
                    mAdapter.setGlasses(new ArrayList<Glasses>());
                    addMarkers(new ArrayList<Glasses>());
                }
            }
        });
        mViewModel.myCurrentLocation().observe(this, new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                moveMapCamera(latLng);
                mMap.setMyLocationEnabled(true);
            }
        });
        mViewModel.getProfile(new OnRequestSuccess<Profile>() {
            @Override
            public void onSuccess(Profile data) {
                Picasso.with(getContext())
                        .load(data.getAvatar())
                        .placeholder(R.drawable.ic_user)
                        .into(imgAvatar);
            }
        });
    }

    private void moveMapCamera(LatLng latLng) {
        if (mMap != null && latLng != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }

    private void addMarkers(ArrayList<Glasses> glasses) {
        if (mMap == null || glasses == null || glasses.isEmpty()) return;
        if (markers == null) {
            markers = new ArrayList<>();
        } else {
            mMap.clear();
            markers.clear();
        }

        for (int i = 0; i < glasses.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(glasses.get(i).getPosition())
                    .title(glasses.get(i).getGlassesName())
                    .icon(BitmapDescriptorFactory.fromBitmap(loadBitmapFromView(glasses.get(i))));
            Marker marker = mMap.addMarker(markerOptions);
            markers.add(marker);
        }

        moveMapCamera(glasses.get(0).getPosition());
        markers.get(0).showInfoWindow();
    }
}
