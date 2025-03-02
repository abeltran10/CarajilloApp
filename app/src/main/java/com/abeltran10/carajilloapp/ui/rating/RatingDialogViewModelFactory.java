package com.abeltran10.carajilloapp.ui.rating;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.RatingRepository;

public class RatingDialogViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RatingDialogViewModel.class)) {
            return (T) new RatingDialogViewModel(BarRepository.getInstance(), RatingRepository.getInstance());
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
