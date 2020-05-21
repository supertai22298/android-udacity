package com.example.familyapp.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;

import com.example.familyapp.R;
import com.example.familyapp.data.Constant;
import com.example.familyapp.data.Prefs;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleObserver;

public class Utils {

    private static final long SECOND_MILLIS = 1000;
    private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    @SuppressLint("MissingPermission")
    public static Single<LatLng> getLatestLocation(final Context context) {
        return new Single<LatLng>() {
            @Override
            protected void subscribeActual(final SingleObserver<? super LatLng> observer) {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
                fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        LatLng latLng = new LatLng(0.0, 0.0);
                        if (task.isSuccessful() && task.getResult() != null) {
                            latLng = new LatLng(task.getResult().getLatitude(), task.getResult().getLongitude());
                        } else {
                            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                            if (lm != null) {
                                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                }
                            }
                        }
                        observer.onSuccess(latLng);
                    }
                });
            }
        };
    }

    public static void changeLanguage(Context context) {
        Resources res = context.getResources();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(Prefs.getInstance(context).getLanguage().toLowerCase()));
        context.getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
        context.getApplicationContext().getResources().updateConfiguration(conf, context.getApplicationContext().getResources().getDisplayMetrics());
    }

    public static String convertBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM, yyyy");
        return format.format(calendar.getTime());
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getHeight(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(wrapContentMeasureSpec, matchParentMeasureSpec);
        final int targetWidth = (int) v.getContext().getResources().getDimension(R.dimen._200_dp);

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().width = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().width = interpolatedTime == 1
                        ? targetWidth
                        : (int) (targetWidth * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetWidth / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialWidth = (int) v.getContext().getResources().getDimension(R.dimen._200_dp);

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialWidth / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static String getTimeAgo(long timestamp, Context context) {
        long time = timestamp;
        if (time < 1000000000000L) {
            time *= 1000;
        }

        long now = System.currentTimeMillis();

        long diff = now - time;
        if (diff < MINUTE_MILLIS)
            return context.getString(R.string.time_just_now);
        else if (diff < 2 * MINUTE_MILLIS)
            return context.getString(R.string.time_one_minute_ago);
        else if (diff < 60 * MINUTE_MILLIS)
            return context.getString(
                    R.string.time_minutes_ago,
                    String.valueOf(diff / MINUTE_MILLIS)
            );
        else if (diff < 2 * HOUR_MILLIS) return context.getString(R.string.time_one_hour_ago);
        else if (diff < 24 * HOUR_MILLIS) return context.getString(
                R.string.time_hours_ago,
                String.valueOf(diff / HOUR_MILLIS)
        );
        else if (diff < 48 * HOUR_MILLIS) return context.getString(R.string.time_yesterday);
        else if (diff / DAY_MILLIS <= 31) return context.getString(
                R.string.time_days_ago,
                String.valueOf(diff / DAY_MILLIS)
        );
        else return context.getString(
                    R.string.time_months_ago,
                    String.valueOf((diff / DAY_MILLIS) / 30)
            );
    }

    public static void showKeyboard(final boolean isShow, final View view) {
        try {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                if (isShow) imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
                else imm.hideSoftInputFromWindow(
                        view.getApplicationWindowToken(), 0);
            }
        } catch (IllegalStateException | NullPointerException e) {
            e.printStackTrace();
        }
    }

}