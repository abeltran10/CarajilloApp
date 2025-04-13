package com.abeltran10.carajilloapp.data.service;

import com.abeltran10.carajilloapp.data.Result;

public interface LocationService {

     Result getStreet(String address, String postalCode, String city);

}
