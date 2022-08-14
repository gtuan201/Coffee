package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private TextView coffeeName, coffeeCategory, coffeeRate, coffeePrice,tvQuantity;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private DetailViewPager2Adapter adapter;
    String name,price,rate,category,background,img,avg;
    int quantityInt,totalPriceInt,priceInt;
    String strQuantity,size = "Nhỏ",ice = "30%",note = "",totalPrice = "";
    String coffeeID = "";
    boolean isInCart = false, isFavourite = false;
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
        tvQuantity = findViewById(R.id.tvQuantity);
        coffeePrice = findViewById(R.id.tvPrice);
        tabLayout = findViewById(R.id.tabLayout_Detail);
        viewPager2 = findViewById(R.id.view_pager2);
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        coffeeName.setText(name);
        setRatingAverage();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        reference.child(name)
               .addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       img = ""+snapshot.child("ImgUrl").getValue();
                       category = ""+snapshot.child("category").getValue();
                       rate = ""+snapshot.child("rate").getValue();
                       price = ""+snapshot.child("price").getValue();
                       background = ""+snapshot.child("background").getValue();
                       coffeeCategory.setText(category);
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
        isCheckFavourite();
        btFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavourite){
                    removeFavourite();
                }
                else {
                    addFavourite();
                }
                isCheckFavourite();
            }
        });
    }

    private void addFavourite() {
        HashMap<Object,String> hashMap = new HashMap<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        hashMap.put("name",name);
        reference.child(user.getUid()).child("Favourite").child(name)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailActivity.this,"Đã thêm " + name +" vào ưa thích!",Toast.LENGTH_SHORT ).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this,"Thất bại khi thêm vào ưu thích",Toast.LENGTH_SHORT).show();
                    }
                });


    }
    private void removeFavourite() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference.child(user.getUid()).child("Favourite")
                .child(name)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(DetailActivity.this,"Đã xóa "+name+" khỏi ưa thích!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this,"Thất bại!",Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void isCheckFavourite() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference.child(user.getUid()).child("Favourite").child(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        isFavourite = snapshot.exists();
                        if (isFavourite){
                            btFavorite.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.red_favourite,0,0);
                        }
                        else {
                            btFavorite.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.ic_baseline_favorite_border_24,0,0);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void openBottomSheet() {
        @SuppressLint("InflateParams")
        View bottomSheet = getLayoutInflater().inflate(R.layout.bottom_sheet_add_to_cart,null);
        BottomSheetDialog bottomSheetAdd = new BottomSheetDialog(this);
        bottomSheetAdd.setContentView(bottomSheet);
        bottomSheetAdd.show();
        size = "Nhỏ"; ice = "30%";
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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
        reference.child(user.getUid()).child("Cart").child(coffeeID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String strQuantityInCart = ""+snapshot.child("quantity").getValue();
                        String strSizeInCart = ""+snapshot.child("size").getValue();
                        String strIceInCart = ""+snapshot.child("ice").getValue();
                        String strTotalPriceInCart = ""+snapshot.child("totalPrice").getValue();
                        String strName = ""+snapshot.child("name").getValue();
                        strQuantity = etQuantity.getText().toString().trim();
                        note = etNote.getText().toString().trim();
                        totalPrice = tvTotalPrice.getText().toString().trim();
                        isInCart = snapshot.exists();
                        if (isInCart && strIceInCart.equals(ice) && strSizeInCart.equals(size) && strName.equals(name)){
                            int quantityInCart = Integer.parseInt(strQuantityInCart);
                            quantityInt = Integer.parseInt(strQuantity);
                            int finalQuantityInCart = quantityInCart + quantityInt;
                            strQuantity = String.valueOf(finalQuantityInCart);
                            int totalPriceInCart = Integer.parseInt(strTotalPriceInCart);
                            totalPriceInt = Integer.parseInt(totalPrice);
                            int finalTotalPriceInCart = totalPriceInCart + totalPriceInt;
                            totalPrice = String.valueOf(finalTotalPriceInCart);
                            updateCart();
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

    private void updateCart() {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("quantity",strQuantity);
        hashMap.put("size",size);
        hashMap.put("ice",ice);
        hashMap.put("note",note);
        hashMap.put("totalPrice",totalPrice);
        reference.child(uid).child("Cart").child(coffeeID)
                .updateChildren(hashMap)
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


    private void addToCart() {
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
        coffeeID = reference.push().getKey();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("coffeeID",coffeeID);
        hashMap.put("image",img);
        hashMap.put("name",name);
        hashMap.put("quantity",strQuantity);
        hashMap.put("size",size);
        hashMap.put("ice",ice);
        hashMap.put("note",note);
        hashMap.put("totalPrice",totalPrice);
        reference.child(uid).child("Cart").child(coffeeID)
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


    private void setRatingAverage() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        reference.child(name).child("Review")
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint({"SetTextI18n", "DefaultLocale"})
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        long quantityReview = snapshot.getChildrenCount();
                        float sum = 0;
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String strRate = ""+dataSnapshot.child("rate").getValue();
                            sum += Float.parseFloat(strRate);
                        }
                        if (sum ==0){
                            coffeeRate.setText("0");
                            tvQuantity.setText("Chưa có đánh giá");
                        }
                        else {
                            float average = sum/quantityReview;
                            avg = String.format("%.1f",average);
                            coffeeRate.setText(String.format("%s/5", avg));
                            tvQuantity.setText(String.format("%s đánh giá", quantityReview));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public String getName() {
        return name;
    }

}