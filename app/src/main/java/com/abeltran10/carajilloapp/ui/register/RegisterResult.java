package com.abeltran10.carajilloapp.ui.register;

import androidx.annotation.Nullable;

public class RegisterResult {
    @Nullable
    private RegisterView success;
    @Nullable
    private String error;

    RegisterResult(RegisterView success) {
        this.success = success;
    }

    RegisterResult(@Nullable String error) {
        this.error = error;
    }

    @Nullable
    RegisterView getSuccess() {
        return success;
    }

    @Nullable
    String getError() {
        return error;
    }
}
