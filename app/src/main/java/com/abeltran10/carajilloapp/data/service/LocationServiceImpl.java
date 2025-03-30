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
    public Result addressExists(String address, String postalCode, String city) {
        OkHttpClient client = new OkHttpClient();

        String formatedAddress = address.replace(" ", "%20") + "%2C+" +
                postalCode + "+" + city.replace(" ", "%20") + "%2C+" + "Spain";


        String url = BASE_URL + "?q=" + formatedAddress + "&key=" + APIKEY;
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            String street = null;
            if (response.isSuccessful()) {
                String responseBody = (response.body() != null) ? response.body().string() : null;

                JSONObject json = new JSONObject(responseBody);
                int totalResults = json.getInt("total_results");

                // si recupera adreça i còdi postal com a mínim
                if (totalResults >= 2) {
                    JSONObject result1 = (JSONObject) json.getJSONArray("results").get(0);
                    street = result1.getJSONObject("components").getString("road");

                    if (street.indexOf("Calle ") != -1) {
                        street = street.substring(street.indexOf("Calle "));
                        street = "Carrer " + street;
                    } else if (street.indexOf("Plaza ") != -1) {
                        street = street.substring(street.indexOf("Plaza "));
                        street = "Plaça " + street;
                    } else if (street.indexOf("Avenida ") != -1) {
                        street = street.substring(street.indexOf("Avenida "));
                        street = "Avinguda " + street;
                    }

                }

            }

            return new Result.Success<String>(street);
        } catch (IOException | IllegalStateException | JSONException ex) {
            return new Result.Error(new Exception("Ha hagut un problema i no s'ha pogut validar l'adreça"));
        }

    }

}
