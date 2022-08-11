package com.example.coffee.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;

public class PhotoActivity extends AppCompatActivity {
    private ImageView bigImg;
    private AppCompatButton btBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        bigImg = findViewById(R.id.bigImgReview);
        btBack = findViewById(R.id.btBackPhoto);
        btBack.setOnClickListener(v -> onBackPressed());
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        Glide.with(bigImg).load(url).error(R.drawable.error_img).into(bigImg);
    }
}