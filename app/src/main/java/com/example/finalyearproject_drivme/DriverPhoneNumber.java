package com.example.finalyearproject_drivme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class DriverPhoneNumber extends AppCompatActivity {
    //declare variables
    TextInputLayout mtilDPhoneNumber;
    TextInputEditText metDPhoneNumber;
    Button mbtnDriverOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_phone_number);

        //obtaining the View with specific ID
        mtilDPhoneNumber = findViewById(R.id.tilSignUpDriverPhoneNumber);
        metDPhoneNumber = findViewById(R.id.etSignUpDriverPhoneNumber);
        mbtnDriverOTP = findViewById(R.id.btnDriverOTP);

        //change error message
        metDPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mtilDPhoneNumber.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Nothing
            }
        });

        mbtnDriverOTP.setOnClickListener(v -> {
            //check condition (fields not empty) before proceed to next page
            if(Objects.requireNonNull(metDPhoneNumber.getText()).toString().trim().isEmpty()){
                mtilDPhoneNumber.setError("Field cannot be empty!");
            }
            else{
                //proceed to verify otp
                Intent intent = new Intent(DriverPhoneNumber.this, DriverOTP.class);
                intent.putExtra("dID", getIntent().getStringExtra("dIDNext"));
                intent.putExtra("dFName", getIntent().getStringExtra("dFNameNext"));
                intent.putExtra("dLName", getIntent().getStringExtra("dLNameNext"));
                intent.putExtra("dPhoneNumber", "+60" + metDPhoneNumber.getText().toString());
                intent.putExtra("dEmail", getIntent().getStringExtra("dEmailNext"));
                intent.putExtra("dPassword", getIntent().getStringExtra("dPasswordNext"));
                intent.putExtra("dRefCode", getIntent().getStringExtra("dRefCodeNext"));

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}