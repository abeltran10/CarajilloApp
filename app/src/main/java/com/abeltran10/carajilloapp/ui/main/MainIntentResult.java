package com.abeltran10.carajilloapp.ui.main;

import androidx.activity.result.IntentSenderRequest;
import androidx.annotation.Nullable;

public class MainIntentResult {

    private IntentSenderRequest success;
    @Nullable
    private String error;

    MainIntentResult(@Nullable String error) {
        this.error = error;
    }

    MainIntentResult(@Nullable IntentSenderRequest success) {
        this.success = success;
    }

    @Nullable
    public IntentSenderRequest getSuccess() {
        return success;
    }

    @Nullable
    public String getError() {
        return error;
    }
}
