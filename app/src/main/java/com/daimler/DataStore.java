package com.daimler;

import android.content.Context;
import android.database.Cursor;
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
