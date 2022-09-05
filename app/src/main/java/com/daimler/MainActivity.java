package com.daimler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    EditText vinEditText,descEditText;
    Button searchButton,submitButton,scanButton,cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vinEditText=findViewById(R.id.vinEditText);
        descEditText=findViewById(R.id.descriptionEditText);
        searchButton=findViewById(R.id.searchButton);
        submitButton=findViewById(R.id.submitButton);
        cameraButton=findViewById(R.id.captureButton);
        scanButton=findViewById(R.id.scanQRButton);


        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}