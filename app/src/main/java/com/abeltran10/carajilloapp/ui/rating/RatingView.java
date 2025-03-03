package com.abeltran10.carajilloapp.ui.rating;

public class RatingView {

    private String barName;

    private Float rating;

    public RatingView(String barName, Float rating) {
        this.barName = barName;
        this.rating = rating;
    }

    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
