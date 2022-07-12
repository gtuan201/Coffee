package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.example.coffee.adapter.DetailViewPager2Adapter;
import com.example.coffee.model.Coffee;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageViewDetail;
    private ImageView btBackDetail;
    private AppCompatButton btFavorite;
    private TextView coffeeName, coffeeCategory, coffeeRate, coffeePrice;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private DetailViewPager2Adapter adapter;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageViewDetail = findViewById(R.id.img_detail);
        btBackDetail = findViewById(R.id.btBackDetail);
        btFavorite = findViewById(R.id.btFavorite);
        coffeeName = findViewById(R.id.tvCoffeeName);
        coffeeCategory = findViewById(R.id.tvCategory);
        coffeeRate = findViewById(R.id.tvRate);
        coffeePrice = findViewById(R.id.tvPrice);
        tabLayout = findViewById(R.id.tabLayout_Detail);
        viewPager2 = findViewById(R.id.view_pager2);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        coffeeName.setText(name);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        reference.child(name)
               .addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       String category = ""+snapshot.child("category").getValue();
                       String rate = ""+snapshot.child("rate").getValue();
                       String price = ""+snapshot.child("price").getValue();
                       String background = ""+snapshot.child("background").getValue();
                       coffeeCategory.setText(category);
                       coffeeRate.setText(rate);
                       coffeePrice.setText(price);
                       Glide.with(imageViewDetail).load(background).into(imageViewDetail);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
        adapter = new DetailViewPager2Adapter(this);
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Mô tả");
                    break;
                case 1:
                    tab.setText("Đánh giá");
                    break;
            }
        }).attach();
        btBackDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    public String getName() {
        return name;
    }

}