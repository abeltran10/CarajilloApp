package com.abeltran10.carajilloapp.ui.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.abeltran10.carajilloapp.R;

public class DetailBarFragment extends Fragment {

    private DetailBarViewModel detailBarViewModel;

    public static DetailBarFragment newInstance() {
        return new DetailBarFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail_bar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        detailBarViewModel = new ViewModelProvider(this, new DetailBarViewModelFactory())
                .get(DetailBarViewModel.class);
    }

}