package com.abeltran10.carajilloapp.ui.rating;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.Rating;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.RatingRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RatingDialogViewModel extends ViewModel {

    //private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private BarRepository barRepository;

    private RatingRepository ratingRepository;

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    private MutableLiveData<RatingResult> ratingResult = new MutableLiveData<>();

    public RatingDialogViewModel(BarRepository barRepository, RatingRepository ratingRepository) {
        this.barRepository = barRepository;
        this.ratingRepository = ratingRepository;
    }

    public MutableLiveData<RatingResult> getRatingResult() {
        return ratingResult;
    }

    public void vote(Float oldRating, Float newRating, Long totalVotes, String idBar, String name) {
          bd.runTransaction((transaction) -> {
                Float oldTotal = oldRating * totalVotes;
                Float averageResult = (oldTotal + newRating) / (totalVotes + 1);
                try {
                    ratingRepository.vote(transaction, newRating, idBar);
                    barRepository.updateBar(transaction, averageResult, idBar);

                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }

                return null;
            }).addOnSuccessListener(aVoid -> {
                ratingResult.postValue(new RatingResult(new RatingView(name, newRating)));
            }).addOnFailureListener(e -> {
                ratingResult.postValue(new RatingResult(e.getMessage()));
            });

    }
}