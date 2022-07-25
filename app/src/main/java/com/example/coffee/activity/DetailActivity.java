package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.example.coffee.adapter.DetailViewPager2Adapter;
import com.example.coffee.model.Coffee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    private ImageView imageViewDetail;
    private ImageView btBackDetail;
    private AppCompatButton btFavorite,btAdd;
    private TextView coffeeName, coffeeCategory, coffeeRate, coffeePrice;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private DetailViewPager2Adapter adapter;
    String name,price,rate,category,background;
    String strQuantity,size = "Nhỏ",ice = "30%",note = "",totalPrice = "";
    int quantityInt,totalPriceInt,priceInt;
    long timestamp = System.currentTimeMillis();
    boolean isInCart = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        imageViewDetail = findViewById(R.id.img_detail);
        btBackDetail = findViewById(R.id.btBackDetail);
        btFavorite = findViewById(R.id.btFavorite);
        btAdd = findViewById(R.id.bt_add_to_cart);
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
                       category = ""+snapshot.child("category").getValue();
                       rate = ""+snapshot.child("rate").getValue();
                       price = ""+snapshot.child("price").getValue();
                       background = ""+snapshot.child("background").getValue();
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
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBottomSheet();
            }
        });
    }

    private void openBottomSheet() {
        @SuppressLint("InflateParams")
        View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet_add_to_cart,null);
        BottomSheetDialog bottomSheetAdd = new BottomSheetDialog(this);
        bottomSheetAdd.setContentView(bottomSheet);
        bottomSheetAdd.show();
        ImageButton btDecrease = bottomSheet.findViewById(R.id.btDecrease);
        ImageButton btIncrease = bottomSheet.findViewById(R.id.btIncrease);
        RadioGroup sizeGroup = bottomSheet.findViewById(R.id.sizeGroup);
        RadioGroup iceGroup = bottomSheet.findViewById(R.id.iceGroup);
        RadioButton btSmallSize = bottomSheet.findViewById(R.id.btSmallSize);
        RadioButton btMediumSize = bottomSheet.findViewById(R.id.btMediumSize);
        RadioButton btBigSize = bottomSheet.findViewById(R.id.btBigSize);
        EditText etQuantity = bottomSheet.findViewById(R.id.etQuantity);
        EditText etNote = bottomSheet.findViewById(R.id.note);
        TextView tvTotalPrice = bottomSheet.findViewById(R.id.tvTotalPriceAddToCart);
        AppCompatButton btAddToCart = bottomSheet.findViewById(R.id.addToCartBottomSheet);
        tvTotalPrice.setText(price);
        btDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strQuantity = etQuantity.getText().toString().trim();
                int quantity = Integer.parseInt(strQuantity);
                if (quantity ==1){
                    return;
                }
                else{
                    quantity = quantity -1;
                }
                strQuantity = String.valueOf(quantity);
                etQuantity.setText(strQuantity);
                displayTotalPrice(btSmallSize,btMediumSize,btBigSize,etQuantity,tvTotalPrice);
            }
        });
        btIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strQuantity = etQuantity.getText().toString().trim();
                int quantity = Integer.parseInt(strQuantity);
                if (quantity <10){
                    quantity += 1;
                }
                else {
                    Toast.makeText(DetailActivity.this,"Vui lòng liên hệ với shop để đặt nhiều hơn",Toast.LENGTH_SHORT).show();
                }
                strQuantity = String.valueOf(quantity);
                etQuantity.setText(strQuantity);
                displayTotalPrice(btSmallSize,btMediumSize,btBigSize,etQuantity,tvTotalPrice);
            }
        });
        sizeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                strQuantity = etQuantity.getText().toString().trim();
                quantityInt = Integer.parseInt(strQuantity);
                priceInt = Integer.parseInt(price);
                switch (checkedId){
                    case R.id.btSmallSize:
                        size = "Nhỏ";
                        totalPriceInt = quantityInt*priceInt;
                        tvTotalPrice.setText(String.valueOf(totalPriceInt));
                        break;
                    case R.id.btMediumSize:
                        size = "Vừa";
                        totalPriceInt = quantityInt*(priceInt+10000);
                        tvTotalPrice.setText(String.valueOf(totalPriceInt));
                        break;
                    case R.id.btBigSize:
                        size = "To";
                        totalPriceInt = quantityInt*(priceInt+15000);
                        tvTotalPrice.setText(String.valueOf(totalPriceInt));
                        break;
                }
            }
        });
        iceGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.bt30ice:
                        ice = "30%";
                        break;
                    case R.id.bt40ice:
                        ice = "40%";
                        break;
                    case R.id.bt50ice:
                        ice = "50%";
                        break;
                }
            }
        });
        btAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheckCart(etQuantity,tvTotalPrice,etNote);
            }
        });
    }

    private void isCheckCart(EditText etQuantity, TextView tvTotalPrice, EditText etNote) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        reference.child(user.getUid()).child("Cart").child(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String strQuantityInCart = ""+snapshot.child("quantity").getValue();
                        String strTotalPriceInCart = ""+snapshot.child("totalPrice").getValue();
                        strQuantity = etQuantity.getText().toString().trim();
                        note = etNote.getText().toString().trim();
                        totalPrice = tvTotalPrice.getText().toString().trim();
                        isInCart = snapshot.exists();
                        if (isInCart){
                            int quantityInCart = Integer.parseInt(strQuantityInCart);
                            quantityInt = Integer.parseInt(strQuantity);
                            int finalQuantityInCart = quantityInCart + quantityInt;
                            strQuantity = String.valueOf(finalQuantityInCart);
                            int totalPriceInCart = Integer.parseInt(strTotalPriceInCart);
                            totalPriceInt = Integer.parseInt(totalPrice);
                            int finalTotalPriceInCart = totalPriceInCart + totalPriceInt;
                            totalPrice = String.valueOf(finalTotalPriceInCart);
                            addToCart();
                        }
                        else {
                            addToCart();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void addToCart() {
        String uid = FirebaseAuth.getInstance().getUid();
        HashMap<String,Object> hashMap = new HashMap<>();
//        hashMap.put("coffeeID",""+timestamp);
        hashMap.put("name",name);
        hashMap.put("quantity",strQuantity);
        hashMap.put("size",size);
        hashMap.put("ice",ice);
        hashMap.put("note",note);
        hashMap.put("totalPrice",totalPrice);
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("User");
        reference.child(uid).child("Cart").child(name)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailActivity.this,"Thêm vào giỏ thành công!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this,"Thêm vào giỏ thất bại!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayTotalPrice(RadioButton btSmallSize, RadioButton btMediumSize, RadioButton btBigSize, EditText etQuantity, TextView tvTotalPrice) {
        strQuantity = etQuantity.getText().toString().trim();
        quantityInt = Integer.parseInt(strQuantity);
        priceInt = Integer.parseInt(price);
        if (btSmallSize.isChecked()){
            totalPriceInt = quantityInt*priceInt;
            tvTotalPrice.setText(String.valueOf(totalPriceInt));
        }
        else if (btMediumSize.isChecked()){
            totalPriceInt = quantityInt*(priceInt+10000);
            tvTotalPrice.setText(String.valueOf(totalPriceInt));
        }
        else if (btBigSize.isChecked()){
            totalPriceInt = quantityInt*(priceInt+15000);
            tvTotalPrice.setText(String.valueOf(totalPriceInt));
        }
    }

    public String getName() {
        return name;
    }

}