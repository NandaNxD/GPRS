package com.daimler;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity{

    private static final int pic_id = 123;

    EditText vinEditText, descEditText;
    Button searchButton, submitButton, scanButton, cameraButton;
    DataStore db;
    Payload payload;
    ImageView imageView;
    FusedLocationProviderClient fusedLocationClient;


    public void clear(){
        vinEditText.setText("");
        descEditText.setText("");
        imageView.setVisibility(View.INVISIBLE);
    }

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
        payload = new Payload();

        db = new DataStore(this, DataStore.dbName, null, 1);

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // start qr scan
                IntentIntegrator intentIntegrator = new IntentIntegrator(MainActivity.this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(false);

                intentIntegrator.initiateScan();
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

                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                // Add Checks

                if(vinEditText.getText().toString().isEmpty() || descEditText.getText().toString().isEmpty() || imageView.getVisibility()==View.INVISIBLE){
                    Toast.makeText(getApplicationContext(),"Fields Empty",Toast.LENGTH_LONG).show();
                    return;
                }
                fusedLocationClient.getLastLocation().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        vinEditText.setText("Error Adding VIN");
                    }
                });

                fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                          double   lat = location.getLatitude();
                          double   lon = location.getLongitude();
                            Log.d("Latitide", "" + lat);
                            payload.latitude = lat;
                            payload.longitude = lon;
                            payload.image = Util.getBytes(((BitmapDrawable) imageView.getDrawable()).getBitmap());
                            db.insert(payload);

                            Log.d("LCX", "Hello");

                            Log.d("IMGX", "" + payload.image.length);

                        }
                        else{
                            Log.d("NULLLOC","NULL");
                        }

                    }
                });
                String p=payload.vin;

                Toast.makeText(getApplicationContext(),"VIM ADDED",Toast.LENGTH_LONG).show();
                clear();
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
            imageView.setVisibility(View.VISIBLE);
            // Set the image in imageview for display
            imageView.setImageBitmap(photo);
        }
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                vinEditText.setText(intentResult.getContents());
//                messageFormat.setText(intentResult.getFormatName());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }


}
