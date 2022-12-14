package com.example.finalyearproject_drivme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class UserRole extends AppCompatActivity {
    //declare variables
    Button mbtnDriver, mbtnTourist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_role);

        //assign variables
        mbtnDriver = findViewById(R.id.btnDriver);
        mbtnTourist = findViewById(R.id.btnTourist);

        //role(driver) -> driver option
        mbtnDriver.setOnClickListener(v -> {
            startActivity(new Intent(UserRole.this, DriverLogin.class));
            finish();
        });

        //role(tourist) -> tourist login
        mbtnTourist.setOnClickListener(v -> {
            startActivity(new Intent(UserRole.this, TouristLogin.class));
            finish();
        });
    }

    //ask before closing application
    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Leaving Drivme?");
        alertDialogBuilder
                .setMessage("Click yes to exit!")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    finishAffinity();
                    finish();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}