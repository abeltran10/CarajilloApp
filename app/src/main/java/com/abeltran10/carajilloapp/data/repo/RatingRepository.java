package com.abeltran10.carajilloapp.data.repo;

import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Rating;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RatingRepository {

    private static volatile RatingRepository instance;

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private Rating rating;

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public static RatingRepository getInstance() {
        if (instance == null) {
            instance = new RatingRepository();
        }
        return instance;
    }

    public void vote(Transaction transaction, Float newRating, String idBar) throws IOException {
        String idUser = mAuth.getCurrentUser().getUid();
        Query q = bd.collection("ratings").where(Filter.and(Filter.equalTo("idBar", idBar),
                Filter.equalTo("idUser", idUser)));
        try {
            QuerySnapshot querySnapshot = Tasks.await(q.get());

            if (querySnapshot.getDocuments().isEmpty()) {
                Map<String, Object> map = new HashMap<>();
                map.put("idBar", idBar);
                map.put("idUser", idUser);
                map.put("rating", newRating);

                DocumentReference documentReference = bd.collection("ratings").document();
                transaction.set(documentReference, map);

            } else {
                throw new IOException("No pots tornar a puntuar aquest bar");
            }


        } catch (ExecutionException | InterruptedException e) {
            throw new IOException("Ha hagut un problema al registrar la puntuació");
        }

    }
}
