package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddRemoveItemActivity extends AppCompatActivity {

    private itemDBHandler itemDbHandler;
    private invLevelDBHandler invLevelDBHandler;
    private EditText itemNumberEdt;
    private EditText locationEdt;
    private EditText quantityEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_item);

        // Initialize variables and UI elements
        itemNumberEdt = findViewById(R.id.enterItem);
        locationEdt = findViewById(R.id.enterLocation);
        quantityEdt = findViewById(R.id.enterQuantity);
        Button addItemButton = findViewById(R.id.addItem);
        Button removeItemButton = findViewById(R.id.RemoveItem);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        String lastChange = LocalDate.now().format(formatter);

        // Create instances of the database handlers
        itemDbHandler = new itemDBHandler(AddRemoveItemActivity.this);
        invLevelDBHandler = new invLevelDBHandler(AddRemoveItemActivity.this);

        // Set click listener for the add/update button
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from the input fields
                String itemNumber = itemNumberEdt.getText().toString();
                String location = locationEdt.getText().toString();
                String quantity = quantityEdt.getText().toString();

                // Check if any field is empty
                if (itemNumber.isEmpty() || location.isEmpty() || quantity.isEmpty()) {
                    Toast.makeText(AddRemoveItemActivity.this, "Please enter all the data.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Send values to itemDBHandler
                itemDbHandler.addNewItem(itemNumber, location, quantity, lastChange);

                // Toast message to show the item has been added
                Toast.makeText(AddRemoveItemActivity.this, "Item " + itemNumber + " has been stored in location " + location + ".", Toast.LENGTH_SHORT).show();
//
//                // Retrieve the phoneNumber value from the shared preference
//                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
//                String phoneNumber = preferences.getString("phoneNumber", null);
//
////                 Send SMS message to the phone number of the logged-in user
//                String message = "**Inventory App** Notification: Item " + itemNumber + " has been added to location " + location + ".";
//                sendSMSNotification(phoneNumber, message);

                // Clear the input fields
                itemNumberEdt.setText("");
                locationEdt.setText("");
                quantityEdt.setText("");

                // Set focus back to the first input needed
                itemNumberEdt.requestFocus();
            }
        });

        // Set click listener for the remove button
        removeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get data from the necessary EditText fields
                String itemNumber = itemNumberEdt.getText().toString();
                String location = locationEdt.getText().toString();

                // Check if any field is empty
                if (location.isEmpty() || itemNumber.isEmpty()) {
                    Toast.makeText(AddRemoveItemActivity.this, "Please enter Item & Location to delete.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Remove the item from the database
                itemDbHandler.removeItem(itemNumber, location);
                // Retrieve the current item quantity from the database
                int currentQuantity = itemDbHandler.getItemQuantity(itemNumber);


                // Retrieve the notification level from the invLevelDBHandler
                //int notificationLevelStr = invLevelDBHandler.getCurrentLevel();
               // int notificationLevel = notificationLevel;
                int notificationLevel = invLevelDBHandler.getCurrentLevel();



                // Check if the updated quantity falls below the notification level
                if (currentQuantity <=  notificationLevel) {
                    // Retrieve the phoneNumber value from the shared preference
                    SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    String phoneNumber = preferences.getString("phoneNumber", null);

                    // Send SMS message to the phone number of the logged-in user
                    String message = "**Inventory App** \nNotification: Item " + itemNumber + " has a current total inventory quantity of " + currentQuantity + " and has fallen below the set inventory notification level of " + notificationLevel + ".";
                    sendSMSNotification(phoneNumber, message);
                }


                // Toast message to show the item has been deleted
                Toast.makeText(AddRemoveItemActivity.this, "Item " + itemNumber + " has been deleted from Location " + location + ".", Toast.LENGTH_SHORT).show();

                // Clear the EditText fields
                itemNumberEdt.setText("");
                locationEdt.setText("");
                quantityEdt.setText("");

                // Set focus back to the first input needed
                itemNumberEdt.requestFocus();
            }
        });
    }

    // Function to send an SMS notification
    private void sendSMSNotification(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void gotoMainMenu(View view) {
        Intent intent = new Intent(this, Main_MenuActivity.class);
        startActivity(intent);
    }
}
