package com.daimler;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {
    EditText searchEditText;
    Button search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchEditText=findViewById(R.id.searchEditText);
        search=findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(SearchActivity.this,InfoActivity.class);
                String vin=searchEditText.getText().toString();
                DataStore db= new DataStore(SearchActivity.this, DataStore.dbName, null, 1);
                it.putExtra("VIN",vin);
                Log.d("VIN",vin);
                Payload payload=db.search(vin);
                if(payload!=null){
                    searchEditText.setText("");
                    startActivity(it);
                }
                else{
                    Toast.makeText(getApplicationContext(),"VIN NOT PRESENT",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}