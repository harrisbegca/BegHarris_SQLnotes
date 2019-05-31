package com.example.sqlnotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Contact2019.db";
    public static final String TABLE_NAME = "Contact20192";
    public static final int DATABASE_VERSION = 1;
    public static final String ID = "ID";
    public static final String COLUMN_NAME_CONTACT = "contact";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ADDRESS = "address";
    private static final String TAG = "DATABASE";
    public static final String DESTROY_DATABASE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NAME_CONTACT + " TEXT, " + COLUMN_ADDRESS + " TEXT, " + COLUMN_PHONE + " TEXT )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db =this.getWritableDatabase();
        Log.d(TAG, "DatabaseHelper: CONSTRUCTED");
+    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "DatabaseHelper: CREATED");
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "DatabaseHelper: UPGRADED");
        db.execSQL(DESTROY_DATABASE);
        onCreate(db);
    }

    public boolean insertData(String name, String address, String phone) {
        Log.d(TAG, "DatabaseHelper: INSERTED " + name);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_CONTACT, name);
        contentValues.put(COLUMN_ADDRESS, address);
        contentValues.put(COLUMN_PHONE, phone);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            Log.d(TAG, "DatabaseHelper: FAILED TO INSERT " + name);
            return false;
        }
        Log.d(TAG, "DatabaseHelper: SUCCEEDED INSERTING " + name);
        return true;
    }

    public Cursor getAllData() {
        Log.d(TAG, "DatabaseHelper: GETTING DATA");
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);
        return result;

    }
}
