package com.abeltran10.carajilloapp.data.service;

import android.content.Context;

import com.abeltran10.carajilloapp.data.Callback;

public interface FusedLocationService {

    public void getCurrentLocation(Context context, Callback callback);
}
