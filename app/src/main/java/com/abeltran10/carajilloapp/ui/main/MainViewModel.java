package com.abeltran10.carajilloapp.ui.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.activity.result.IntentSenderRequest;
import androidx.core.app.ActivityCompat;
import androidx.core.view.MenuProvider;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.Callback;
import com.abeltran10.carajilloapp.data.EventWrapper;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.repo.CitiesRepository;
import com.abeltran10.carajilloapp.data.repo.RatingRepository;
import com.abeltran10.carajilloapp.data.service.FusedLocationService;
import com.abeltran10.carajilloapp.data.service.impl.FusedLocationServiceImpl;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainViewModel extends ViewModel {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private BarRepository barRepository;

    private RatingRepository ratingRepository;

    private CitiesRepository citiesRepository;

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    private MutableLiveData<EventWrapper<MainResult>> mainResult = new MutableLiveData<>();

    private MutableLiveData<EventWrapper<MainLocationResult>> mainLocationResult = new MutableLiveData<>();

    private MutableLiveData<EventWrapper<MainIntentResult>> mainGPSResult = new MutableLiveData<>();

    private FusedLocationService fusedLocationService = new FusedLocationServiceImpl();

    public MainViewModel(BarRepository barRepository, RatingRepository ratingRepository, CitiesRepository citiesRepository) {
        this.barRepository = barRepository;
        this.ratingRepository = ratingRepository;
        this.citiesRepository = citiesRepository;
    }

    public MutableLiveData<EventWrapper<MainResult>> getMainResult() {
        return mainResult;
    }

    public MutableLiveData<EventWrapper<MainLocationResult>> getMainLocationResult() {
        return mainLocationResult;
    }

    public MutableLiveData<EventWrapper<MainIntentResult>> getMainGPSResult() {
        return mainGPSResult;
    }

    public Query searchBars(City city, String searchText, List<Bar> barList) {
        Query query;

        if (barList != null) {
            List<String> barIds = new ArrayList<>();
            query = bd.collection("bars").whereEqualTo("city", city.getId());
            for(Bar bar : barList) {
                barIds.add(bar.getId());
            }

            query = query.whereIn("id", barIds);
            query.orderBy("name", Query.Direction.ASCENDING);

        } else {
            if (searchText.isEmpty())
                query = bd.collection("bars")
                        .whereEqualTo("city", city.getId())
                        .orderBy("name", Query.Direction.ASCENDING);
            else
                query = bd.collection("bars")
                        .whereEqualTo("city", city.getId())
                        .orderBy("name", Query.Direction.ASCENDING)
                        .startAt(searchText.toUpperCase())
                        .endAt(searchText.toUpperCase() + "\uf8ff");
        }

        return query;
    }

    public void vote(Float newRating, Bar bar) {
        bd.runTransaction((transaction) -> {
            Float oldTotal = bar.getRating() * bar.getTotalVotes();
            Float averageResult = (oldTotal + newRating) / (bar.getTotalVotes() + 1);
            try {
                ratingRepository.vote(transaction, newRating, bar.getId());
                barRepository.updateBar(transaction, averageResult, bar.getId());

            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }

            return null;
        }).addOnSuccessListener(aVoid -> {
            mainResult.postValue(new EventWrapper<>(new MainResult(new MainView(newRating, bar))));
        }).addOnFailureListener(e -> {
            mainResult.postValue(new EventWrapper<>(new MainResult(e.getMessage())));
        });

    }

    public void loadCurrentLocation(Context context, City city) {
            currentLocation(context, city);
    }

    private void currentLocation(Context context, City city) {
        fusedLocationService.getCurrentLocation(context, locationResult -> {
            if (locationResult instanceof Result.Success) {
                barRepository.getNearBars(((Result.Success<Location>) locationResult).getData(), city.getId(), searchResult -> {
                    if (searchResult instanceof Result.Success) {
                        List<Bar> listBars = (List<Bar>) ((Result.Success<?>) searchResult).getData();
                        MainLocationResult mainLocation = new MainLocationResult(listBars);
                        mainLocationResult.postValue(new EventWrapper<>(mainLocation));
                    } else {
                        String error = ((Result.Error) searchResult).getError().getMessage();
                        MainLocationResult mainLocation = new MainLocationResult(error);
                        mainLocationResult.postValue(new EventWrapper<>(mainLocation));
                    }

                });
            } else {
                String error = ((Result.Error) locationResult).getError().getMessage();
                MainLocationResult mainLocation = new MainLocationResult(error);
                mainLocationResult.postValue(new EventWrapper<>(mainLocation));
            }
        }, resolution -> {
            if (resolution instanceof Result.Success) {
                IntentSenderRequest intentSenderRequest = (IntentSenderRequest) ((Result.Success<?>) resolution).getData();
                MainIntentResult mainIntentResult = new MainIntentResult(intentSenderRequest);
                mainGPSResult.postValue(new EventWrapper<>(mainIntentResult));
            } else {
                String error = ((Result.Error) resolution).getError().getMessage();
                MainIntentResult mainIntentResult = new MainIntentResult(error);
                mainGPSResult.postValue(new EventWrapper<>(mainIntentResult));
            }
        });
    }
}