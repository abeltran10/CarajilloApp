package com.abeltran10.carajilloapp.data;

public interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}
