package com.abeltran10.carajilloapp.data.repo;

import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
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

    public Result createBar(String name, String address, City city, String postalCode) {
        Result result = null;
        Bar bar = null;

        name = name.toUpperCase();

        Query q = bd.collection("bars").whereEqualTo("name", name)
                .whereEqualTo("address", address)
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
                map.put("address", address);
                map.put("city", city.getId());
                map.put("postalCode", postalCode);
                map.put("rating", 0.0);
                map.put("totalVotes", 0);

                Tasks.await(bd.collection("bars").document(idBar).set(map));

                bar = new Bar(idBar);
                bar.setName(name);
                bar.setAddress(address);
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
            map.put("name", documentSnapshot.getString("name"));
            map.put("city", documentSnapshot.getString("city"));
            map.put("address", documentSnapshot.getString("address"));
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
}
