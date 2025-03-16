package com.abeltran10.carajilloapp.ui.cities;

import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.CitiesRepository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CitiesViewModel extends ViewModel {

    private CitiesRepository citiesRepository;

    private BarRepository barRepository;

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    public CitiesViewModel(CitiesRepository citiesRepository, BarRepository barRepository) {
        this.citiesRepository = citiesRepository;
        this.barRepository = barRepository;
    }

    public AggregateQuery getTotalBarsByCity(String cityId) {
        return barRepository.getTotalBarsByCity(cityId);
    }

    public Query searchCities(String searchText) {
        Query query;

        if (searchText.isEmpty()) {
            query = bd.collection("cities").orderBy("name", Query.Direction.ASCENDING);
        } else {
            query = bd.collection("cities")
                    .orderBy("name", Query.Direction.ASCENDING)
                    .startAt(searchText)
                    .endAt(searchText + "\uf8ff");
        }

        return query;
    }
}