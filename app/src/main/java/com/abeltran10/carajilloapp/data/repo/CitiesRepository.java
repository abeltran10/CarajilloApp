package com.abeltran10.carajilloapp.data.repo;

public class CitiesRepository {

    private static CitiesRepository instance;

    public static CitiesRepository getInstance() {
        if (instance == null) {
            instance = new CitiesRepository();
        }
        return instance;
    }
}
