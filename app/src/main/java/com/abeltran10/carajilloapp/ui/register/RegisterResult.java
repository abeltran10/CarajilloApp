package com.abeltran10.carajilloapp.ui.register;

import androidx.annotation.Nullable;

public class RegisterResult {
    @Nullable
    private RegisterView success;
    @Nullable
    private Integer error;

    RegisterResult(RegisterView success) {
        this.success = success;
    }

    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    @Nullable
    RegisterView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
