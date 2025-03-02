package com.abeltran10.carajilloapp.ui.rating;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.abeltran10.carajilloapp.R;

public class RatingDialogFragment extends DialogFragment {

    private RatingDialogViewModel ratingDialogViewModel;

    public static RatingDialogFragment newInstance() {
        return new RatingDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ratingDialogViewModel = new ViewModelProvider(this, new RatingDialogViewModelFactory())
                .get(RatingDialogViewModel.class);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_dialog, null);

        RatingBar ratingBar = view.findViewById(R.id.rating_dialog);

        String name = (getArguments() != null) ? getArguments().getString("name") : "";
        final Float rating = (getArguments() != null) ? getArguments().getFloat("rating") : 0.0f;

       AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(name);
        builder.setView(view);
        builder.setPositiveButton("Puntuar", (dialog, which) -> {
            ratingDialogViewModel.vote(rating, ratingBar.getRating());
        });
        builder.setNegativeButton("Cancel·lar", null);

        return builder.create();
    }
}