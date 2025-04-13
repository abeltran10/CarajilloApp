package com.abeltran10.carajilloapp.data.service.impl;

import com.abeltran10.carajilloapp.BuildConfig;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Street;
import com.abeltran10.carajilloapp.data.service.LocationService;

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
    public Result getStreet(String address, String postalCode, String city) {
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
            Street street = null;
            if (response.isSuccessful()) {
                String responseBody = (response.body() != null) ? response.body().string() : null;

                JSONObject json = new JSONObject(responseBody);
                int totalResults = json.getInt("total_results");

                // si recupera adreça i còdi postal com a mínim
                if (totalResults >= 2) {
                    street = new Street();
                    JSONObject result1 = (JSONObject) json.getJSONArray("results").get(0);
                    String road = result1.getJSONObject("components").getString("road");
                    Double lat = result1.getJSONObject("geometry").getDouble("lat");
                    Double lng = result1.getJSONObject("geometry").getDouble("lng");

                    if (road.indexOf("Calle ") != -1) {
                        road = road.substring(road.indexOf("Calle "));
                        road = "Carrer " + road;
                    } else if (road.indexOf("Plaza ") != -1) {
                        road = road.substring(road.indexOf("Plaza "));
                        road = "Plaça " + road;
                    } else if (road.indexOf("Avenida ") != -1) {
                        road = road.substring(road.indexOf("Avenida "));
                        road = "Avinguda " + road;
                    }

                    street.setName(road);
                    street.setLatitude(lat);
                    street.setLongitude(lng);

                    return new Result.Success<Street>(street);
                }

            }

            return new Result.Error(new Exception("L'adreça no és valida"));
        } catch (IOException | IllegalStateException | JSONException ex) {
            return new Result.Error(new Exception("Ha hagut un problema i no s'ha pogut validar l'adreça"));
        }

    }

}
