package com.abeltran10.carajilloapp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.databinding.FragmentMainBinding;
import com.abeltran10.carajilloapp.ui.bar.BarFragment;
import com.abeltran10.carajilloapp.ui.rating.RatingDialogFragment;
import com.abeltran10.carajilloapp.ui.rating.RatingDialogViewModel;
import com.abeltran10.carajilloapp.ui.rating.RatingDialogViewModelFactory;
import com.abeltran10.carajilloapp.ui.rating.RatingResult;
import com.abeltran10.carajilloapp.ui.rating.RatingView;
import com.abeltran10.carajilloapp.ui.register.RegisterViewModelFactory;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    private RatingDialogViewModel ratingDialogViewModel;

    private MainViewModel mainViewModel;

    private MainAdapter mainAdapter;



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

        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory())
                .get(MainViewModel.class);

        FirestoreRecyclerOptions<Bar> options = new FirestoreRecyclerOptions.Builder<Bar>()
                .setQuery(mainViewModel.getBarQuery(), Bar.class)
                .build();

        mainAdapter = new MainAdapter(options, (bar) -> {
            if (getContext() != null && getContext().getApplicationContext() != null) {
                Bundle bundle = new Bundle();
                bundle.putString("id", bar.getId());
                bundle.putString("name", bar.getName());
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
                requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                        .replace(R.id.frame_container, BarFragment.class, null)
                        .addToBackStack("main")
                        .commit();
            }
        });


        ratingDialogViewModel = new ViewModelProvider(requireActivity(), new RatingDialogViewModelFactory())
                .get(RatingDialogViewModel.class);

        ratingDialogViewModel.getRatingResult().observe(getViewLifecycleOwner(), new Observer<RatingResult>() {
            @Override
            public void onChanged(RatingResult ratingResult) {
                if (ratingResult.getError() != null) {
                    showRatingError(ratingResult.getError());
                }
                if (ratingResult.getSuccess() != null) {
                    showRatingSuccess(ratingResult.getSuccess());
                }
            }
        });

    }

    private void showRatingSuccess(RatingView success) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            String message = "Has puntuat " + success.getBarName() + " amb " + success.getRating().toString()
                    + " punts";

            Toast.makeText(
                    getContext().getApplicationContext(),
                    message,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void showRatingError(String error) {
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