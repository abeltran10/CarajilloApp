package com.abeltran10.carajilloapp.ui.bar;

import androidx.annotation.Nullable;

public class BarResult {

    @Nullable
    private BarView success;
    @Nullable
    private String error;

    BarResult(@Nullable String error) {
        this.error = error;
    }

    BarResult(@Nullable BarView success) {
        this.success = success;
    }

    @Nullable
    BarView getSuccess() {
        return success;
    }

    @Nullable
    String getError() {
        return error;
    }
}
