package com.abeltran10.carajilloapp.ui.bar;

import androidx.annotation.Nullable;

public class BarFormState {

    private boolean isDataValid;

    @Nullable
    private Integer error;

    public BarFormState(boolean isDataValid) {
        this.isDataValid = isDataValid;
    }

    public BarFormState(@Nullable Integer error) {
        this.error = error;
    }
    public boolean isDataValid() {
        return isDataValid;
    }
}
