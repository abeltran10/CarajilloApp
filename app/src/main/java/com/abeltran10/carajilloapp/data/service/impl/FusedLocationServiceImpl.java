package com.abeltran10.carajilloapp.data.service.impl;

import android.content.Context;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.location.Location;
import android.os.Looper;

import com.abeltran10.carajilloapp.data.Callback;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.service.FusedLocationService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;

public class FusedLocationServiceImpl implements FusedLocationService {

    public void getCurrentLocation(Context context, Callback callback) {
        FusedLocationProviderClient fusedClient = LocationServices.getFusedLocationProviderClient(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            callback.onComplete(new Result.Error(new SecurityException("Permis d'ubicació no concedit")));
            return;
        }

        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                3000L
        ).setMinUpdateIntervalMillis(1000L)
                .setMaxUpdateDelayMillis(5000L)
                .setWaitForAccurateLocation(true)
                .setMaxUpdates(1)
                .build();

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    Location location = locationResult.getLastLocation();
                    callback.onComplete(new Result.Success<>(location));
                } else {
                    callback.onComplete(new Result.Error(new IOException("No s'ha pogut obtindre la ubicació")));
                }
                fusedClient.removeLocationUpdates(this);
            }

        };

        try {
            fusedClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (SecurityException e) {
            callback.onComplete(new Result.Error(new SecurityException("Permis d'ubicació no concedit")));
        }
    }
}


