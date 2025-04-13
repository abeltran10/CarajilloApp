package com.abeltran10.carajilloapp.data.repo;

import android.location.Location;

import androidx.annotation.NonNull;

import com.abeltran10.carajilloapp.data.Callback;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.data.model.Street;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public Result createBar(String name, Street address, City city, String postalCode) {
        Result result = null;
        Bar bar = null;

        name = name.toUpperCase();

        Query q = bd.collection("bars").whereEqualTo("name", name)
                .whereEqualTo("address", address.getName())
                .whereEqualTo("city", city.getId());

        try {
            QuerySnapshot querySnapshot = Tasks.await(q.get(Source.SERVER));

            if (querySnapshot.getDocuments().isEmpty()) {

                AggregateQuerySnapshot aggregateQuerySnapshot = Tasks.await(bd.collection("bars")
                        .count().get(AggregateSource.SERVER));
                String idBar = String.valueOf(aggregateQuerySnapshot.getCount() + 1);

                Map<String, Object> map = new HashMap<>();
                map.put("id", idBar);
                map.put("name", name);
                map.put("address", address.getName());
                map.put("city", city.getId());
                map.put("postalCode", postalCode);
                map.put("latitude", address.getLatitude());
                map.put("longitude", address.getLongitude());
                map.put("rating", 0.0);
                map.put("totalVotes", 0);

                Tasks.await(bd.collection("bars").document(idBar).set(map));

                bar = new Bar(idBar);
                bar.setName(name);
                bar.setAddress(address.getName());
                bar.setLatitude(address.getLatitude());
                bar.setLongitude(address.getLongitude());
                bar.setCity(city.getId());
                bar.setPostalCode(postalCode);
                bar.setRating(((Number)map.get("rating")).floatValue());
                bar.setTotalVotes(((Number)map.get("totalVotes")).longValue());
                setBar(bar);

                result = new Result.Success<Bar>(this.bar);
            } else {
                result = new Result.Error(new IOException("Ja s'ha registrat aquest bar"));
            }

        } catch (ExecutionException | InterruptedException e) {
            result = new Result.Error(new IOException("Ha hagut un problema i no s'ha registrat el bar"));
        }

        return result;

    }


    public void updateBar(Transaction transaction, Float averageResult, String idBar) throws IOException {
        Map<String, Object> map = new HashMap<>();

        try {
            DocumentSnapshot documentSnapshot = Tasks.await(bd.collection("bars").document(idBar).get());

            map.put("id", documentSnapshot.getString("id"));
            map.put("name", documentSnapshot.getString("name"));
            map.put("city", documentSnapshot.getString("city"));
            map.put("address", documentSnapshot.getString("address"));
            map.put("latitude", documentSnapshot.getDouble("latitude"));
            map.put("longitude", documentSnapshot.getDouble("longitude"));
            map.put("postalCode", documentSnapshot.getString("postalCode"));
            map.put("rating", averageResult);
            map.put("totalVotes", documentSnapshot.getLong("totalVotes") + 1L);

            DocumentReference documentReference = bd.collection("bars").document(idBar);
            transaction.set(documentReference, map);

        } catch (ExecutionException | InterruptedException e) {
            throw new IOException("Ha hagut un problema al actualitzar la puntucació del bar");
        }

    }

    public AggregateQuery getTotalBarsByCity(String cityId) {
        CollectionReference collectionReference = bd.collection("bars");

        return collectionReference.where(Filter.equalTo("city", cityId)).count();
    }

    public void getNearBars(Location location, String cityId, Callback callback) {
        List<Bar> listBars = new ArrayList<>();
        bd.collection("bars").whereEqualTo("city", cityId)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Bar bar = doc.toObject(Bar.class);

                            Location barLocation = new Location("Bar");
                            barLocation.setLatitude(bar.getLatitude());
                            barLocation.setLongitude(bar.getLongitude());

                            float distance = location.distanceTo(barLocation);

                            if (distance <= 500.0f) {
                                listBars.add(bar);
                            }
                        }

                        if (listBars.isEmpty()) {
                            callback.onComplete(new Result.Error(new IOException("No s'han trobar bars a prop")));
                        } else {
                            callback.onComplete(new Result.Success<List<Bar>>(listBars));
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onComplete(new Result.Error(new IOException("Ha hagut un problema al cercar els bars")));
                    }
                });

    }
}
