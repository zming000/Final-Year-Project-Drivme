package com.example.finalyearproject_drivme;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hsalf.smilerating.SmileRating;

import java.util.HashMap;
import java.util.Map;

public class TouristAboutUs extends AppCompatActivity {
    //declare variables
    ConstraintLayout mclickTUA, mclickTPP, mclickTF;
    FirebaseFirestore feedbackRate;
    SharedPreferences spDrivme;

    //key name
    private static final String SP_NAME = "drivmePref";
    private static final String KEY_ID = "userID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_about_us);

        //assign variables
        mclickTUA = findViewById(R.id.clickTUA);
        mclickTPP = findViewById(R.id.clickTPP);
        mclickTF = findViewById(R.id.clickTF);

        mclickTUA.setOnClickListener(agreementView -> {
            //initialize layout
            LayoutInflater dialogInflater = getLayoutInflater();
            agreementView = dialogInflater.inflate(R.layout.activity_user_agreement_popup, null);

            //initialize dialog builder
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.dialog);
            AlertDialog agreementDialog = dialogBuilder.setView(agreementView).create();

            //assign variables
            ImageView mivClose = agreementView.findViewById(R.id.ivClose);

            mivClose.setOnClickListener(view1 -> agreementDialog.dismiss());

            //display dialog with suitable size
            agreementDialog.show();
            agreementDialog.getWindow().setLayout(650, 1100);
        });

        mclickTPP.setOnClickListener(policyView -> {
            //initialize layout
            LayoutInflater dialogInflater = getLayoutInflater();
            policyView = dialogInflater.inflate(R.layout.activity_privacy_policy_popout, null);

            //initialize dialog builder
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.dialog);
            AlertDialog policyDialog = dialogBuilder.setView(policyView).create();

            //assign variables
            ImageView mivCloseP = policyView.findViewById(R.id.ivCloseP);

            mivCloseP.setOnClickListener(view1 -> policyDialog.dismiss());

            //display dialog with suitable size
            policyDialog.show();
            policyDialog.getWindow().setLayout(650, 1100);
        });

        mclickTF.setOnClickListener(feedbackView -> {
            //initialize layout
            LayoutInflater dialogInflater = getLayoutInflater();
            feedbackView = dialogInflater.inflate(R.layout.activity_user_feedback, null);

            //initialize dialog builder
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.dialog);
            AlertDialog feedbackDialog = dialogBuilder.setView(feedbackView).create();

            //assign variables
            SmileRating mFeedbackRating = feedbackView.findViewById(R.id.feedbackRating);

            mFeedbackRating.setOnSmileySelectionListener(smiley -> {
                spDrivme = getSharedPreferences(SP_NAME, MODE_PRIVATE);
                //get user id from shared preference
                String uID = spDrivme.getString(KEY_ID, null);
                feedbackRate = FirebaseFirestore.getInstance();
                String feedback = "";

                switch (smiley){
                    case SmileRating.TERRIBLE:
                        feedback = "Terrible";
                        break;
                    case SmileRating.BAD:
                        feedback = "Bad";
                        break;
                    case SmileRating.OKAY:
                        feedback = "Okay";
                        break;
                    case SmileRating.GOOD:
                        feedback = "Good";
                        break;
                    case SmileRating.GREAT:
                        feedback = "Great";
                        break;
                }

                Map<String,Object> feedbackDB = new HashMap<>();
                feedbackDB.put("userID", uID);
                feedbackDB.put("feedback", feedback);

                feedbackRate.collection("App Feedbacks").document(uID)
                        .set(feedbackDB)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(TouristAboutUs.this, "Feedback Sent!", Toast.LENGTH_SHORT).show();
                            feedbackDialog.dismiss();
                        });
            });

            //display dialog with suitable size
            feedbackDialog.show();
            feedbackDialog.getWindow().setLayout(650, 600);
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TouristAboutUs.this, TouristNavSettings.class));
        finishAffinity();
        finish();
    }
}