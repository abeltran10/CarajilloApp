package com.abeltran10.carajilloapp.ui.register;

import androidx.annotation.Nullable;

public class RegisterFormState {

    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer repeatpasswordError;
    @Nullable
    private Integer emailError;
    private boolean isDataValid;

    RegisterFormState(@Nullable Integer usernameError, @Nullable Integer emailError, @Nullable Integer passwordError, @Nullable Integer repeatpasswordError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.repeatpasswordError = repeatpasswordError;
        this.emailError = emailError;
        this.isDataValid = false;
    }

    RegisterFormState(boolean isDataValid) {
        this.usernameError = null;
        this.passwordError = null;
        this.repeatpasswordError = null;
        this.emailError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getUsernameError() {
        return usernameError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getRepeatpasswordError() {
        return repeatpasswordError;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    boolean isDataValid() {
        return isDataValid;
    }
}
