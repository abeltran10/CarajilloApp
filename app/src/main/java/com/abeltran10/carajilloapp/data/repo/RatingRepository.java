package com.abeltran10.carajilloapp.data.repo;

public class RatingRepository {

    private static volatile RatingRepository instance;

    public static RatingRepository getInstance() {
        if (instance == null) {
            instance = new RatingRepository();
        }
        return instance;
    }
}
