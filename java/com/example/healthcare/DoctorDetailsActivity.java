package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class DoctorDetailsActivity extends AppCompatActivity {

    private static final String TAG = "DoctorDetailsActivity";


    private String[][] doctor_details1 = {
            {"Doctor Name: Noel Sebastin", "Hospital Address: Kodagu", "Exp: 5yrs", "Mobile No:9898989898", "600"},
            {"Doctor Name: Pavan M", "Hospital Address: Mysore", "Exp: 15yrs", "Mobile No: 7898989898", "900"},
            {"Doctor Name: Manu M", "Hospital Address: Dharwad", "Exp: 8yrs", "Mobile No:8898989898", "300"},
            {"Doctor Name: Saadhan P", "Hospital Address: Mysore", "Exp: 6yrs", "Mobile No:9898000000", "500"},
            {"Doctor Name: Srinivas SP", "Hospital Address: Bengaluru", "Exp: 7yrs", "Mobile No:7798989898", "800"}

    };

    private String[][] doctor_details2 = {
            {"Doctor Name: Neelam Patil", "Hospital Address: Pimpri", "Exp: 5yrs", "Mobile No:9898989898", "600"},
            {"Doctor Name: Swati Pawar", "Hospital Address: Nigdi", "Exp: 15yrs", "Mobile No: 7898989898", "900"},
            {"Doctor Name: Neeraja Kale", "Hospital Address: Pune", "Exp: 8yrs", "Mobile No:8898989898", "300"},
            {"Doctor Name: Mayuri Deshmukh", "Hospital Address: Chinchwad", "Exp: 6yrs", "Mobile No:9898000000", "500"},
            {"Doctor Name: Minakshi Panda", "Hospital Address: Katraj", "Exp: 7yrs", "Mobile No:7798989898", "800"}

    };

    private String[][] doctor_details3 = {
            {"Doctor Name: Seema Patil", "Hospital Address: Pimpri", "Exp: 4yrs", "Mobile No:9898989898", "200"},
            {"Doctor Name: Pnkaj Parab", "Hospital Address: Nigdi", "Exp: 5yrs", "Mobile No:7898989898", "300"},
            {"Doctor Name: Monish Jain", "Hospital Address: Pune", "Exp: 7yrs", "Mobile No:8898989898", "300"},
            {"Doctor Name: Vishal Deshmukh", "Hospital Address: Chinchwad", "Exp: 6yrs", "Mobile No:9898000000", "500"},
            {"Doctor Name: Shrikant Panda", "Hospital Address: Katraj", "Exp: 7yrs", "Mobile No:7798989898", "600"}

    };

    private String[][] doctor_details4 = {
            {"Doctor Name: Amol Gawade", "Hospital Address: Pimpri", "Exp: 5yrs", "Mobile No:9898989898", "600"},
            {"Doctor Name: Prasad Pawar", "Hospital Address: Nigdi", "Exp: 15yrs", "Mobile No:7898989898", "900"},
            {"Doctor Name: Nilesh Kale", "Hospital Address: Pune", "Exp: 8yrs", "Mobile No:8898989898", "300"},
            {"Doctor Name: Deepak Deshpande", "Hospital Address: Chinchwad", "Exp: 6yrs", "Mobile No:9898000000", "500"},
            {"Doctor Name: Ashok Singh", "Hospital Address: Katraj", "Exp: 7yrs", "Mobile No:7798989898", "800"}

    };

    private String[][] doctor_details5 = {
            {"Doctor Name: Nilesh Borate", "Hospital Address: Pimpri", "Exp: 5yrs", "Mobile No:9898989898", "1600"},
            {"Doctor Name: Pamkaj Pawar", "Hospital Address: Nigdi", "Exp: 15yrs", "Mobile No:7898989898", "1900"},
            {"Doctor Name: Swapnil Lele", "Hospital Address: Pune", "Exp: 8yrs", "Mobile No:8898989898", "1300"},
            {"Doctor Name: Deepak Kumar", "Hospital Address: Chinchwad", "Exp: 6yrs", "Mobile No:9898000000", "1500"},
            {"Doctor Name: Ankul Panda", "Hospital Address: Katraj", "Exp: 7yrs", "Mobile No:7798989898", "1800"}

    };

    TextView tv;
    Button btn;
    String[][] doctor_details = {};
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        tv = findViewById(R.id.textViewLT);
        btn = findViewById(R.id.buttonBMCartCheckout);
        Intent it = getIntent();
        String title = it.getStringExtra("title");
        Log.d(TAG, "Received title: " + title);
        tv.setText(title);

        if (title.equals("Family Physicians")) {
            doctor_details = doctor_details1;
        } else if (title.equals("Dietician")) {
            doctor_details = doctor_details2;
        } else if (title.equals("Dentist")) {
            doctor_details = doctor_details3;
        } else if (title.equals("Surgeon")) {
            doctor_details = doctor_details4;
        } else {
            doctor_details = doctor_details5;
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorDetailsActivity.this, FindDoctorActivity.class));
            }
        });

        list = new ArrayList<>();
        for (String[] doctor_detail : doctor_details) {
            item = new HashMap<>();
            item.put("Line1", doctor_detail[0]);
            item.put("line2", doctor_detail[1]);
            item.put("Line3", doctor_detail[2]);
            item.put("line4", doctor_detail[3]);
            item.put("line5", "Cons Fees " + doctor_detail[4] + "-/");
            list.add(item);
        }

        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines,
                new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
        ListView lst = findViewById(R.id.listViewBMCartTitle);
        lst.setAdapter(sa);

        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent it = new Intent(DoctorDetailsActivity.this, BookAppointmentActivity.class);
                it.putExtra("Text1", title);
                it.putExtra("Text2", doctor_details[i][0]);
                it.putExtra("Text3", doctor_details[i][1]);
                it.putExtra("Text4", doctor_details[i][3]);
                it.putExtra("Text5", doctor_details[i][4]);
                startActivity(it);
            }
        });
    }
}