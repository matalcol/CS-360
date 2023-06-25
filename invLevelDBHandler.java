package com.example.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class invLevelDBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "invLevelDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "invLevel";

    private static final String LEVEL = "InvLevel";

    public invLevelDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + LEVEL + " TEXT)";

        db.execSQL(query);
    }

    public void updateLevel(String newLevel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LEVEL, newLevel);

        // Check if the table has records
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            // The table has records, update the existing record
            cursor.moveToFirst();
            String rowId = cursor.getString(cursor.getColumnIndexOrThrow(LEVEL));
            String selection = LEVEL + " = ?";
            String[] selectionArgs = {rowId};
            db.update(TABLE_NAME, values, selection, selectionArgs);
        } else {
            // The table is empty, insert a new record
            db.insert(TABLE_NAME, null, values);
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public int getCurrentLevel() {
        int currentLevel = 10; // Default value of 10

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + LEVEL + " FROM " + TABLE_NAME, null);

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(LEVEL);
            if (columnIndex != -1) {
                currentLevel = cursor.getInt(columnIndex);
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return currentLevel;
    }
}
