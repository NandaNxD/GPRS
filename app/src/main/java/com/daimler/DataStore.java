package com.daimler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataStore extends SQLiteOpenHelper {
    static String tableName="VEHICLEINFO";
    static String dbName="DAIM";
    static String columns[]={"VIN","LATITUDE","LONGITUDE","IMAGEBLOB","DESCRIPTION"};

    public DataStore(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dbName, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE VEHICLEINFO(VIN TEXT PRIMARY KEY NOT NULL,LATITUDE REAL,LONGITUDE REAL,IMAGEBLOB BLOB,DESCRIPTION TEXT)");
    }

    public void insert(Payload payload){
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS VEHICLEINFO(VIN TEXT PRIMARY KEY NOT NULL,LATITUDE REAL,LONGITUDE REAL,IMAGEBLOB BLOB,DESCRIPTION TEXT)");

        ContentValues cv=new ContentValues();
        cv.put(columns[0],payload.vin);
        cv.put(columns[1],payload.latitude);
        cv.put(columns[2],payload.longitude);
        Log.d("ARRBYTE", String.valueOf(payload.image.length));
        if(payload.image==null){
            Log.d("IMG ERROR","insert error");
            return;
        }
        cv.put(columns[3],payload.image);
        cv.put(columns[4],payload.description);
        SQLiteDatabase db=getWritableDatabase();
        String v=payload.vin;
        //db.update(tableName,cv);
        db.insert(tableName,null,cv);
    }

    public Payload search(String vin){
//        getWritableDatabase().execSQL("DROP TABLE IF EXISTS " + tableName);
//        return null;
        SQLiteDatabase db=getReadableDatabase();
        String query="SELECT * FROM VEHICLEINFO WHERE VIN=\'"+ vin+"\'";
        Log.d("Payload Search",query);

        Cursor cu=db.rawQuery(query,null);
        //db.close();
        if(cu==null){
            return null;
        }
        int count=cu.getCount();
        if(cu.getCount()>0){
            cu.moveToFirst();
            Payload p=new Payload(cu.getString(0),cu.getDouble(1),cu.getDouble(2),cu.getBlob(3),cu.getString(4));
            cu.close();
            return p;
        }
        else {
            return null;
        }
    }
    public void delete(String vin){
        SQLiteDatabase db=getWritableDatabase();
        db.execSQL("DELETE FROM VEHICLEINFO WHERE VIN="+"\'"+vin+"\'");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);

    }
}
