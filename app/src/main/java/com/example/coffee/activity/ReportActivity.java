package com.example.coffee.activity;

import static com.example.coffee.R.color.dark_grey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.coffee.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ReportActivity extends AppCompatActivity {

    private AppCompatButton btBack,btSend;
    private RadioGroup groupReport;
    private RadioButton rb1,rb2,rb3,rb4,rb5,rb6,rb7;
    private String s ="";
    private EditText etReport;
    private String id,username,coffeeReview,imgReview,review,rate,date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        btBack = findViewById(R.id.btBackReport);
        btSend = findViewById(R.id.btSendReport);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);
        rb4 = findViewById(R.id.rb4);
        rb5 = findViewById(R.id.rb5);
        rb6 = findViewById(R.id.rb6);
        rb7 = findViewById(R.id.rb7);
        etReport = findViewById(R.id.tvRb7);
        groupReport = findViewById(R.id.groupReport);
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        username = intent.getStringExtra("username");
        coffeeReview = intent.getStringExtra("coffeeReview");
        imgReview = intent.getStringExtra("imgReview");
        review = intent.getStringExtra("review");
        rate = intent.getStringExtra("rate");
        date = intent.getStringExtra("date");
        btBack.setOnClickListener(v -> onBackPressed());
        groupReport.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb1:
                        s = rb1.getText().toString().trim();
                        etReport.setVisibility(View.GONE);
                        changeBackground();
                        break;
                    case R.id.rb2:
                        s = rb2.getText().toString().trim();
                        etReport.setVisibility(View.GONE);
                        changeBackground();
                        break;
                    case R.id.rb3:
                        s = rb3.getText().toString().trim();
                        etReport.setVisibility(View.GONE);
                        changeBackground();
                        break;
                    case R.id.rb4:
                        s = rb4.getText().toString().trim();
                        etReport.setVisibility(View.GONE);
                        changeBackground();
                        break;
                    case R.id.rb5:
                        s = rb5.getText().toString().trim();
                        etReport.setVisibility(View.GONE);
                        changeBackground();
                        break;
                    case R.id.rb6:
                        s = rb6.getText().toString().trim();
                        etReport.setVisibility(View.GONE);
                        changeBackground();
                        break;
                    case R.id.rb7:
                        etReport.setVisibility(View.VISIBLE);
                        changeBackground();
                        break;
                }
            }
        });
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rb7.isChecked()){
                    s = etReport.getText().toString().trim();
                }
                if (!s.equals("")){
                    long timestamp = System.currentTimeMillis();
                    HashMap<String,Object> hashMap = new HashMap<>();
                    hashMap.put("idReport",""+timestamp);
                    hashMap.put("idReview",id);
                    hashMap.put("username",username);
                    hashMap.put("coffeeReview",""+coffeeReview);
                    hashMap.put("imgReview",imgReview);
                    hashMap.put("review",review);
                    hashMap.put("rate",rate);
                    hashMap.put("date",date);
                    hashMap.put("reason",s);
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Report");
                    reference.child(""+timestamp)
                            .setValue(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ReportActivity.this,"Cảm ơn bạn đánh báo cáo",Toast.LENGTH_SHORT).show();
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            onBackPressed();
                                        }
                                    },1500);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ReportActivity.this,"Lỗi! Vui lòng thử lại",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
    private void changeBackground() {
        btSend.setBackgroundResource(R.drawable.button_custom5);
        btSend.setTextColor(getResources().getColor(R.color.white));
    }
}