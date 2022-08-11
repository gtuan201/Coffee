package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.coffee.R;
import com.example.coffee.adapter.CoffeeRateAdapter;
import com.example.coffee.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RateActivity extends AppCompatActivity {
    private AppCompatButton btBack;
    private RecyclerView rev_coffee_rate;
    private List<Cart> list;
    private CoffeeRateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        rev_coffee_rate = findViewById(R.id.rev_rate_coffee);
        btBack = findViewById(R.id.btBackOrderRate);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(RateActivity.this,LinearLayoutManager.VERTICAL,false);
        rev_coffee_rate.setLayoutManager(manager);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bill");
        reference.child("customer").child(id).child("coffee")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String imgUrl  = ""+dataSnapshot.child("image").getValue();
                            String name = ""+dataSnapshot.child("name").getValue();
                            String size = ""+dataSnapshot.child("size").getValue();
                            String ice = ""+dataSnapshot.child("ice").getValue();
                            String quantity = ""+dataSnapshot.child("quantity").getValue();
                            String totalPrice = ""+dataSnapshot.child("totalPrice").getValue();
                            String status = ""+dataSnapshot.child("status").getValue();
                            Cart cart = new Cart();
                            cart.setCoffeeID(id);
                            cart.setImgCart(imgUrl);
                            cart.setNameCart(name);
                            cart.setSizeCart(size);
                            cart.setIceCart(ice);
                            cart.setQuantityCart(quantity);
                            cart.setTotalPriceCart(totalPrice);
                            cart.setStatus(status);
                            list.add(cart);
                        }
                        adapter = new CoffeeRateAdapter(list,RateActivity.this);
                        rev_coffee_rate.setAdapter(adapter);
                        rev_coffee_rate.setHasFixedSize(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}