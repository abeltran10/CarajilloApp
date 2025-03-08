package com.abeltran10.carajilloapp.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.CitiesRepository;
import com.abeltran10.carajilloapp.data.repo.RatingRepository;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends ViewModel {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private BarRepository barRepository;

    private RatingRepository ratingRepository;

    private CitiesRepository citiesRepository;

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    private MutableLiveData<MainResult> mainResult = new MutableLiveData<>();


    public MainViewModel(BarRepository barRepository, RatingRepository ratingRepository, CitiesRepository citiesRepository) {
        this.barRepository = barRepository;
        this.ratingRepository = ratingRepository;
        this.citiesRepository = citiesRepository;
    }

    public MutableLiveData<MainResult> getMainResult() {
        return mainResult;
    }

    public void vote(Float newRating, Bar bar) {
        bd.runTransaction((transaction) -> {
            Float oldTotal = bar.getRating() * bar.getTotalVotes();
            Float averageResult = (oldTotal + newRating) / (bar.getTotalVotes() + 1);
            try {
                ratingRepository.vote(transaction, newRating, bar.getId());
                barRepository.updateBar(transaction, averageResult, bar.getId());

            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

            return null;
        }).addOnSuccessListener(aVoid -> {
            mainResult.postValue(new MainResult(new MainView(newRating, bar)));
        }).addOnFailureListener(e -> {
            mainResult.postValue(new MainResult(e.getMessage()));
        });

    }
}