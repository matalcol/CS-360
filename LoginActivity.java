package com.example.inventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private userDBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHandler = new userDBHandler(this);

        boolean adminExists = dbHandler.checkAdminExists();

        Button createAdminButton = findViewById(R.id.CreateAdmin);

        if (adminExists) {
            // Admin exists, hide the Create Admin button
            createAdminButton.setVisibility(View.GONE);
        } else {
            // Admin doesn't exist, show the Create Admin button
            createAdminButton.setVisibility(View.VISIBLE);
        }

    }

public void gotoMainMenu(View view) {
    EditText usernameEdt = findViewById(R.id.enterUserID);
    EditText passwordEdt = findViewById(R.id.enterPW);


    String username = usernameEdt.getText().toString();
    String password = passwordEdt.getText().toString();

    if (username.isEmpty() || password.isEmpty()) {
        Toast.makeText(LoginActivity.this, "Please enter both username and password.", Toast.LENGTH_SHORT).show();
        return;
    }

    // Create instance of the userDBHandler
    userDBHandler userDbHandler = new userDBHandler(LoginActivity.this);
    boolean loginSuccessful = userDbHandler.checkLogin(username, password);

    if (loginSuccessful) {
        int accessLevel = userDbHandler.checkAccessLevel(username, password); // Replace checkAccessLevel with the correct method name
        String phoneNumber = userDbHandler.getPhoneNumber(username, password);

        // Store the accessLevel and phone number value in a shared preference
            SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("accessLevel", accessLevel);
            editor.putString("phoneNumber", phoneNumber);
            editor.apply();

            // Start the Main_MenuActivity
            Intent intent = new Intent(this, Main_MenuActivity.class);
            startActivity(intent);
    } else {
        Toast.makeText(LoginActivity.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
    }

}


    public void gotoAdmin(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}
