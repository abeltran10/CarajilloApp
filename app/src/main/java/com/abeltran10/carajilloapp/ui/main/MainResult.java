package com.abeltran10.carajilloapp.ui.main;

import androidx.annotation.Nullable;

public class MainResult {

    @Nullable
    private MainView success;
    @Nullable
    private String error;

    MainResult(@Nullable String error) {
        this.error = error;
    }

    MainResult(@Nullable MainView success) {
        this.success = success;
    }

    @Nullable
    public MainView getSuccess() {
        return success;
    }

    @Nullable
    public String getError() {
        return error;
    }
}
