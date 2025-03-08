package com.abeltran10.carajilloapp.ui.cities;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.databinding.FragmentCitiesBinding;
import com.abeltran10.carajilloapp.ui.bar.BarFragment;
import com.abeltran10.carajilloapp.ui.main.MainFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CitiesFragment extends Fragment {

    private CitiesViewModel citiesViewModel;

    private FragmentCitiesBinding binding;

    private CityAdapter cityAdapter;

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

        cityAdapter = new CityAdapter(options, (city) -> {
            if (getContext() != null && getContext().getApplicationContext() != null) {
                Bundle bundle = new Bundle();
                bundle.putString("cityId", city.getId());
                bundle.putString("cityName", city.getName());

                requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.frame_container, MainFragment.class, bundle)
                        .addToBackStack("cities")
                        .commit();
            }
        });

        RecyclerView recyclerView = binding.listCities;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(cityAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        cityAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        cityAdapter.stopListening();
    }
}