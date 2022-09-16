package com.example.coffee.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.coffee.R;
import com.example.coffee.adapter.CoffeeHomeAdapter;
import com.example.coffee.adapter.CoffeeHomeAdapter2;
import com.example.coffee.model.Coffee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private AppCompatButton btMember;
    private TextView levelMember,tvMore;
    private RecyclerView rev_home,rev_home2;
    private CoffeeHomeAdapter adapter;
    private CoffeeHomeAdapter2 adapter2;
    private ImageSlider imageSlider;
    List<Coffee> coffeeList, coffeeList2;
    String strIsMember ="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        btMember = view.findViewById(R.id.btMember);
        levelMember = view.findViewById(R.id.tvLevelMember);
        imageSlider = view.findViewById(R.id.slider_news);
        tvMore = view.findViewById(R.id.tv_more);
        rev_home = view.findViewById(R.id.rev_home);
        rev_home2 = view.findViewById(R.id.rev_home2);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String strLevel = ""+snapshot.child("LevelMember").getValue();
                        String strPointMember = ""+snapshot.child("PointMember").getValue();
                        strIsMember = ""+snapshot.child("isMember").getValue();
                        if (strIsMember.equals("Yes")){
                            levelMember.setText(String.format("Hạng thành viên của bạn là : %s Member", strLevel));
                            btMember.setText(String.format("Điểm thành viên: %s", strPointMember));
                        }
                        if (!strIsMember.equals("Yes")) {
                            btMember.setOnClickListener(v -> {
                                openMemberDialog();
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        // Hiển thị dữ liệu lên Recyclerview1
        coffeeList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rev_home.setLayoutManager(manager);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coffeeList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String imgUrlCoffee = "" + dataSnapshot.child("ImgUrl").getValue();
                    String coffeeName = "" + dataSnapshot.child("name").getValue();
                    String description = "" + dataSnapshot.child("description").getValue();
                    String category = "" + dataSnapshot.child("category").getValue();
                    String price = "" + dataSnapshot.child("price").getValue();
                    String id = "" + dataSnapshot.child("id").getValue();
                    String rate = ""+dataSnapshot.child("rate").getValue();
                    Coffee coffee = new Coffee();
                    float rating;
                    if (rate.equals("")) rating = 0;
                    else {
                        rating = Float.parseFloat(rate);
                    }
                    if (rating >= 4){
                        coffee.setId(id);
                        coffee.setUrlImg(imgUrlCoffee);
                        coffee.setCoffeeName(coffeeName);
                        coffee.setCoffeeDescription(description);
                        coffee.setCategory(category);
                        coffee.setPrice(price);
                        coffeeList.add(coffee);
                    }
                }
                adapter = new CoffeeHomeAdapter(coffeeList,getContext());
                rev_home.setAdapter(adapter);
                rev_home.setHasFixedSize(true);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Hiển thị dữ liệu lên RecyclerView2
        coffeeList2 = new ArrayList<>();
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rev_home2.setLayoutManager(manager2);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Coffee");
        Query query = reference2.limitToLast(6);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                coffeeList2.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String name = ""+ dataSnapshot.child("name").getValue();
                    String imgUrl = "" +dataSnapshot.child("ImgUrl").getValue();
                    Coffee coffee = new Coffee();
                    coffee.setCoffeeName(name);
                    coffee.setUrlImg(imgUrl);
                    coffeeList2.add(coffee);
                }
                adapter2 = new CoffeeHomeAdapter2(coffeeList2,getContext());
                rev_home2.setAdapter(adapter2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Hiển thị dữ liệu lên Slider
        addNewsSlider();
        return view;
    }

    private void isCheckMember() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ref.child(user.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String strLevel = ""+snapshot.child("LevelMember").getValue();
                        String strPointMember = ""+snapshot.child("PointMember").getValue();
                        strIsMember = ""+snapshot.child("isMember").getValue();
                        if (strIsMember.equals("Yes")){
                            levelMember.setText(String.format("Hạng thành viên của bạn là : %s Member", strLevel));
                            btMember.setText(String.format("Điểm thành viên: %s", strPointMember));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void openMemberDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.member_dialog);
        Window window = dialog.getWindow();
        if (window==null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);
        AppCompatButton btCancel = dialog.findViewById(R.id.btCancelMember);
        AppCompatButton btConfirm = dialog.findViewById(R.id.btConfirmMember);
        btCancel.setOnClickListener(v -> dialog.dismiss());
        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("isMember","Yes");
                hashMap.put("LevelMember","Đồng");
                hashMap.put("PointMember","0");
                reference.child(user.getUid())
                        .updateChildren(hashMap)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                isCheckMember();
                                Toast.makeText(getContext(),"Chúc mừng bạn đã là thành viên của Tuna Coffee Shop",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(),"Lỗi ! Vui lòng thử lại",Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
            }
        });
        dialog.show();
    }


    private void addNewsSlider() {
        List<SlideModel> newsList = new ArrayList<>();
        newsList.add(new SlideModel(R.drawable.img_1));
        newsList.add(new SlideModel(R.drawable.img_2));
        newsList.add(new SlideModel(R.drawable.img_3));
        newsList.add(new SlideModel(R.drawable.img_4));
        newsList.add(new SlideModel(R.drawable.img));
        imageSlider.setImageList(newsList,true);
    }

}