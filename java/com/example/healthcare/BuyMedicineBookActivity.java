package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class BuyMedicineBookActivity extends AppCompatActivity {
    private static final String TAG = "BuyMedicineBookActivity";
    EditText edname, edaddress, edcontact, edpincode;
    Button btnBooking;
    String date, time;
    float amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_book);

        edname = findViewById(R.id.editTextBMBFullName);
        edaddress = findViewById(R.id.editTextBMBAddress);
        edcontact = findViewById(R.id.editTextBMBContact);
        edpincode = findViewById(R.id.editTextBMBPincode);
        btnBooking = findViewById(R.id.buttonBMBBooking);

        // Retrieve Intent extras with default values
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        String total = intent.getStringExtra("price");

        if (total != null) {
            try {
                String totalClean = total.replace("Total Cost: ", "").replace("/-", "").trim();
                amount = Float.parseFloat(totalClean);
            } catch (NumberFormatException e) {
                Log.e(TAG, "Error parsing total cost", e);
                Toast.makeText(this, "Invalid total cost received", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
        } else {
            Log.e(TAG, "Total cost is null");
            Toast.makeText(this, "No total cost provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load username from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "Username is null or empty");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = edname.getText().toString();
                String address = edaddress.getText().toString();
                String contact = edcontact.getText().toString();
                String pincode = edpincode.getText().toString();

                // Validate input fields
                if (fullname.isEmpty() || address.isEmpty() || contact.isEmpty() || pincode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    int pin = Integer.parseInt(pincode);

                    Database db = new Database(getApplicationContext(), "healthcare", null, 1);
                    db.addOrder(username, fullname, address, contact, pin, date, time, amount, "medicine");
                    db.removeCart(username, "medicine");

                    Toast.makeText(getApplicationContext(), "Your booking is done successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(BuyMedicineBookActivity.this, HomeActivity.class));
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Invalid pincode format", e);
                    Toast.makeText(getApplicationContext(), "Invalid pincode", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "Error during booking", e);
                    Toast.makeText(getApplicationContext(), "Booking failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}