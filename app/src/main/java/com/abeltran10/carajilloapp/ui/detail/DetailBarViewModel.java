package com.abeltran10.carajilloapp.ui.detail;

import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.repo.BarRepository;

public class DetailBarViewModel extends ViewModel {
    private BarRepository barRepository;

    public DetailBarViewModel(BarRepository barRepository) {
        this.barRepository = barRepository;
    }

}