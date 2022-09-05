package com.daimler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {
    TextView vinTextView,descTextView;
    ImageView imageDisp;
    Button navButton,retButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        vinTextView=findViewById(R.id.vinTextView);
        descTextView=findViewById(R.id.descriptionTextView);
        imageDisp=findViewById(R.id.imageDisplay);
        navButton=findViewById(R.id.navButton);
        retButton=findViewById(R.id.retrieveButton);
        String s=getIntent().getStringExtra("VIN");
        vinTextView.setText(s);
        Log.d("DisplayActivity",s);
    }
}