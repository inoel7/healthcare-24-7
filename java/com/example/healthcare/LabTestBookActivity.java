package com.example.healthcare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LabTestBookActivity extends AppCompatActivity {
    EditText edname, edaddress, edcontact, edpincode;
    Button btnBooking;
    String date, time;
    float amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_book);

        edname = findViewById(R.id.editTextLTBFullName);
        edaddress = findViewById(R.id.editTextLTBAddress);
        edcontact = findViewById(R.id.editTextLTBContact);
        edpincode = findViewById(R.id.editTextLTBPincode);
        btnBooking = findViewById(R.id.buttonLTBBooking);

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        String total = intent.getStringExtra("price").replace("Total Cost: ", "").replace("/-", "");
        amount = Float.parseFloat(total);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullname = edname.getText().toString();
                String address = edaddress.getText().toString();
                String contact = edcontact.getText().toString();
                String pincode = edpincode.getText().toString();

                if (fullname.isEmpty() || address.isEmpty() || contact.isEmpty() || pincode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill all details", Toast.LENGTH_LONG).show();
                } else {
                    Database db = new Database(getApplicationContext(), "healthcare", null, 1);
                    db.addOrder(username, fullname, address, contact, Integer.parseInt(pincode), date, time, amount, "lab");
                    db.removeCart(username, "lab");
                    Toast.makeText(getApplicationContext(), "Your booking is done successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LabTestBookActivity.this, HomeActivity.class));
                }
            }
        });
    }
}
