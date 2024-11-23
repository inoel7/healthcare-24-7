package com.example.healthcare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HealthArticlesDetailsActivity extends AppCompatActivity {

    TextView tv1;
    ImageView img;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles_details);

        btnBack = findViewById(R.id.buttonHADBack);
        tv1 = findViewById(R.id.textViewHADtitle);
        img = findViewById(R.id.imageViewHAD);

        Intent intent = getIntent();
        String title = intent.getStringExtra("Text1");
        int imageResId = intent.getIntExtra("Text2", -1);

        if (title != null) {
            tv1.setText(title);
        }

        if (imageResId != -1) {
            img.setImageResource(imageResId);
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HealthArticlesDetailsActivity.this, HealthArticlesActivity.class));
            }
        });
    }
}
