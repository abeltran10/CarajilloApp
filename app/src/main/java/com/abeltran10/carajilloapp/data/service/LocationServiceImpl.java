package com.abeltran10.carajilloapp.data.service;

import com.abeltran10.carajilloapp.BuildConfig;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LocationServiceImpl implements LocationService {

    public static final String APIKEY = BuildConfig.APIKEY;
    private static final String BASE_URL = "https://api.opencagedata.com/geocode/v1/json";

    @Override
    public boolean isAddressValid(String address, String number, String postalCode, String city) {
        OkHttpClient client = new OkHttpClient();

        String formatedAddress = address.replace(" ", "%20") + "+" + number + "%2C+" +
                postalCode + "+" + city.replace(" ", "%20") + "%2C+" + "Spain";

        try {

            String url = BASE_URL + "?q=" + formatedAddress + "&key=" + APIKEY;

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                // Parsear la respuesta JSON
                JSONObject json = new JSONObject(responseBody);
                int totalResults = json.getInt("total_results");

                // si recupera direcció i códi postal com a mínim
                return totalResults >= 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
