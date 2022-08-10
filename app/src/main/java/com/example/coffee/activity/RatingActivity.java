package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatRatingBar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class RatingActivity extends AppCompatActivity {
    private ImageView imgCoffee;
    private TextView nameCoffee, sizeCoffee,iceCoffee,quantityCoffee;
    private AppCompatRatingBar ratingBar;
    private EditText stringRate;
    private AppCompatButton btAddImg,btBack;
    private CheckBox cb1,cb2,cb3,cb4,cb5;
    private String strCb = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        imgCoffee = findViewById(R.id.imgCoffeeRating);
        nameCoffee = findViewById(R.id.nameCoffeeRating);
        sizeCoffee = findViewById(R.id.sizeRating);
        iceCoffee = findViewById(R.id.iceRating);
        quantityCoffee = findViewById(R.id.quantityRating);
        ratingBar = findViewById(R.id.ratingbar);
        stringRate = findViewById(R.id.etRating);
        btAddImg = findViewById(R.id.btAddImg);
        btBack = findViewById(R.id.btBackRating);
        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        cb4 = findViewById(R.id.cb4);
        cb5 = findViewById(R.id.cb5);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String img = intent.getStringExtra("imgUrl");
        String size = intent.getStringExtra("size");
        String ice = intent.getStringExtra("ice");
        String quantity = intent.getStringExtra("quantity");
        nameCoffee.setText(name);
        Glide.with(imgCoffee).load(img).error(R.drawable.error_img).into(imgCoffee);
        sizeCoffee.setText(String.format("Size : %s | ", size));
        iceCoffee.setText(String.format("Phần trăm đá :%s", ice));
        quantityCoffee.setText(String.format("Số lượng : %s", quantity));
        btBack.setOnClickListener(v -> onBackPressed());
        setTextCheckBox();
    }

    private void setTextCheckBox() {
        String strCb1 = cb1.getText().toString().trim();
        String strCb2 = cb2.getText().toString().trim();
        String strCb3 = cb3.getText().toString().trim();
        String strCb4 = cb4.getText().toString().trim();
        String strCb5 = cb5.getText().toString().trim();
        cb1.setOnClickListener(v -> {
            if (cb1.isChecked()){
                strCb = strCb.replaceAll(strCb1 +" {2}","");
                strCb = strCb + strCb1 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb1+" {2}","");
            }
            stringRate.setText(strCb);
        });
        cb2.setOnClickListener(v -> {
            if (cb2.isChecked()){
                strCb = strCb.replaceAll(strCb2 +" {2}","");
                strCb = strCb + strCb2 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb2+" {2}","");
            }
            stringRate.setText(strCb);
        });
        cb3.setOnClickListener(v -> {
            if (cb3.isChecked()){
                strCb = strCb.replaceAll(strCb3 +" {2}","");
                strCb = strCb + strCb3 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb3+"  ","");
            }
            stringRate.setText(strCb);
        });
        cb4.setOnClickListener(v -> {
            if (cb4.isChecked()){
                strCb = strCb.replaceAll(strCb4 +" {2}","");
                strCb = strCb + strCb4 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb4+"  ","");
            }
            stringRate.setText(strCb);
        });
        cb5.setOnClickListener(v -> {
            if (cb5.isChecked()){
                strCb = strCb.replaceAll(strCb5 +" {2}","");
                strCb = strCb + strCb5 + "  ";
            }
            else {
                strCb = strCb.replaceAll(strCb5+"  ","");
            }
            stringRate.setText(strCb);
        });
    }
}