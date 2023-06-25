package com.example.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class userDBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "userDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "myUsers";
    private static final String USER_ID = "userID";
    private static final String NAME = "name";
    private static final String PH_NUMBER = "phNumber";
    private static final String ACCESS_LEVEL = "accessLevel";
    private static final String PASSWORD = "password";

    public userDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + USER_ID + " TEXT PRIMARY KEY,"
                + NAME + " TEXT,"
                + PH_NUMBER + " TEXT,"
                + ACCESS_LEVEL + " TEXT,"
                + PASSWORD + " TEXT)";

        db.execSQL(query);
    }

    public void addNewUser(String userID, String name, String phNumber, String accessLevel, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USER_ID, userID);
        values.put(NAME, name);
        values.put(PH_NUMBER, phNumber);
        values.put(ACCESS_LEVEL, accessLevel);
        values.put(PASSWORD, password);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void removeUser(String userID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = USER_ID + " = ?";
        String[] selectionArgs = { userID };
        db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean checkAdminExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean adminExists = false;

        // Query the database to check if an admin user exists
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ACCESS_LEVEL + " > 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            adminExists = true;
        }

        cursor.close();
        db.close();

        return adminExists;
    }

    public boolean checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database to check if the username and password combination exists
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USER_ID + " = ? AND " + PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        boolean loginSuccessful = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return loginSuccessful;
    }
    public int checkAccessLevel(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        int accessLevel = -1;

        // Query the database to check if the username and password combination exists
        String query = "SELECT " + ACCESS_LEVEL + " FROM " + TABLE_NAME + " WHERE " + USER_ID + " = ? AND " + PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            int accessLevelIndex = cursor.getColumnIndex(ACCESS_LEVEL);
            if (accessLevelIndex != -1) {
                accessLevel = cursor.getInt(accessLevelIndex);
            }
        }

        cursor.close();
        db.close();

        return accessLevel;
    }
    public String getPhoneNumber(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String phoneNumber = null;

        // Query the database to check if the username and password combination exists
        String query = "SELECT " + PH_NUMBER + " FROM " + TABLE_NAME + " WHERE " + USER_ID + " = ? AND " + PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor.moveToFirst()) {
            int phoneNumberIndex = cursor.getColumnIndex(PH_NUMBER);
            if (phoneNumberIndex != -1) {
                phoneNumber = cursor.getString(phoneNumberIndex);
            } else {
                // Handle case where column index is -1 (column not found)
                phoneNumber = "1234567890";
            }
        }

        cursor.close();
        db.close();

        return phoneNumber;
    }




}
