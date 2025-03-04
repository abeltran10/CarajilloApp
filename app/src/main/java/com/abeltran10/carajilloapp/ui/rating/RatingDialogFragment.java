package com.abeltran10.carajilloapp.ui.rating;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.ui.main.MainViewModel;
import com.abeltran10.carajilloapp.ui.main.MainViewModelFactory;

public class RatingDialogFragment extends DialogFragment {

    private MainViewModel mainViewModel;

    public static RatingDialogFragment newInstance() {
        return new RatingDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mainViewModel = new ViewModelProvider(this, new MainViewModelFactory())
                .get(MainViewModel.class);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_dialog, null);

        RatingBar ratingBar = view.findViewById(R.id.rating_dialog);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        if (getArguments() != null) {
            String idBar = getArguments().getString("id");
            String name = getArguments().getString("name");
            String city = getArguments().getString("city");
            String address = getArguments().getString("address");
            String postalCode = getArguments().getString("postalCode");
            Float rating = getArguments().getFloat("rating");
            Long totalVotes = getArguments().getLong("totalVotes");

            Bar bar = new Bar(idBar, name, city, address, postalCode, rating, totalVotes);

            builder.setTitle(name);
            builder.setView(view);
            builder.setPositiveButton("Puntuar", (dialog, which) -> {
                mainViewModel.vote(ratingBar.getRating(), bar);
            });
            builder.setNegativeButton("Cancel·lar", null);
        }

        return builder.create();
    }

}