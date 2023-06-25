package com.example.inventoryapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.security.Permission;

public class Main_MenuActivity extends AppCompatActivity {
   // private static final int SMS_PERMISSION_REQUEST_CODE = 1;//?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Check if the necessary permissions are granted
        if (checkPermission()) {
            setContentView(R.layout.activity_main_menu);// Permissions are granted, proceed with normal initialization
            //initialize();
        } else {
            // Permissions are not granted, start the Permission activity
            startActivity(new Intent(this, Permission.class));
            finish();
        }

        // Retrieve the accessLevel value from the shared preference
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int userAccessLevel = preferences.getInt("accessLevel", 0);

        // Show/hide the admin button based on the user's access level
        Button adminButton = findViewById(R.id.adminButton);
        if (userAccessLevel == 2) {
            adminButton.setVisibility(View.VISIBLE);
        } else {
            adminButton.setVisibility(View.GONE);
        }
    }

    private boolean checkPermission() {
        int receiveSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int readSmsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        return receiveSmsPermission == PackageManager.PERMISSION_GRANTED && readSmsPermission == PackageManager.PERMISSION_GRANTED;
    }

//    private void initialize() {
//        // Perform initialization tasks here
//        // ...
//    }

    public void logOutGotoLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void gotoAllInventory(View view) {
        Intent intent = new Intent(this, AllInventoryActivity.class);
        startActivity(intent);
    }

    public void gotoSearchInventory(View view) {
        Intent intent = new Intent(this, SearchInventoryActivity.class);
        startActivity(intent);
    }

    //gotoItemQuantities
    public void gotoItemQuantities(View view) {
        Intent intent = new Intent(this, ItemQuantityTotals.class);
        startActivity(intent);
    }
    public void gotoAdmin(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}

