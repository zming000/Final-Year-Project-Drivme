package com.example.finalyearproject_drivme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TouristBookingDayDetails extends AppCompatActivity {
    //declare variables
    TextView mtvBOrderID, mtvBName, mtvBContact, mtvBTripOption, mtvBMeetDate, mtvBMeetTime, mtvBStartDate,
            mtvBEndDate, mtvBDuration, mtvBLocality, mtvBAddress, mtvBCarPlate, mtvBCarModel, mtvBCarColour,
            mtvBCarTrans, mtvBPetrolCompany, mtvBPetrolType, mtvBNotes, mtvBPriceDay, mtvBTotal;
    ImageView mivCopy;
    Button mbtnCancel, mbtnPayment;
    String orderID;
    FirebaseFirestore driverDetails, tripDetails, carDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_booking_day_details);

        //assign variables
        mtvBOrderID = findViewById(R.id.tvBOrderID);
        mtvBName = findViewById(R.id.tvBName);
        mtvBContact = findViewById(R.id.tvBContact);
        mtvBTripOption = findViewById(R.id.tvBTripOption);
        mtvBMeetDate = findViewById(R.id.tvBMeetDate);
        mtvBMeetTime = findViewById(R.id.tvBMeetTime);
        mtvBStartDate = findViewById(R.id.tvBStartDate);
        mtvBEndDate = findViewById(R.id.tvBEndDate);
        mtvBDuration = findViewById(R.id.tvBDuration);
        mtvBLocality = findViewById(R.id.tvBLocality);
        mtvBAddress = findViewById(R.id.tvBAddress);
        mtvBCarPlate = findViewById(R.id.tvBCarPlate);
        mtvBCarModel = findViewById(R.id.tvBCarModel);
        mtvBCarColour = findViewById(R.id.tvBCarColour);
        mtvBCarTrans = findViewById(R.id.tvBCarTrans);
        mtvBPetrolCompany = findViewById(R.id.tvBPetrolCompany);
        mtvBPetrolType = findViewById(R.id.tvBPetrolType);
        mtvBNotes = findViewById(R.id.tvBNotes);
        mtvBPriceDay = findViewById(R.id.tvBPriceDay);
        mtvBTotal = findViewById(R.id.tvBTotal);
        mivCopy = findViewById(R.id.ivCopy);
        mbtnCancel = findViewById(R.id.btnCancel);
        mbtnPayment = findViewById(R.id.btnPayment);

        orderID = getIntent().getStringExtra("orderID");

        //set text
        /*order id*/
        mtvBOrderID.setText(orderID);

        /*trip details*/
        tripDetails = FirebaseFirestore.getInstance();
        tripDetails.collection("Trip Details").document(orderID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        String status = doc.getString("orderStatus");

                        if(status.equals("Rejected by Driver") || status.equals("Cancelled by Tourist") || status.equals("Cancelled by Driver") || status.equals("Trip Ongoing") || status.equals("Trip Finished")){
                            mbtnCancel.setVisibility(View.GONE);
                        }
                        else if(status.equals("Pending Tourist Payment")){
                            mbtnPayment.setVisibility(View.VISIBLE);
                        }

                        mtvBTripOption.setText("By " + doc.getString("tripOption"));
                        mtvBMeetDate.setText(doc.getString("meetDate"));
                        mtvBMeetTime.setText(doc.getString("meetTime"));
                        mtvBStartDate.setText(doc.getString("meetDate"));
                        mtvBEndDate.setText(doc.getString("endDate"));

                        int num = Objects.requireNonNull(doc.getLong("duration")).intValue();

                        if(num > 1) {
                            mtvBDuration.setText(num + " days");
                        }
                        else{
                            mtvBDuration.setText(num + " day");
                        }

                        mtvBLocality.setText(doc.getString("locality"));
                        mtvBAddress.setText(doc.getString("address"));
                        mtvBNotes.setText(doc.getString("note"));
                        mtvBPriceDay.setText(String.valueOf(Objects.requireNonNull(doc.getLong("priceDriver")).intValue()));
                        mtvBTotal.setText(String.valueOf(Objects.requireNonNull(doc.getLong("total")).intValue()));

                        String touristID = doc.getString("touristID");
                        String driverID = doc.getString("driverID");
                        String carPlate = doc.getString("carPlate");

                        /*driver details*/
                        driverDetails = FirebaseFirestore.getInstance();
                        driverDetails.collection("User Accounts").document(Objects.requireNonNull(driverID)).get()
                                .addOnCompleteListener(task1 -> {
                                    DocumentSnapshot driver = task1.getResult();

                                    mtvBName.setText(driver.getString("lastName") + " " + driver.getString("firstName"));
                                    mtvBContact.setText(driver.getString("phoneNumber"));
                                });

                        /*car details*/
                        carDetails = FirebaseFirestore.getInstance();
                        carDetails.collection("User Accounts").document(Objects.requireNonNull(touristID)).collection("Car Details").document(Objects.requireNonNull(carPlate)).get()
                                .addOnCompleteListener(task2 -> {
                                    DocumentSnapshot car = task2.getResult();

                                    mtvBCarPlate.setText(car.getString("carPlate"));
                                    mtvBCarModel.setText(car.getString("carModel"));
                                    mtvBCarColour.setText(car.getString("carColour"));
                                    mtvBCarTrans.setText(car.getString("carTransmission"));
                                    mtvBPetrolCompany.setText(car.getString("petrolCompany"));
                                    mtvBPetrolType.setText(car.getString("petrolType"));
                                });
                    }
                });

        mivCopy.setOnClickListener(view -> {
            ClipboardManager copyAddress = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData address = ClipData.newPlainText("address", mtvBAddress.getText().toString());
            copyAddress.setPrimaryClip(address);

            Toast.makeText(TouristBookingDayDetails.this, "Copied address to clipboard!", Toast.LENGTH_SHORT).show();
        });

        mbtnPayment.setOnClickListener(view -> {
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(TouristBookingDayDetails.this);
            alertDialogBuilder.setTitle("Booking Payment");
            alertDialogBuilder
                    .setMessage("Do you wish to pay for booking?")
                    .setCancelable(false)
                    .setPositiveButton("Do Payment", (dialog, id) -> bookingPayment())
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());

            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        });

        mbtnCancel.setOnClickListener(view -> {
            String orderStatus = getIntent().getStringExtra("orderStatus");
            if(orderStatus.equals("Coming Soon")){
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(TouristBookingDayDetails.this);
                alertDialogBuilder.setTitle("Cancel Booking");
                alertDialogBuilder
                        .setMessage("You can only refund 90% of total price paid! Do you still wish to cancel and refund the booking?")
                        .setCancelable(false)
                        .setPositiveButton("Cancel Booking", (dialog, id) -> cancelBooking())
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            else {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(TouristBookingDayDetails.this);
                alertDialogBuilder.setTitle("Cancel Booking");
                alertDialogBuilder
                        .setMessage("Do you wish to cancel the booking?")
                        .setCancelable(false)
                        .setPositiveButton("Cancel Booking", (dialog, id) -> cancelBooking())
                        .setNegativeButton("No", (dialog, id) -> dialog.cancel());

                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void bookingPayment() {
        Intent booking = new Intent(TouristBookingDayDetails.this, TouristPayment.class);
        booking.putExtra("orderID", orderID);

        startActivity(booking);
    }

    private void cancelBooking() {
        FirebaseFirestore getToken = FirebaseFirestore.getInstance();
        FirebaseFirestore getAdminToken = FirebaseFirestore.getInstance();
        FirebaseFirestore getTouristID = FirebaseFirestore.getInstance();
        FirebaseFirestore updateOrderStatus = FirebaseFirestore.getInstance();
        FirebaseFirestore updateDriver = FirebaseFirestore.getInstance();
        FirebaseFirestore updateTourist = FirebaseFirestore.getInstance();
        String orderStatus = getIntent().getStringExtra("orderStatus");

        //delete date booked
        getTouristID.collection("Trip Details").document(orderID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        String tID = doc.getString("touristID");
                        String dID = doc.getString("driverID");
                        String startDate = doc.getString("meetDate");
                        float duration = doc.getLong("duration");
                        int days = Math.round(duration);

                        /* calculate dates */
                        //initialize
                        ArrayList<String> dates = new ArrayList<>();
                        SimpleDateFormat input = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
                        Calendar c = Calendar.getInstance();

                        //add start date
                        try {
                            dates.add(sdf.format((Objects.requireNonNull(input.parse(Objects.requireNonNull(startDate))))));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        //calculate and add dates into arraylist
                        for(int i = 0; i < days - 1; i++) {
                            //calculate end date with duration
                            try {
                                c.setTime(Objects.requireNonNull(sdf.parse(dates.get(i))));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            //add 1 day
                            c.add(Calendar.DATE, 1);

                            Date resultDate = new Date(c.getTimeInMillis());
                            dates.add(sdf.format(resultDate));
                        }

                        /*update driver date booked*/
                        for (int i = 0; i < dates.size(); i++) {
                            String dateID = dates.get(i);
                            updateDriver.collection("User Accounts").document(Objects.requireNonNull(dID)).collection("Date Booked").document(dateID)
                                    .delete();
                        }

                        /*update tourist date booked*/
                        for (int i = 0; i < dates.size(); i++) {
                            String dateID = dates.get(i);
                            updateTourist.collection("User Accounts").document(Objects.requireNonNull(tID)).collection("Date Booked").document(dateID)
                                    .delete();
                        }

                        /*send notification to driver*/
                        getToken.collection("User Accounts").document(Objects.requireNonNull(dID)).get()
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        DocumentSnapshot tourToken = task1.getResult();
                                        String token = tourToken.getString("notificationToken");

                                        UserFCMSend.pushNotification(
                                                TouristBookingDayDetails.this,
                                                token,
                                                "Booking Cancelled by Tourist",
                                                "Tourist have cancelled the booking!");

                                        //update order status
                                        Map<String,Object> order = new HashMap<>();
                                        order.put("orderStatus", "Cancelled by Tourist");

                                        if(orderStatus.equals("Coming Soon")){
                                            order.put("refundStatus", "Refund Needed");

                                            /*send notification to admin*/
                                            getAdminToken.collection("User Accounts").document("admin001").get()
                                                    .addOnCompleteListener(task2 -> {
                                                        if (task2.isSuccessful()) {
                                                            DocumentSnapshot doc2 = task2.getResult();
                                                            String adminToken = doc2.getString("notificationToken");

                                                            UserFCMSend.pushNotification(
                                                                    TouristBookingDayDetails.this,
                                                                    adminToken,
                                                                    "Refund Needed",
                                                                    "Tourist have cancelled a booking and requested a refund! Please review the order at the refund list!");
                                                        }
                                                    });

                                            Toast.makeText(TouristBookingDayDetails.this, "Booking cancelled successfully! Refund will be done in 5 working days!", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(TouristBookingDayDetails.this, "Booking cancelled successfully!", Toast.LENGTH_SHORT).show();
                                        }

                                        updateOrderStatus.collection("Trip Details").document(orderID)
                                                .update(order);

                                        startActivity(new Intent(TouristBookingDayDetails.this, TouristNavActivity.class));
                                        finishAffinity();
                                        finish();
                                    }
                                });
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(TouristBookingDayDetails.this, TouristNavActivity.class));
        finish();
    }
}