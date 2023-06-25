package com.example.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;


public class itemDBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "InventoryDB";

    private static final int DB_VERSION = 1;

    private static final String TABLE_NAME = "myInventory";

    private static final String ITEM_COL = "ITEM";

    private static final String LOCATION_COL = "location";

    private static final String QUANTITY_COL = "quantity";

    private static final String LAST_CHG_COL = "last_change";

    //constructor for database handler.
    public itemDBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String addQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + ITEM_COL + " TEXT,"
                + LOCATION_COL + " TEXT PRIMARY KEY,"
                + QUANTITY_COL + " TEXT,"
                + LAST_CHG_COL + " DATE)";

        db.execSQL(addQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNewItem(String item, String location, String quantity, String lastChange) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Check if the combination of item and location exists
        String itemLocationSelection = ITEM_COL + " = ? AND " + LOCATION_COL + " = ?";
        String[] itemLocationSelectionArgs = {item, location};

        // Combination of item and location does not exist, insert a new row
        ContentValues values = new ContentValues();
        values.put(ITEM_COL, item);
        values.put(LOCATION_COL, location);
        values.put(QUANTITY_COL, quantity);
        values.put(LAST_CHG_COL, lastChange);
        db.replace(TABLE_NAME, null, values);


        db.close();
    }

    public void removeItem(String item, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = ITEM_COL + " = ? AND " + LOCATION_COL + " = ?";
        String[] selectionArgs = {item, location};
        db.delete(TABLE_NAME, selection, selectionArgs);
        db.close();
    }
    public int getItemQuantity(String itemNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        int quantity = -1;

        String query = "SELECT SUM(" + QUANTITY_COL + ") AS totalQuantity FROM " + TABLE_NAME +
                " WHERE " + ITEM_COL + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{itemNumber});

        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("totalQuantity");
            if (columnIndex != -1) {
                quantity = cursor.getInt(columnIndex);
                if (quantity < 0) {
                    quantity = -1;
                }
            }
        }

        cursor.close();
        db.close();

        return quantity;
    }


//    public int getItemQuantity(String itemNumber) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        int quantity = -1;
//
//        String query = "SELECT SUM(" + QUANTITY_COL + ") AS totalQuantity FROM " + TABLE_NAME +
//                " WHERE " + ITEM_COL + " = ?";
//
//        Cursor cursor = db.rawQuery(query, new String[]{itemNumber});
//
//        if (cursor.moveToFirst()) {
//            int columnIndex = cursor.getColumnIndex("totalQuantity");
//            if (columnIndex != -1) {
//                quantity = cursor.getInt(columnIndex);
//                if (quantity < 0) {
//                    quantity = -1;
//                }
//            }
//        }
//
//        cursor.close();
//        db.close();
//
//        return quantity;
//    }

}


