package com.daimler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        String vin=getIntent().getStringExtra("VIN");
        DataStore db= new DataStore(InfoActivity.this, DataStore.dbName, null, 1);
        Payload payload=db.search(vin);
        imageDisp.setImageBitmap(Util.getImage(payload.image));
        vinTextView.setText(payload.vin);
        descTextView.setText(payload.description);

        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String la=String.valueOf(payload.latitude);
                String lon=String.valueOf(payload.longitude);
                String address=la+","+lon;
                String url = "https://www.google.com/maps/search/?api=1&query="+address;
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        retButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.delete(vin);
                Toast.makeText(getApplicationContext(),"Vehicle Retrieved",Toast.LENGTH_LONG).show();
                Intent it=new Intent(InfoActivity.this,SearchActivity.class);
                startActivity(it);
            }
        });


    }
}