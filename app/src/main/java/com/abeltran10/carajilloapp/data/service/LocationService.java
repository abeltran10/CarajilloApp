package com.abeltran10.carajilloapp.data.service;

import com.abeltran10.carajilloapp.data.LocationCallback;

public interface LocationService {

     void asyncAddressExists(String address, String number, String postalCode, String city,
                           LocationCallback<Boolean> callback);

}
