package com.abeltran10.carajilloapp.data.repo;

import com.abeltran10.carajilloapp.data.RepositoryCallback;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;

import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.ExecutionException;

public class BarRepository {

    private static volatile BarRepository instance;

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    private Bar bar;

    public BarRepository() {
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public static BarRepository getInstance() {
        if (instance == null) {
            instance = new BarRepository();
        }
        return instance;
    }

    public FirestoreRecyclerOptions<Bar> getBars() {
        CollectionReference ref = bd.collection("bars");
        Query q = ref.orderBy("name", Query.Direction.ASCENDING);

        return new FirestoreRecyclerOptions.Builder<Bar>()
                .setQuery(q, Bar.class)
                .build();
    }

    public void asyncCreateBar(String name, String address, String city, String postalCode, RepositoryCallback<Bar> callback) {
        Runnable runnable = () -> {
            try {
                Result<Bar> result = createBar(name, address, city, postalCode);
                callback.onComplete(result);
            } catch (Exception e) {
                Result<Bar> errorResult = new Result.Error(new IOException("Error al afegir el bar a la base de dades"));
                callback.onComplete(errorResult);
            }
        };

        new Thread(runnable).start();
    }

    private Result<Bar> createBar(String name, String address, String city, String postalCode) {
        Result<Bar> result = null;
        Query q = bd.collection("bars").where(Filter.and(Filter.equalTo("address",address),
                Filter.equalTo("city", city), Filter.equalTo("postalCode", postalCode)));

        try {
            QuerySnapshot querySnapshot = Tasks.await(q.get());

            if (querySnapshot.getDocuments().isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", name);
                map.put("address", address);
                map.put("city", city);
                map.put("postalCode", postalCode);
                map.put("rating", 0.0);

                Tasks.await(bd.collection("bars").add(map));

                bar = new Bar();
                bar.setName(name);
                bar.setAddress(address);
                bar.setCity(city);
                bar.setPostalCode(postalCode);
                bar.setRating((Double) map.get("rating"));


                result = new Result.Success<Bar>(bar);
            } else {
                result = new Result.Error(new IOException("Ja s'ha registrat aquest bar"));
            }

        } catch (ExecutionException | InterruptedException e) {
            result = new Result.Error(new IOException("Ha agut un problema i no s'ha registrat el bar"));
        }

        return result;

    }
}
