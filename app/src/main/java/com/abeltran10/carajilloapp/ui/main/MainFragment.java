package com.abeltran10.carajilloapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.databinding.FragmentMainBinding;
import com.abeltran10.carajilloapp.ui.bar.BarFragment;
import com.abeltran10.carajilloapp.ui.rating.RatingDialogFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    private MainViewModel mainViewModel;

    private MainAdapter mainAdapter;

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();



    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity(), new MainViewModelFactory())
                .get(MainViewModel.class);

        City city = new City();
        if (getArguments() != null) {
            city.setId(getArguments().getString("cityId"));
            city.setName(getArguments().getString("cityName"));
        }
        Query query = bd.collection("bars").where(Filter.equalTo("city", city.getId()))
                .orderBy("name", Query.Direction.ASCENDING);;
        FirestoreRecyclerOptions<Bar> options = new FirestoreRecyclerOptions.Builder<Bar>()
                .setQuery(query, Bar.class)
                .build();

        mainAdapter = new MainAdapter(options, city, (bar, c) -> {
            if (getContext() != null && getContext().getApplicationContext() != null) {
                Bundle bundle = new Bundle();
                bundle.putString("id", bar.getId());
                bundle.putString("name", bar.getName());
                bundle.putString("cityId", c.getId());
                bundle.putString("cityName", c.getName());
                bundle.putString("address", bar.getAddress());
                bundle.putString("postalCode", bar.getPostalCode());
                bundle.putFloat("rating", bar.getRating());
                bundle.putLong("totalVotes", bar.getTotalVotes());

                RatingDialogFragment ratingDialogFragment = RatingDialogFragment.newInstance();
                ratingDialogFragment.setArguments(bundle);

                ratingDialogFragment.show(requireActivity().getSupportFragmentManager(), "ratingDialog");
            }
        });

        RecyclerView recyclerView = binding.listView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mainAdapter);

        FloatingActionButton fab = binding.floatingButton;
        fab.setOnClickListener(view1 -> {
            if (getContext() != null && getContext().getApplicationContext() != null) {
                Bundle bundle = new Bundle();
                bundle.putString("cityId", city.getId());
                bundle.putString("cityName", city.getName());

                requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.frame_container, BarFragment.class, bundle)
                        .addToBackStack("main")
                        .commit();
            }
        });

        mainViewModel.getMainResult().observe(getViewLifecycleOwner(), mainResult -> {
            if (mainResult.getError() != null) {
                showMainError(mainResult.getError());
            }
            if (mainResult.getSuccess() != null) {
                showMainSuccess(mainResult.getSuccess());
            }
        });

    }

    private void showMainSuccess(MainView success) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            String message = "Has puntuat " + success.getBar().getName() + " amb " + success.getRating().toString()
                    + " punts";

            Toast.makeText(
                    getContext().getApplicationContext(),
                    message,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showMainError(String error) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    error,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}