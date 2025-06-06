package com.abeltran10.carajilloapp.ui.cities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.databinding.FragmentCitiesBinding;
import com.abeltran10.carajilloapp.ui.main.MainFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.Query;

public class CitiesFragment extends Fragment {

    private CitiesViewModel citiesViewModel;

    private FragmentCitiesBinding binding;

    private CityAdapter cityAdapter;

    private RecyclerView recyclerView;

    private String lastSearchQuery = "";

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


        recyclerView = binding.listCities;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setCityAdapter("");

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu, menu);

                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) searchItem.getActionView();
                searchView.setQueryHint("Cerca un poble...");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String text) {
                        setCityAdapter(text);
                        lastSearchQuery = text;
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        setCityAdapter(newText);
                        lastSearchQuery = newText;

                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner());
    }

    private void setCityAdapter(String search) {
        Query query = citiesViewModel.searchCities(search);

        if (cityAdapter != null) {
            cityAdapter.stopListening();
        }

        FirestoreRecyclerOptions<City> options = new FirestoreRecyclerOptions.Builder<City>()
                .setQuery(query, City.class)
                .build();

        cityAdapter = new CityAdapter(options, (viewHolder, city) -> {
            if (!isAdded() || binding == null) return;

            citiesViewModel.getTotalBarsByCity(city.getId())
                    .get(AggregateSource.SERVER)
                    .addOnSuccessListener(aggregateQuerySnapshot ->
                            viewHolder.getTotalBars().setText(aggregateQuerySnapshot.getCount() + " bars")
                    ).addOnFailureListener(e -> {
                        viewHolder.getTotalBars().setText("No s'ha pogut recuperar el total de bars");
                    });
        });

        cityAdapter.setOnItemClickListener((city) -> {
            if (getContext() != null) {
                Bundle bundle = new Bundle();
                bundle.putString("cityId", city.getId());
                bundle.putString("cityName", city.getName());

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.frame_container, MainFragment.class, bundle)
                        .addToBackStack("cities")
                        .commit();
            }
        });

        recyclerView.setAdapter(cityAdapter);
        cityAdapter.startListening();
    }




    @Override
    public void onStart() {
        super.onStart();
        if (cityAdapter != null) {
            setCityAdapter(lastSearchQuery);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (cityAdapter != null) {
            cityAdapter.stopListening();
        }
    }

    @Override
    public void onDestroyView() {
        if (cityAdapter != null) {
            cityAdapter.stopListening();
            recyclerView.setAdapter(null);
            cityAdapter = null;
        }

        super.onDestroyView();
    }
}