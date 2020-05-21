package com.example.familyapp.network;

import android.content.Context;

import com.example.familyapp.BuildConfig;
import com.example.familyapp.data.Constant;
import com.example.familyapp.data.Prefs;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sandy on 09/03/2020.
 */
public class RetrofitManager {

    private final static long CONNECT_TIMEOUT = 180; // ms
    private final static long READ_TIMEOUT = 180; // ms
    private final static long WRITE_TIMEOUT = 180; // ms

    private static RetrofitManager manger;
    private Retrofit retrofit;

    private RetrofitManager(Context context) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getClient(context))
                .build();
    }

    public static synchronized RetrofitManager getInstance(Context context) {
        if (manger == null) {
            manger = new RetrofitManager(context);
        }
        return manger;
    }

    private OkHttpClient getClient(final Context context) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient().newBuilder()
                .addInterceptor(httpLoggingInterceptor)
                .callTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        if (Prefs.getInstance(context).getAuthentication() != null) {
                            return response.request().newBuilder()
                                    .header(Constant.AUTHORIZTION_KEY, Prefs.getInstance(context).getAuthentication())
                                    .build();
                        }
                        return null;
                    }
                })
                .build();
    }

    public APIs getService() {
        return retrofit.create(APIs.class);
    }
}
