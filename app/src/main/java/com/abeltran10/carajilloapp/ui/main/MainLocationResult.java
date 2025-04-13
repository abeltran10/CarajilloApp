package com.abeltran10.carajilloapp.ui.main;

import androidx.annotation.Nullable;

import com.abeltran10.carajilloapp.data.model.Bar;

import java.util.List;

public class MainLocationResult {

    @Nullable
    private List<Bar> success;
    @Nullable
    private String error;

    MainLocationResult(@Nullable String error) {
        this.error = error;
    }

    MainLocationResult(@Nullable List<Bar> success) {
        this.success = success;
    }

    @Nullable
    public List<Bar> getSuccess() {
        return success;
    }

    @Nullable
    public String getError() {
        return error;
    }

}
