package com.example.inventoryapp;


import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Permission extends AppCompatActivity {
    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private boolean permissionRequested = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        Button grantPermissionsButton = findViewById(R.id.button10);
        grantPermissionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button10) {
                requestSmsPermissions();
            }
        });
    }

    private void requestSmsPermissions() {
        if (!permissionRequested) {
            permissionRequested = true;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        }
    }

    private void navigateToMainMenu() {
        startActivity(new Intent(this, Main_MenuActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            permissionRequested = false;

            boolean allPermissionsGranted = true;
            boolean shouldShowRationale = false;
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)
                            && !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {
                        shouldShowRationale = true;
                    }
                    break;
                }
            }

            if (allPermissionsGranted) {
                // Permissions granted, proceed to the main menu screen
                navigateToMainMenu();
            } else {
                if (shouldShowRationale) {
                    // Permissions were denied with "Don't ask again" option, show a dialog explaining the need for permissions
                    showPermissionExplanationDialog();
                } else {
                    // Permissions were denied, show a toast message
                    showPermissionDeniedMessage();
                }

                // Proceed to the main menu screen regardless of permission result
                navigateToMainMenu();
            }
        }
    }

    private void showPermissionExplanationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Required")
                .setMessage("Please grant the required permissions to use this app.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestSmsPermissions();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        navigateToMainMenu();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void showPermissionDeniedMessage() {
        Toast.makeText(this, "Permissions were denied. Please manually enable them in the app settings.", Toast.LENGTH_LONG).show();
    }
}
