package com.daimler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DataStore extends SQLiteOpenHelper {
    static String tableName="VEHICLEINFO";
    static String dbName="DAIMLER";
    static String columns[]={"VIN","LATITUDE","LONGITUDE","IMAGEBLOB","DESCRIPTION"};

    public DataStore(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE VEHICLEINFO(VIN TEXT PRIMARY KEY NOT NULL,LATITUDE REAL,LONGITUDE REAL,IMAGEBLOB BLOB,DESCRIPTION TEXT)");
    }

    public void insert(Payload payload){
        ContentValues cv=new ContentValues();
        cv.put(columns[0],payload.vin);
        cv.put(columns[1],payload.latitude);
        cv.put(columns[2],payload.longitude);
        if(payload.image==null){
            Log.d("IMG ERROR","insert error");
            return;
        }
        cv.put(columns[3],payload.image);
        cv.put(columns[4],payload.description);
        SQLiteDatabase db=getWritableDatabase();
        db.insert(tableName,null,cv);
    }

    public Payload search(String vin){
        SQLiteDatabase db=getReadableDatabase();
        String query="SELECT * FROM VEHICLEINFO WHERE VIN="+vin;
        Cursor cu=db.rawQuery(query,null);
        if(cu.getCount()==0){
            return null;
        }
        else {
            Payload p=new Payload(cu.getString(0),cu.getDouble(1),cu.getDouble(2),cu.getBlob(3),cu.getString(4));
            cu.close();
            return p;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }
}
