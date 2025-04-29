package com.abeltran10.carajilloapp.ui.location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.CitiesRepository;
import com.abeltran10.carajilloapp.data.repo.RatingRepository;
import com.abeltran10.carajilloapp.ui.main.MainViewModel;

public class LocationViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel > T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LocationViewModel.class)) {
            return (T) new LocationViewModel();
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
