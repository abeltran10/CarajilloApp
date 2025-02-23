package com.abeltran10.carajilloapp.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.abeltran10.carajilloapp.databinding.FragmentDetailBarBinding;

public class DetailBarFragment extends Fragment {

    private DetailBarViewModel detailBarViewModel;

    private FragmentDetailBarBinding detailBarBinding;

    public static DetailBarFragment newInstance() {
        return new DetailBarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        detailBarBinding = FragmentDetailBarBinding.inflate(inflater, container, false);

        return detailBarBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailBarViewModel = new ViewModelProvider(this, new DetailBarViewModelFactory())
                .get(DetailBarViewModel.class);

        if (getArguments() != null) {
            String name = getArguments().getString("name");
            String location = getArguments().getString("location");
            Double rating = getArguments().getDouble("rating");

            TextView barName = detailBarBinding.name;
            TextView barLocation = detailBarBinding.location;
            RatingBar barRating = detailBarBinding.rating;

            barName.setText(name);
            barLocation.setText(location);
            barRating.setRating(rating.floatValue());
        }


    }

}