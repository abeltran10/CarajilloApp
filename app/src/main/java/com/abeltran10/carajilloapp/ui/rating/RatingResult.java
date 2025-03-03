package com.abeltran10.carajilloapp.ui.rating;

import androidx.annotation.Nullable;

public class RatingResult {

    @Nullable
    private RatingView success;
    @Nullable
    private String error;

    RatingResult(@Nullable String error) {
        this.error = error;
    }

    RatingResult(@Nullable RatingView success) {
        this.success = success;
    }

    @Nullable
    public RatingView getSuccess() {
        return success;
    }

    @Nullable
    public String getError() {
        return error;
    }
}
