package com.abeltran10.carajilloapp.ui.location;

import androidx.lifecycle.ViewModel;

import com.abeltran10.carajilloapp.data.model.Bar;
import com.abeltran10.carajilloapp.data.model.City;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class LocationViewModel extends ViewModel {

    private FirebaseFirestore bd = FirebaseFirestore.getInstance();

    public Query searchBars(City city, List<String> barsIdList) {
        Query query = bd.collection("bars").whereEqualTo("city", city.getId());
        query = query.whereIn("id", barsIdList);
        query.orderBy("name", Query.Direction.ASCENDING);

        return query;
    }
}
