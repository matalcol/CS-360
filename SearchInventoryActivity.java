package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class SearchInventoryActivity extends AppCompatActivity {

    private EditText searchItem;
    private EditText searchLocation;
    private EditText searchLastChanged;
    private TableLayout tableLayout;
    private boolean headersAdded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_inventory);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        tableLayout = findViewById(R.id.inventoryTableLayout);
        searchItem = findViewById(R.id.enterItem);
        searchLocation = findViewById(R.id.enterLocation);
        searchLastChanged = findViewById(R.id.enterLastChange);

        Button searchButton = findViewById(R.id.Search);

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
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchInventory();
            }
        });
    }

    public void searchInventory() {
        int currentVisibility = tableLayout.getVisibility();
        if (currentVisibility == View.GONE) {
            tableLayout.setVisibility(View.VISIBLE);
        }

        try {
            // Open or create SQLite database
            SQLiteDatabase database = openOrCreateDatabase("InventoryDB", MODE_PRIVATE, null);

            // Get the search values
            String item = searchItem.getText().toString();
            String location = searchLocation.getText().toString();
            String lastChanged = searchLastChanged.getText().toString();

            // Execute a query to retrieve the data
            String query = "SELECT * FROM myInventory WHERE ITEM = '" + item + "' OR location = '" + location + "' OR last_change = '" + lastChanged + "'";

            Cursor cursor = database.rawQuery(query, null);

            // Remove all rows except the header row
            tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String itemText = cursor.getString(cursor.getColumnIndexOrThrow("ITEM"));
                    String locationText = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                    String quantity = cursor.getString(cursor.getColumnIndexOrThrow("quantity"));
                    String lastChange = cursor.getString(cursor.getColumnIndexOrThrow("last_change"));

                    TableRow newRow = new TableRow(this);

                    TextView itemTextView = new TextView(this);
                    itemTextView.setText(itemText);
                    itemTextView.setGravity(Gravity.CENTER);
                    newRow.addView(itemTextView);

                    TextView locationTextView = new TextView(this);
                    locationTextView.setText(locationText);
                    locationTextView.setGravity(Gravity.CENTER);
                    newRow.addView(locationTextView);

                    TextView quantityTextView = new TextView(this);
                    quantityTextView.setText(quantity);
                    quantityTextView.setGravity(Gravity.CENTER);
                    newRow.addView(quantityTextView);

                    TextView lastChangeTextView = new TextView(this);
                    lastChangeTextView.setText(lastChange);
                    lastChangeTextView.setGravity(Gravity.CENTER);
                    newRow.addView(lastChangeTextView);

                    tableLayout.addView(newRow);
                } while (cursor.moveToNext());
            } else {
                // No results found, display a message
                TextView noResultsTextView = new TextView(this);
                noResultsTextView.setText("No results found.");
                noResultsTextView.setGravity(Gravity.CENTER);
                tableLayout.addView(noResultsTextView);
            }

            // Close the cursor and database
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //clear search criteria inputs
        searchItem.setText("");
        searchLocation.setText("");
        searchLastChanged.setText("");

        //set input focus back to item
        searchItem.requestFocus();

    }

    public void gotoMainMenu(View view) {
        Intent intent = new Intent(this, Main_MenuActivity.class);
        startActivity(intent);
    }

    public void gotoAddRemoveItem(View view) {
        Intent intent = new Intent(this, AddRemoveItemActivity.class);
        startActivity(intent);
    }

    public void showTable(View view) {
        TableLayout tableLayout = findViewById(R.id.inventoryTableLayout);
        TextView searchResultsTextView = findViewById(R.id.SearchResults);

        if (tableLayout.getVisibility() == View.GONE) {
            tableLayout.setVisibility(View.VISIBLE);
        }
        if (searchResultsTextView.getVisibility() == View.GONE){
            searchResultsTextView.setVisibility(View.VISIBLE);
        }
        searchInventory();
    }
}
