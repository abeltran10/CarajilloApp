package com.abeltran10.carajilloapp.ui.bar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.abeltran10.carajilloapp.data.model.Street;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.service.LocationService;
import com.abeltran10.carajilloapp.data.service.impl.LocationServiceImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BarViewModel extends ViewModel {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private MutableLiveData<BarResult> barResult = new MutableLiveData<>();
    private BarRepository barRepository;

    private LocationService locationService = new LocationServiceImpl();
    private MutableLiveData<BarFormState> barFormState = new MutableLiveData<>();

    BarViewModel(BarRepository barRepository) {
        this.barRepository = barRepository;
    }

    public LiveData<BarResult> getBarResult() {
        return barResult;
    }

    public MutableLiveData<BarFormState> getBarFormState() {
        return barFormState;
    }

    public void create(String name, String address, City city, String postalCode) {
        executorService.execute(() -> {

            Result locationResult = locationService.getStreet(address, postalCode, city.getName());
            if (locationResult instanceof Result.Success) {
                Street street = ((Result.Success<Street>)locationResult).getData();

                if (street != null) {
                    Result repositoryResult = barRepository.createBar(name, street, city, postalCode);
                    if (repositoryResult instanceof Result.Success) {
                        Bar data = ((Result.Success<Bar>) repositoryResult).getData();
                        barResult.postValue(new BarResult(new BarView(data.getName(), data.getAddress(),
                                data.getCity(), data.getPostalCode())));
                    } else {
                        barResult.postValue(new BarResult(((Result.Error)repositoryResult).getError().getMessage()));
                    }
                } else {
                    barResult.postValue(new BarResult("L'adreça no existeix"));
                }
            } else {
                barResult.postValue(new BarResult(((Result.Error)locationResult).getError().getMessage()));
            }

        });

    }

    public void barDataChanged(String name, String address, String city, String postalCode) {
        if (isNameValid(name) && isAddressValid(address) && isCityValid(city)
                && isPostalCodeValid(postalCode)) {
            barFormState.setValue(new BarFormState(true));
        } else if (!isAddressValid(address)) {
            barFormState.setValue(new BarFormState(null, R.string.invalid_address, null));
        }else if (!isPostalCodeValid(postalCode)) {
            barFormState.setValue(new BarFormState(null, null, R.string.invalid_postal_code));
        }
    }

    private boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private boolean isAddressValid(String address) {
        return address != null && !address.trim().isEmpty() && address.matches("^[^0-9]+$");
    }

    private boolean isCityValid(String city) {
        return city != null && !city.trim().isEmpty();
    }

    private boolean isPostalCodeValid(String postalCode) {
        return postalCode != null && !postalCode.trim().isEmpty() && postalCode.matches("[0-9]*");
    }
}
