package com.abeltran10.carajilloapp.ui.bar;

import androidx.annotation.Nullable;

public class BarFormState {

    private boolean isDataValid;

    @Nullable
    private Integer nameError;

    @Nullable
    private Integer addressError;

    @Nullable
    private Integer postalCodeError;


    public BarFormState(boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    public BarFormState(@Nullable Integer nameError, Integer addressError, Integer postalCodeError) {
        this.nameError = nameError;
        this.addressError = addressError;
        this.postalCodeError = postalCodeError;
    }
    public boolean isDataValid() {
        return isDataValid;
    }

    @Nullable
    public Integer getNameError() {
        return nameError;
    }

    @Nullable
    public Integer getAddressError() {
        return addressError;
    }

    @Nullable
    public Integer getPostalCodeError() {
        return postalCodeError;
    }
}
