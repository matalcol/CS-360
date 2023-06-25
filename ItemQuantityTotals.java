package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ItemQuantityTotals extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_quantity_totals);


        TableLayout tableLayout = findViewById(R.id.inventoryQuantityTableLayout);


        try {
            // Open or create SQLite database
            SQLiteDatabase database = openOrCreateDatabase("InventoryDB", MODE_PRIVATE, null);

            // Execute a query to retrieve the total quantity of each unique item number
            String query = "SELECT ITEM, SUM(quantity) AS totalQuantity FROM myInventory GROUP BY ITEM";
            Cursor cursor = database.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String item = cursor.getString(cursor.getColumnIndexOrThrow("ITEM"));
                    int totalQuantity = cursor.getInt(cursor.getColumnIndexOrThrow("totalQuantity"));

                    TableRow newRow = new TableRow(this);

                    TextView itemTextView = new TextView(this);
                    itemTextView.setText(item);
                    newRow.addView(itemTextView);

                    TextView quantityTextView = new TextView(this);
                    quantityTextView.setText(String.valueOf(totalQuantity));
                    newRow.addView(quantityTextView);

                    // Set the gravity for each TextView
                    itemTextView.setGravity(Gravity.CENTER);
                    quantityTextView.setGravity(Gravity.CENTER);

                    tableLayout.addView(newRow);
                } while (cursor.moveToNext());
            }


            // Close the cursor and database
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void gotoMainMenu(View view) {
        Intent intent = new Intent(this, Main_MenuActivity.class);
        startActivity(intent);
    }

    public void gotoAddRemoveItem(View view) {
        Intent intent = new Intent(this, AddRemoveItemActivity.class);
        startActivity(intent);
    }

}
