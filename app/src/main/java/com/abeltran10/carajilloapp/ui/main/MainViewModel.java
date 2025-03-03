package com.abeltran10.carajilloapp.ui.main;

import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.google.firebase.firestore.Query;

public class MainViewModel extends ViewModel {
    private BarRepository barRepository;


    public MainViewModel(BarRepository repository) {
        this.barRepository = repository;
    }

    public Query getBarQuery() {
        return barRepository.getBarQuery();
    }
}