package com.example.familyapp.repository;

import android.content.Context;

import com.example.familyapp.R;
import com.example.familyapp.model.Direction;

import io.reactivex.Single;

public class MapRepository extends BaseRepository {
    public MapRepository(Context context) {
        super(context);
    }

    public Single<Direction> getDirectionMap(String origin, String destination){
        return service.getDirection(origin, destination, "AIzaSyCo2J70QmXk3iOVwQa6Q-nL-I-dMmBAuo");
    }
}
