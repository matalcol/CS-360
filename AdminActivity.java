package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private userDBHandler userDbHandler;
    private Button mainMenuButton;
    private invLevelDBHandler invLevelDBHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        // Initialize variables
        EditText userIDEdt = findViewById(R.id.enterUserID);
        EditText nameEdt = findViewById(R.id.enterName);
        EditText phoneEdt = findViewById(R.id.enterPhoneNumber);
        EditText accessLevelEdt = findViewById(R.id.enterAccessLevel);
        EditText passwordEdt = findViewById(R.id.enterPassword);
        EditText updateLevelEdt = findViewById(R.id.enterInvLevelSMS);
        Button addUpdateUser = findViewById(R.id.addUpdateUser);
        Button removeUser = findViewById(R.id.removeUser);
        Button updateLevel = findViewById(R.id.UpdateMinInvLv);
        TextView currentLevelTextView = findViewById(R.id.minInventory);

        // Create an instance of the UserDBHandler
        userDbHandler = new userDBHandler(AdminActivity.this);
        mainMenuButton = findViewById(R.id.MainMenu);
        mainMenuButton.setVisibility(View.GONE);

        // Create an instance of the invLevelDBHandler
        invLevelDBHandler = new invLevelDBHandler(AdminActivity.this);
        // Check if the admin exists after adding/updating the user
        boolean adminExists = userDbHandler.checkAdminExists();

        if (adminExists) {
            // Admin exists, show the "Main Menu" button
            mainMenuButton.setVisibility(View.VISIBLE);
        }
        // Retrieve the current LEVEL value from the database
        int currentLevel = invLevelDBHandler.getCurrentLevel(); // Replace invLevelDBHandler with the actual instance of your inventory level DB handler

        // Set the current LEVEL value in the TextView
        currentLevelTextView.setText("Minimum Inventory SMS Notification Level: " + currentLevel);

        // Set click listener for the add/update button
        addUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = userIDEdt.getText().toString();
                String name = nameEdt.getText().toString();
                String phNumber = phoneEdt.getText().toString();
                String accessLevel = accessLevelEdt.getText().toString();
                String password = passwordEdt.getText().toString();

                if (userID.isEmpty() || name.isEmpty() || accessLevel.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AdminActivity.this, "Please enter all the data.", Toast.LENGTH_SHORT).show();
                    return;
                }

                userDbHandler.addNewUser(userID, name, phNumber, accessLevel, password);

                Toast.makeText(AdminActivity.this, "User has been updated.", Toast.LENGTH_SHORT).show();
                clearEditTextFields(userIDEdt, nameEdt, phoneEdt, accessLevelEdt, passwordEdt);
                userIDEdt.requestFocus();

                // Check if the admin exists after adding/updating the user
                boolean adminExists = userDbHandler.checkAdminExists();

                if (adminExists) {
                    // Admin exists, show the "Main Menu" button
                    mainMenuButton.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set click listener for the remove button
        removeUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userID = userIDEdt.getText().toString();

                if (userID.isEmpty()) {
                    Toast.makeText(AdminActivity.this, "Please enter User ID to delete.", Toast.LENGTH_SHORT).show();
                    return;
                }

                userDbHandler.removeUser(userID);

                Toast.makeText(AdminActivity.this, "User " + userID + " has been deleted.", Toast.LENGTH_SHORT).show();
                clearEditTextFields(userIDEdt);
                userIDEdt.requestFocus();
            }
        });

// Set click listener for the updateLevel button
        updateLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newLevel = updateLevelEdt.getText().toString();

                if (newLevel.isEmpty()) {
                    Toast.makeText(AdminActivity.this, "Update Level cannot be blank.", Toast.LENGTH_SHORT).show();
                    return;
                }

                //setting newLevel in DB
                invLevelDBHandler.updateLevel(newLevel);

                Toast.makeText(AdminActivity.this, "New Inventory SMS Level " + newLevel + " has been set.", Toast.LENGTH_SHORT).show();
                clearEditTextFields(updateLevelEdt);
                userIDEdt.requestFocus();

                //gets the current level from the database
                int currentLevel = invLevelDBHandler.getCurrentLevel();
                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("currentLevel", currentLevel);
                // Set the current LEVEL value to the TextView
                currentLevelTextView.setText("Minimum Inventory SMS Notification Level: " + currentLevel);

            }
        });


    }

    private void clearEditTextFields(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setText("");
        }
    }

    public void gotoMainMenu(View view) {
        Intent intent = new Intent(this, Main_MenuActivity.class);
        startActivity(intent);
    }
}

