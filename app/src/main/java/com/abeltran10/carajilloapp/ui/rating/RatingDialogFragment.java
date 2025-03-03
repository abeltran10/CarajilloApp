package com.abeltran10.carajilloapp.ui.rating;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.ui.register.RegisterViewModelFactory;

public class RatingDialogFragment extends DialogFragment {

    private RatingDialogViewModel ratingDialogViewModel;

    public static RatingDialogFragment newInstance() {
        return new RatingDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        ratingDialogViewModel = new ViewModelProvider(requireActivity(), new RatingDialogViewModelFactory())
                .get(RatingDialogViewModel.class);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.rating_dialog, null);

        RatingBar ratingBar = view.findViewById(R.id.rating_dialog);

        String idBar = (getArguments() != null) ? getArguments().getString("id") : null;
        String name = (getArguments() != null) ? getArguments().getString("name") : "";
        Float rating = (getArguments() != null) ? getArguments().getFloat("rating") : 0.0f;
        Long totalVotes = (getArguments() != null) ? getArguments().getLong("totalVotes") : 0L;

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(name);
        builder.setView(view);
        builder.setPositiveButton("Puntuar", (dialog, which) -> {
            ratingDialogViewModel.vote(rating, ratingBar.getRating(), totalVotes, idBar);
        });
        builder.setNegativeButton("Cancel·lar", null);


        return builder.create();
    }

}