package com.abeltran10.carajilloapp.data.service;

import com.abeltran10.carajilloapp.data.Callback;
import com.abeltran10.carajilloapp.data.Result;

public interface LocationService {

     Result addressExists(String address, String number, String postalCode, String city);

}
