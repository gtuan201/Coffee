package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.coffee.R;
import com.example.coffee.adapter.OrderDetailAdapter;
import com.example.coffee.model.Cart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView test;
    private AppCompatButton btBack;
    private RecyclerView rev_order_detail;
    private OrderDetailAdapter adapter;
    private List<Cart> list;
    private String imgUrl,name,size,ice,quantity,totalPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        btBack = findViewById(R.id.btBackOrderDetail);
        rev_order_detail = findViewById(R.id.rev_order_detail);
        LinearLayoutManager manager = new LinearLayoutManager(OrderDetailActivity.this,LinearLayoutManager.VERTICAL,false);
        rev_order_detail.setLayoutManager(manager);
        test = findViewById(R.id.test);
        list = new ArrayList<>();
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bill");
        reference.child("customer").child(id).child("coffee")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            imgUrl = ""+dataSnapshot.child("imgCart").getValue();
                            name = ""+dataSnapshot.child("nameCart").getValue();
                            size = ""+dataSnapshot.child("sizeCart").getValue();
                            ice = ""+dataSnapshot.child("iceCart").getValue();
                            quantity = ""+dataSnapshot.child("quantityCart").getValue();
                            totalPrice = ""+dataSnapshot.child("totalPriceCart").getValue();
                            Cart cart = new Cart();
                            cart.setImgCart(imgUrl);
                            cart.setNameCart(name);
                            cart.setSizeCart(size);
                            cart.setIceCart(ice);
                            cart.setQuantityCart(quantity);
                            cart.setTotalPriceCart(totalPrice);
                            list.add(cart);
                        }
                        adapter = new OrderDetailAdapter(list);
                        rev_order_detail.setAdapter(adapter);
                        rev_order_detail.setHasFixedSize(true);
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