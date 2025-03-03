package com.abeltran10.carajilloapp.data.service;

import com.abeltran10.carajilloapp.BuildConfig;
import com.abeltran10.carajilloapp.data.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationServiceImpl implements LocationService {

    private static final String APIKEY = BuildConfig.APIKEY;
    private static final String BASE_URL = "https://api.opencagedata.com/geocode/v1/json";


    @Override
    public Result addressExists(String address, String number, String postalCode, String city) {
        OkHttpClient client = new OkHttpClient();

        String formatedAddress = address.replace(" ", "%20") + "+" + number + "%2C+" +
                postalCode + "+" + city.replace(" ", "%20") + "%2C+" + "Spain";


        String url = BASE_URL + "?q=" + formatedAddress + "&key=" + APIKEY;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = (response.body() != null) ? response.body().string() : null;

                JSONObject json = new JSONObject(responseBody);
                int totalResults = json.getInt("total_results");

                // si recupera adreça i còdi postal com a mínim
                boolean exists =  totalResults >= 2;

                return new Result.Success<Boolean>(exists);
            }

            return new Result.Success<Boolean>(false);
        } catch (IOException | IllegalStateException | JSONException ex) {
            return new Result.Error(new Exception("Ha hagut un problema i no s'ha pogut validar l'adreça"));
        }

    }

}
