package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.example.coffee.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    private EditText name,des,category,price,diachi,mota,ten;
    private Button btAdd,btAddShop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        name = findViewById(R.id.name);
        des = findViewById(R.id.description);
        category = findViewById(R.id.category);
        price = findViewById(R.id.price);
        btAdd = findViewById(R.id.btAdd);
        diachi = findViewById(R.id.diachi);
        ten = findViewById(R.id.tenSHop);
        mota = findViewById(R.id.mota);
        btAddShop = findViewById(R.id.btAddShop);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strName = name.getText().toString().trim();
                String strDes = des.getText().toString().trim();
                String strCategory = category.getText().toString().trim();
                String strPrice = price.getText().toString().trim();
                HashMap<String,Object> hashMap = new HashMap<>();
                long timestamp = System.currentTimeMillis();
                hashMap.put("id",""+timestamp);
                hashMap.put("name",strName);
                hashMap.put("description",strDes);
                hashMap.put("category",strCategory);
                hashMap.put("price",strPrice);
                hashMap.put("ImgUrl","");
                hashMap.put("background","");
                hashMap.put("rate","");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
                reference.child(strName)
                        .setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminActivity.this,"Not Ok",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        btAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTen = ten.getText().toString().trim();
                String strDiachi = diachi.getText().toString().trim();
                String strMota = mota.getText().toString().trim();
                HashMap<Object,String> hashMap = new HashMap<>();
                hashMap.put("name",strTen);
                hashMap.put("address",strDiachi);
                hashMap.put("description",strMota);
                hashMap.put("ImgUrl","");
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shop");
                reference.child(strTen)
                        .setValue(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(AdminActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AdminActivity.this,"Not Ok",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}