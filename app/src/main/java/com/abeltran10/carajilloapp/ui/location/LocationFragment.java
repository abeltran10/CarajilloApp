package com.abeltran10.carajilloapp.ui.location;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.databinding.FragmentLocationBinding;
import com.abeltran10.carajilloapp.databinding.FragmentMainBinding;
import com.abeltran10.carajilloapp.ui.main.MainAdapter;
import com.abeltran10.carajilloapp.ui.main.MainViewModel;
import com.abeltran10.carajilloapp.ui.main.MainViewModelFactory;
import com.abeltran10.carajilloapp.ui.rating.RatingDialogFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment {

    private FragmentLocationBinding binding;

    private LocationViewModel locationViewModel;

    private LocationAdapter locationAdapter;

    private City city;

    private RecyclerView recyclerView;

    private List<String> lastBarsIdList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationViewModel = new ViewModelProvider(requireActivity(), new LocationViewModelFactory())
                .get(LocationViewModel.class);

        city = new City();
        lastBarsIdList = new ArrayList<>();
        if (getArguments() != null) {
            city.setId(getArguments().getString("cityId"));
            city.setName(getArguments().getString("cityName"));

            lastBarsIdList.addAll(getArguments().getStringArrayList("barsIdList"));
        }

        recyclerView = binding.listView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setLocationAdapter(city, lastBarsIdList);


    }

    private void setLocationAdapter(City city, List<String> barsIdList) {
        FirestoreRecyclerOptions<Bar> options = new FirestoreRecyclerOptions.Builder<Bar>()
                .setQuery(locationViewModel.searchBars(city, barsIdList), Bar.class)
                .build();

        if (locationAdapter != null)
            locationAdapter.stopListening();

        locationAdapter = new LocationAdapter(options, city);

        recyclerView.setAdapter(locationAdapter);

        locationAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (locationAdapter != null) {
           setLocationAdapter(city, lastBarsIdList);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (locationAdapter != null) {
            locationAdapter.stopListening();
        }

    }

    @Override
    public void onDestroyView() {
        if (locationAdapter != null) {
            locationAdapter.stopListening();
            recyclerView.setAdapter(null);
            locationAdapter = null;
        }

        super.onDestroyView();
    }
}
