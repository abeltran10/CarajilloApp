package com.abeltran10.carajilloapp.ui.main;

import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MainViewModel extends ViewModel {
    private BarRepository barRepository;

    private FirestoreRecyclerOptions<Bar> options = null;

    public MainViewModel(BarRepository repository) {
        this.barRepository = repository;
    }

    public void loadBarOptions() {
        options = barRepository.getBars();
    }

    public FirestoreRecyclerOptions<Bar> getOptions() {
        return options;
    }
}