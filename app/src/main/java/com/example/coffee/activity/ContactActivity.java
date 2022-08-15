package com.example.coffee.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.example.coffee.R;

public class ContactActivity extends AppCompatActivity {
    private TextView facebook,instagram,github,phone;
    private AppCompatButton btBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        facebook = findViewById(R.id.facebook);
        instagram = findViewById(R.id.instagram);
        github = findViewById(R.id.github);
        phone = findViewById(R.id.phone);
        btBack = findViewById(R.id.btBackContact);
        facebook.setMovementMethod(LinkMovementMethod.getInstance());
        instagram.setMovementMethod(LinkMovementMethod.getInstance());
        github.setMovementMethod(LinkMovementMethod.getInstance());
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}