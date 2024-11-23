package com.example.healthcare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class CartBuyMedicineActivity extends AppCompatActivity {
    private static final String TAG = "CartBuyMedicineActivity";
    private ArrayList<HashMap<String, String>> list;
    private SimpleAdapter sa;
    private TextView tvTotal;
    private ListView lst;
    private DatePickerDialog datePickerDialog;
    private Button dateButton, btnCheckout, btnBack;
    private String[][] packages = {};
    private float totalAmount = 0;  // Move totalAmount to a class-level variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_buy_medicine);

        dateButton = findViewById(R.id.buttonBMCartDate);
        btnCheckout = findViewById(R.id.buttonBMCartCheckout);
        btnBack = findViewById(R.id.buttonBMCartBack);
        tvTotal = findViewById(R.id.textViewBMCartTotalCost);
        lst = findViewById(R.id.listViewBMCart);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "Username is null or empty");
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if the user is not logged in
            return;
        }

        Log.d(TAG, "Username: " + username);

        Database db = new Database(getApplicationContext(), "healthcare", null, 1);

        ArrayList<String> dbData = null;
        try {
            dbData = db.getCartData(username, "medicine");
        } catch (Exception e) {
            Log.e(TAG, "Error fetching cart data", e);
        }

        if (dbData != null && !dbData.isEmpty()) {
            packages = new String[dbData.size()][5];
            for (int i = 0; i < dbData.size(); i++) {
                String arrData = dbData.get(i);
                Log.d(TAG, "Cart Data: " + arrData);
                String[] strData = arrData.split(java.util.regex.Pattern.quote("$"));
                if (strData.length >= 2) {
                    packages[i][0] = strData[0];
                    packages[i][1] = ""; // Placeholder, adjust if needed
                    packages[i][2] = ""; // Placeholder, adjust if needed
                    packages[i][3] = ""; // Placeholder, adjust if needed
                    packages[i][4] = "Cost: " + strData[1] + "/-";
                    try {
                        totalAmount += Float.parseFloat(strData[1]);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Error parsing price", e);
                    }
                } else {
                    Log.e(TAG, "Unexpected cart data format: " + arrData);
                }
            }
        } else {
            Log.e(TAG, "Cart data is null or empty");
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
        }

        tvTotal.setText("Total Cost: " + totalAmount + "/-");

        list = new ArrayList<>();
        for (String[] aPackage : packages) {
            HashMap<String, String> item = new HashMap<>();
            item.put("Line1", aPackage[0]);
            item.put("Line2", aPackage[1]);
            item.put("Line3", aPackage[2]);
            item.put("Line4", aPackage[3]);
            item.put("Line5", aPackage[4]);
            list.add(item);
        }
        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"Line1", "Line2", "Line3", "Line4", "Line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});

        lst.setAdapter(sa);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CartBuyMedicineActivity.this, BuyMedicineActivity.class));
            }
        });

        btnCheckout.setOnClickListener(view -> {
            if (totalAmount == 0) {
                Toast.makeText(getApplicationContext(), "No items in cart to checkout", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(CartBuyMedicineActivity.this, BuyMedicineBookActivity.class);
                intent.putExtra("price", "Total Cost: " + totalAmount + "/-"); // Ensure correct formatting
                intent.putExtra("date", dateButton.getText().toString());
                startActivity(intent);
            }
        });


        initDatePicker();
        dateButton.setOnClickListener(view -> datePickerDialog.show());
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String date = day + "/" + month + "/" + year;
            dateButton.setText(date);
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis() + 86400000); // Minimum date is tomorrow
        }
    }
}
