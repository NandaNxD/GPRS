package com.daimler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    private static final int pic_id = 123;

    EditText vinEditText, descEditText;
    Button searchButton, submitButton, scanButton, cameraButton;
    DataStore db;
    Payload payload;
    ImageView imageView;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vinEditText = findViewById(R.id.vinEditText);
        descEditText = findViewById(R.id.descriptionEditText);
        searchButton = findViewById(R.id.searchButton);
        submitButton = findViewById(R.id.submitButton);
        cameraButton = findViewById(R.id.captureButton);
        imageView = findViewById(R.id.imageDisplayMain);
        scanButton = findViewById(R.id.scanQRButton);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        payload=new Payload();

        db = new DataStore(this, DataStore.dbName, null, 1);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start qr scan
            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // click picture from camera
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Start the activity with camera_intent, and request pic id
                startActivityForResult(camera_intent, pic_id);
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payload.vin = vinEditText.getText().toString();
                payload.description = descEditText.getText().toString();

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },100);
                }

                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        double lat=location.getLatitude();
                        double lon=location.getLongitude();
                        Log.d("Latitide",""+lat);
                        payload.latitude=lat;
                        payload.longitude=lon;
                        payload.vin = vinEditText.getText().toString();
                        payload.description = descEditText.getText().toString();
                        Log.d("LCX","Hello") ;

                        payload.image=Util.getBytes(((BitmapDrawable)imageView.getDrawable()).getBitmap());
                        Log.d("IMGX",""+payload.image.length);
                        db.insert(payload);
                    }
                });
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(it);
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == pic_id && resultCode == RESULT_OK) {
            // BitMap is data structure of image file which store the image in memory
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            imageView.setImageBitmap(photo);
        }
    }
}
