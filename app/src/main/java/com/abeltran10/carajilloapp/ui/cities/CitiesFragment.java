package com.abeltran10.carajilloapp.ui.cities;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.databinding.FragmentCitiesBinding;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CitiesFragment extends Fragment {

    private CitiesViewModel citiesViewModel;

    private FragmentCitiesBinding binding;

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    public static CitiesFragment newInstance() {
        return new CitiesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCitiesBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        citiesViewModel = new ViewModelProvider(this, new CitiesViewModelFactory())
                .get(CitiesViewModel.class);

        Query query = bd.collection("cities").orderBy("name", Query.Direction.ASCENDING);;
        FirestoreRecyclerOptions<City> options = new FirestoreRecyclerOptions.Builder<City>()
                .setQuery(query, City.class)
                .build();
    }
}