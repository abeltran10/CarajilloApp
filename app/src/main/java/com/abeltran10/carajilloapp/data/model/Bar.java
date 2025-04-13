package com.abeltran10.carajilloapp.data.model;

public class Bar {

    private String id;

    private String name;

    private String city;

    private String address;

    private Double latitude;

    private Double longitude;

    private String postalCode;

    private Float rating;

    private Long totalVotes;

    public Bar() {
    }

    public Bar(String id) {
        this.id = id;
    }

    public Bar(String id, String name, String city, Street address, String postalCode, Float rating, Long totalVotes) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address.getName();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
        this.postalCode = postalCode;
        this.rating = rating;
        this.totalVotes = totalVotes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setTotalVotes(Long totalVotes) {
        this.totalVotes = totalVotes;
    }

    public Long getTotalVotes() {
        return totalVotes;
    }
}
