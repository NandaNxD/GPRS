package com.daimler;

import android.media.Image;

public class Payload {
    String vin;
    double latitude;
    double longitude;
    byte[] image;
    String description;


    public Payload(String vin, double latitude, double longitude, Image image, String description) {
//        Bitmap yourBitmap;
//        ByteArrayOutputStream bos = new ByteArrayOutputStream();
//        yourBitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
//        byte[] bArray = bos.toByteArray();

        this.vin = vin;
        this.latitude = latitude;
        this.longitude = longitude;

        this.description = description;
    }
}
