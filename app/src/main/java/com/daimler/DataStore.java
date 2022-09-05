package com.daimler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataStore extends SQLiteOpenHelper {
    static String tableName="VEHICLEINFO";
    static String dbName="DAIMLER";
    static String columns[]={"VIN","LATITUDE","LONGITUDE","IMAGEBLOB","DESCRIPTION"};

    public DataStore(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE VEHICLEINFO(VIN TEXT PRIMARY KEY NOT NULL,LATITUDE TEXT,LONGITUDE TEXT,IMAGEBLOB BLOB,DESCRIPTION TEXT)");
    }



    public void search(String vin){
        SQLiteDatabase db=getReadableDatabase();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
    }
}
