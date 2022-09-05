package com.daimler;


public class Payload {
    String vin;
    double latitude;
    double longitude;
    byte[] image;
    String description;

    public Payload(){

    }

    public Payload(String vin, double latitude, double longitude, byte[] image, String description) {
        this.vin = vin;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.description = description;
    }
}
