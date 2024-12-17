package com.abeltran10.carajilloapp.data.repo;

import com.abeltran10.carajilloapp.data.model.Bar;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class BarRepository {

    private static volatile BarRepository instance;

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    public BarRepository() {
    }

    public static BarRepository getInstance() {
        if (instance == null) {
            instance = new BarRepository();
        }
        return instance;
    }

    public FirestoreRecyclerOptions<Bar> getBars() {
        CollectionReference ref = bd.collection("bars");
        Query q = ref.orderBy("name");

        return new FirestoreRecyclerOptions.Builder<Bar>()
                .setQuery(q, Bar.class)
                .build();
    }
}
