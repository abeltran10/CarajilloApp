package com.abeltran10.carajilloapp.ui.main;

import com.abeltran10.carajilloapp.data.model.Bar;

public class MainView {

    private Bar bar;

    private Float rating;

    public MainView(Float rating, Bar bar) {
        this.bar = bar;
        this.rating = rating;
    }

    public Bar getBar() {
        return bar;
    }

    public void setBar(Bar bar) {
        this.bar = bar;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
