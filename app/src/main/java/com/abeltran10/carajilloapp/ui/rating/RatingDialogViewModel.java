package com.abeltran10.carajilloapp.ui.rating;

import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.RatingRepository;

public class RatingDialogViewModel extends ViewModel {
    private BarRepository barRepository;

    private RatingRepository ratingRepository;

    public RatingDialogViewModel(BarRepository barRepository, RatingRepository ratingRepository) {
        this.barRepository = barRepository;
        this.ratingRepository = ratingRepository;
    }

    public void vote(Float oldRating, Float newRating) {
         Integer result = (int)Math.ceil((oldRating.doubleValue() + newRating.doubleValue()) / 2);
         Float rating = result.floatValue();

         //ratingRepository.vote(rating);

    }
}