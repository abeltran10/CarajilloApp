package com.abeltran10.carajilloapp.ui.cities;

import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.repo.CitiesRepository;

public class CitiesViewModel extends ViewModel {

    private CitiesRepository citiesRepository;

    public CitiesViewModel(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }
}