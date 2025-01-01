package com.abeltran10.carajilloapp.ui.bar;

public class BarView {

    private String name;

    private String address;

    private String city;

    private String postalCode;

    public BarView(String name, String address, String city, String postalCode) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }
}
