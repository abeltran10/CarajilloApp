package com.abeltran10.carajilloapp.data;

public interface LocationCallback<T> {
    void onComplete(Result<T> result);
}
