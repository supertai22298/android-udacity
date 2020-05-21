package com.example.familyapp.repository;

import android.content.Context;

import com.example.familyapp.network.APIs;
import com.example.familyapp.network.RetrofitManager;

/**
 * Created by Sandy on 09/03/2020.
 */
public abstract class BaseRepository {

    protected Context context;
    protected APIs service;

    public BaseRepository(Context context) {
        this.context = context;
        service = RetrofitManager.getInstance(context).getService();
    }
}
