package com.abeltran10.carajilloapp.ui.bar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.databinding.FragmentBarBinding;
import com.abeltran10.carajilloapp.ui.main.MainFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BarFragment extends Fragment {

    private FragmentBarBinding binding;

    private BarViewModel barViewModel;

    public static BarFragment newInstance() {
        return new BarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentBarBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barViewModel = new ViewModelProvider(this, new BarViewModelFactory()).get(BarViewModel.class);

        City city = new City();
        if (getArguments() != null) {
            city.setId(getArguments().getString("cityId"));
            city.setName(getArguments().getString("cityName"));
        }

        ProgressBar loadingBar = binding.loadingBar;
        EditText barName = binding.barName;
        EditText barCity = binding.barCity;
        EditText barAddress = binding.barAddress;
        EditText barPostalCode = binding.barPostalCode;
        Button barCreate = binding.createBar;

        barCity.setText(city.getName());
        barCity.setEnabled(false);

        barViewModel.getBarResult().observe(getViewLifecycleOwner(), new Observer<BarResult>() {
            @Override
            public void onChanged(BarResult barResult) {
                if (barResult == null) {
                    return;
                }

                loadingBar.setVisibility(View.GONE);

                if (barResult.getError() != null) {
                    showCreaterBarFailed(barResult.getError());
                }

                if (barResult.getSuccess() != null) {
                    updateUI(barResult.getSuccess(), city);
                }

            }
        });

        barViewModel.getBarFormState().observe(getViewLifecycleOwner(), new Observer<BarFormState>() {
            @Override
            public void onChanged(BarFormState barFormState) {
                barCreate.setEnabled(barFormState.isDataValid());

                if (barFormState.getNameError() != null) {
                    barName.setError(getString(barFormState.getNameError()));
                }

                if (barFormState.getAddressError() != null) {
                    barAddress.setError(getString(barFormState.getAddressError()));
                }

                if (barFormState.getPostalCodeError() != null) {
                    barPostalCode.setError(getString(barFormState.getPostalCodeError()));
                }

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                barViewModel.barDataChanged(barName.getText().toString(),
                        barAddress.getText().toString(),
                        barCity.getText().toString(), barPostalCode.getText().toString());
            }
        };

        barName.addTextChangedListener(afterTextChangedListener);
        barAddress.addTextChangedListener(afterTextChangedListener);
        barCity.addTextChangedListener(afterTextChangedListener);
        barPostalCode.addTextChangedListener(afterTextChangedListener);

        barCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setVisibility(View.VISIBLE);
                barViewModel.create(barName.getText().toString(),
                        barAddress.getText().toString(),
                        city, barPostalCode.getText().toString());
            }
        });
    }



    private void updateUI(BarView success,City city) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    "Bar: " + success.getName().toUpperCase() + " afegit amb exit.",
                    Toast.LENGTH_LONG).show();

            Bundle bundle = new Bundle();
            bundle.putString("cityId", city.getId());
            bundle.putString("cityName", city.getName());

            requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .replace(R.id.frame_container, MainFragment.class, bundle)
                    .commit();
        }
    }

    private void showCreaterBarFailed(String errorString) {
        if (getContext() != null && getContext().getApplicationContext() != null) {
            Toast.makeText(
                    getContext().getApplicationContext(),
                    errorString,
                    Toast.LENGTH_LONG).show();
        }
    }
}