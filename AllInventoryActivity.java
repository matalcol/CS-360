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

public class AllInventoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_inventory);

        TableLayout tableLayout = findViewById(R.id.inventoryTableLayout);


        try {
            // Open or create SQLite database
            SQLiteDatabase database = openOrCreateDatabase("InventoryDB", MODE_PRIVATE, null);

            // Execute a query to retrieve the data
            String query = "SELECT * FROM myInventory ORDER BY LOCATION";
            Cursor cursor = database.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String item = cursor.getString(cursor.getColumnIndexOrThrow("ITEM"));
                    String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                    String quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
                    String lastChange = cursor.getString(cursor.getColumnIndexOrThrow("last_change"));

                    TableRow newRow = new TableRow(this);

                    TextView itemTextView = new TextView(this);
                    itemTextView.setText(item);
                    newRow.addView(itemTextView);

                    TextView locationTextView = new TextView(this);
                    locationTextView.setText(location);
                    newRow.addView(locationTextView);

                    TextView quantityTextView = new TextView(this);
                    quantityTextView.setText(quantity);
                    newRow.addView(quantityTextView);

                    TextView lastChangeTextView = new TextView(this);
                    lastChangeTextView.setText(lastChange);
                    newRow.addView(lastChangeTextView);

                    // Set the gravity for each TextView
                    itemTextView.setGravity(Gravity.CENTER);
                    locationTextView.setGravity(Gravity.CENTER);
                    quantityTextView.setGravity(Gravity.CENTER);
                    lastChangeTextView.setGravity(Gravity.CENTER);


                    tableLayout.addView(newRow);
                } while (cursor.moveToNext());
            }

            // Close the cursor and database
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retrieve the accessLevel value from the shared preference
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userAccessLevel = preferences.getInt("accessLevel", 0);

        // Show/hide the admin button based on the user's access level
        Button adminButton = findViewById(R.id.addRemoveItem);
        if (userAccessLevel >= 1) {
            adminButton.setVisibility(View.VISIBLE);
        } else {
            adminButton.setVisibility(View.GONE);
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
