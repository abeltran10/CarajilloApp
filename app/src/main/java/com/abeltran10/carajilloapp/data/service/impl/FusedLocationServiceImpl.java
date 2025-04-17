package com.abeltran10.carajilloapp.data.service.impl;

import android.content.Context;

import androidx.activity.result.IntentSenderRequest;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.location.Location;
import android.os.Looper;

import com.abeltran10.carajilloapp.data.Callback;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.service.FusedLocationService;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;

public class FusedLocationServiceImpl implements FusedLocationService {

    private FusedLocationProviderClient fusedClient;

    public FusedLocationServiceImpl() {
    }


    public void getCurrentLocation(Context context, Callback callback, Callback resolutionCallback) {
        fusedClient = LocationServices.getFusedLocationProviderClient(context);
        SettingsClient settingsClient = LocationServices.getSettingsClient(context);


        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                3000L
        ).setMinUpdateIntervalMillis(1000L)
                .setMaxUpdateDelayMillis(5000L)
                .setWaitForAccurateLocation(true)
                .setMaxUpdates(1)
                .build();

        LocationSettingsRequest settingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true)
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


            settingsClient.checkLocationSettings(settingsRequest)
                    .addOnSuccessListener(response -> {
                        requestLocation(context, locationRequest, locationCallback, Looper.getMainLooper(), callback);
                    })
                    .addOnFailureListener(e -> {
                        if (e instanceof ResolvableApiException) {
                            try {
                                IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(
                                        ((ResolvableApiException) e).getResolution()
                                ).build();
                                resolutionCallback.onComplete(new Result.Success<>(intentSenderRequest));
                            } catch (Exception ex) {
                                resolutionCallback.onComplete(new Result.Error(new IOException("No es pot activar el GPS")));
                            }
                        } else {
                            resolutionCallback.onComplete(new Result.Error(new IOException("No es pot activar el GPS")));
                        }
                    });

    }

    private void requestLocation(Context context, LocationRequest locationRequest, LocationCallback locationCallback,
                                 Looper looper, Callback callback) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            callback.onComplete(new Result.Error(new SecurityException("Permis d'ubicació no concedit")));
            return;
        }

        try {
            fusedClient.requestLocationUpdates(locationRequest, locationCallback, looper);
        } catch (SecurityException e) {
                callback.onComplete(new Result.Error(new SecurityException("Permis d'ubicació no concedit")));
        }
    }

}


