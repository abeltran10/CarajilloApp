package com.abeltran10.carajilloapp.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.EventWrapper;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.databinding.FragmentMainBinding;
import com.abeltran10.carajilloapp.ui.bar.BarFragment;
import com.abeltran10.carajilloapp.ui.location.LocationFragment;
import com.abeltran10.carajilloapp.ui.rating.RatingDialogFragment;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    private MainViewModel mainViewModel;

    private MainAdapter mainAdapter;

    private RecyclerView recyclerView;

    private ProgressBar loadingBar;

    private City city;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    private ActivityResultLauncher<IntentSenderRequest> gpsLauncher;

    private String lastSearchQuery = "";


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestLocationPermission();
        gpsLauncher();
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

        city = new City();
        if (getArguments() != null) {
            city.setId(getArguments().getString("cityId"));
            city.setName(getArguments().getString("cityName"));
        }

        recyclerView = binding.listView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        loadingBar = binding.loadingBar;

        setMainAdapter(city, "");

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

        mainViewModel.getMainResult().observe(getViewLifecycleOwner(), eventWrapper -> {
            MainResult mainResult = eventWrapper.getContentIfNotHandled();

            if (mainResult != null && mainResult.getError() != null) {
                showMainError(mainResult.getError());
            }
            if (mainResult != null && mainResult.getSuccess() != null) {
                showMainSuccess(mainResult.getSuccess());
            }
        });

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_bar, menu);

                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) searchItem.getActionView();
                searchView.setQueryHint("Cerca un bar...");

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String text) {
                        setMainAdapter(city, text);
                        lastSearchQuery = text;

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        setMainAdapter(city, newText);
                        lastSearchQuery = newText;

                        return true;
                    }
                });

                MenuItem locationItem = menu.findItem(R.id.action_location);
                locationItem.setOnMenuItemClickListener(view -> {

                    if (ContextCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        loadingBar.setVisibility(View.VISIBLE);
                        mainViewModel.loadCurrentLocation(requireActivity().getApplicationContext(), city);
                    } else {
                        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
                    }

                    return true;
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner());

        mainViewModel.getMainLocationResult().observe(getViewLifecycleOwner(), eventWrapper -> {
            loadingBar.setVisibility(View.GONE);
            MainLocationResult mainLocationResult = eventWrapper.getContentIfNotHandled();
            if (mainLocationResult != null && mainLocationResult.getSuccess() != null) {
                List<Bar> barList = mainLocationResult.getSuccess();
                List<String> barIdList = barList.stream()
                        .map(Bar::getId)
                        .collect(Collectors.toList());

                Bundle bundle = new Bundle();
                bundle.putString("cityId", city.getId());
                bundle.putString("cityName", city.getName());
                bundle.putStringArrayList("barsIdList", (ArrayList<String>) barIdList);

                requireActivity().getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.frame_container, LocationFragment.class, bundle)
                        .addToBackStack("main")
                        .commit();
            }

            if (mainLocationResult != null && mainLocationResult.getError() != null) {
                showMainError(mainLocationResult.getError());
            }
        });

        mainViewModel.getMainGPSResult().observe(getViewLifecycleOwner(), eventWrapper -> {
            MainIntentResult mainIntentResult = eventWrapper.getContentIfNotHandled();

            if (mainIntentResult != null && mainIntentResult.getSuccess() != null) {
                IntentSenderRequest intentSenderRequest = mainIntentResult.getSuccess();
                gpsLauncher.launch(intentSenderRequest);
            }

            if (mainIntentResult != null && mainIntentResult.getError() != null) {
                showMainError(mainIntentResult.getError());
            }
        });

    }

    private void requestLocationPermission() {
        requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (isGranted) {
                        loadingBar.setVisibility(View.VISIBLE);
                        mainViewModel.loadCurrentLocation(requireActivity().getApplicationContext(), city);
                    } else {
                        Toast.makeText(getContext(), "Permis d'ubicació denegat", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void gpsLauncher() {
        gpsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        loadingBar.setVisibility(View.VISIBLE);
                        mainViewModel.loadCurrentLocation(requireActivity().getApplicationContext(), city);
                    } else {
                        Toast.makeText(getContext(), "GPS no activat", Toast.LENGTH_SHORT).show();
                    }
                }
        );
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

    private void setMainAdapter(City city, String search) {
        FirestoreRecyclerOptions<Bar> options = new FirestoreRecyclerOptions.Builder<Bar>()
                .setQuery(mainViewModel.searchBars(city, search), Bar.class)
                .build();

        if (mainAdapter != null)
            mainAdapter.stopListening();

        mainAdapter = new MainAdapter(options, city);

        mainAdapter.setOnItemClickListener((bar, c) -> {
            if (getContext() != null && getContext().getApplicationContext() != null) {
                Bundle bundle = new Bundle();
                bundle.putString("id", bar.getId());
                bundle.putString("name", bar.getName());
                bundle.putString("cityId", c.getId());
                bundle.putString("cityName", c.getName());
                bundle.putString("address", bar.getAddress());
                bundle.putDouble("latitude", bar.getLatitude());
                bundle.putDouble("longitude", bar.getLongitude());
                bundle.putString("postalCode", bar.getPostalCode());
                bundle.putFloat("rating", bar.getRating());
                bundle.putLong("totalVotes", bar.getTotalVotes());

                RatingDialogFragment ratingDialogFragment = RatingDialogFragment.newInstance();
                ratingDialogFragment.setArguments(bundle);

                ratingDialogFragment.show(requireActivity().getSupportFragmentManager(), "ratingDialog");
            }
        });

        mainAdapter.setOnBtnShareListener((bar, c) -> {
            String location = bar.getName() + ", " + bar.getAddress() + " "
                    + bar.getPostalCode() + ", " + c.getName();

            String message = "Prova els carajillos d'ací! \n" +
                    "Nom: " + bar.getName() + "\n" +
                    "Adreça: " + bar.getAddress() + "\n" +
                    "Població: " + c.getName() + "\n" +
                    "https://maps.google.com/?q=" + Uri.encode("Bar " + location);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, message);

            Intent chooser = Intent.createChooser(intent, "Compartir bar amb...");
            getContext().startActivity(chooser);
        });

        recyclerView.setAdapter(mainAdapter);

        mainAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mainAdapter != null) {
            setMainAdapter(city, lastSearchQuery);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mainAdapter != null) {
            mainAdapter.stopListening();
        }

    }

    @Override
    public void onDestroyView() {
        if (mainAdapter != null) {
            mainAdapter.stopListening();
            recyclerView.setAdapter(null);
            mainAdapter = null;
        }

        super.onDestroyView();
    }
}