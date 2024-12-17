package com.abeltran10.carajilloapp.ui.main;

import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.UserRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class MainViewModel extends ViewModel {
    private BarRepository barRepository;


    public MainViewModel(BarRepository repository) {
        this.barRepository = repository;
    }

    public FirestoreRecyclerOptions<Bar> getBars() {
        return barRepository.getBars();
    }
}