package com.abeltran10.carajilloapp.ui.bar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.R;
import com.abeltran10.carajilloapp.data.Result;
import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.repo.BarRepository;
import com.abeltran10.carajilloapp.data.service.LocationService;
import com.abeltran10.carajilloapp.data.service.LocationServiceImpl;

public class BarViewModel extends ViewModel {

    private MutableLiveData<BarResult> barResult = new MutableLiveData<>();
    private BarRepository barRepository;

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

    public void create(String name, String address, String number, String city, String postalCode) {
        boolean addressExists = false;
        LocationService location = new LocationServiceImpl();

        try {
            addressExists = location.addressExists(address, number, postalCode, city);

            if (!addressExists) {
                barFormState.setValue(new BarFormState(R.string.address_not_exists));
            } else {
                barRepository.asyncCreateBar(name, address, number, city, postalCode, result -> {
                    if (result instanceof Result.Success) {
                        Bar data = ((Result.Success<Bar>) result).getData();
                        barResult.postValue(new BarResult(new BarView(data.getName(), data.getAddress(),
                                data.getCity(), data.getPostalCode())));
                    } else {
                        barResult.postValue(new BarResult(((Result.Error)result).getError().getMessage()));
                    }
                });
            }
        } catch (Exception e) {
            barFormState.setValue(new BarFormState(R.string.address_validate_error));
        }
    }

    public void barDataChanged(String name, String address, String number, String city, String postalCode) {
        if (isNameValid(name) && isAddressValid(address) && isNumberValid(number) && isCityValid(city)
                && isPostalCodeValid(postalCode)) {
            barFormState.setValue(new BarFormState(true));
        } else if (!isPostalCodeValid(postalCode)) {
            barFormState.setValue(new BarFormState(R.string.invalid_postal_code));
        } else if (!isNumberValid(number)) {
            barFormState.setValue(new BarFormState(R.string.invalid_number));
        }
    }

    private boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    private boolean isAddressValid(String address) {
        return address != null && !address.trim().isEmpty();
    }

    private boolean isNumberValid(String number) {
        return number != null && !number.trim().isEmpty() && number.matches("[0-9]*");
    }

    private boolean isCityValid(String city) {
        return city != null && !city.trim().isEmpty();
    }

    private boolean isPostalCodeValid(String postalCode) {
        return postalCode != null && !postalCode.trim().isEmpty() && postalCode.matches("[0-9]*");
    }
}
