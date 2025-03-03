package com.abeltran10.carajilloapp.ui.rating;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.Rating;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.RatingRepository;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RatingDialogViewModel extends ViewModel {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private BarRepository barRepository;

    private RatingRepository ratingRepository;

    private MutableLiveData<RatingResult> ratingResult = new MutableLiveData<>();

    public RatingDialogViewModel(BarRepository barRepository, RatingRepository ratingRepository) {
        this.barRepository = barRepository;
        this.ratingRepository = ratingRepository;
    }

    public MutableLiveData<RatingResult> getRatingResult() {
        return ratingResult;
    }

    public void vote(Float oldRating, Float newRating, Long totalVotes, String idBar) {
        executorService.execute(() -> {

            Float oldTotal = oldRating * totalVotes;
            Float averageResult = (oldTotal + newRating) / (totalVotes + 1);

            Result resultVote = ratingRepository.vote(newRating, idBar);

            if (resultVote instanceof Result.Success) {
                Rating dataRating = ((Result.Success<Rating>) resultVote).getData();

                Result resultBar = barRepository.updateBar(averageResult, idBar);

                if (resultBar instanceof Result.Success) {
                    Bar dataBar = ((Result.Success<Bar>) resultBar).getData();
                    ratingResult.postValue(new RatingResult(new RatingView(dataBar.getName(), dataRating.getVote())));
                } else {
                   ratingResult.postValue(new RatingResult((((Result.Error)resultBar).getError().getMessage())));
                }
            } else {
                ratingResult.postValue(new RatingResult((((Result.Error)resultVote).getError().getMessage())));
            }
        });
    }
}