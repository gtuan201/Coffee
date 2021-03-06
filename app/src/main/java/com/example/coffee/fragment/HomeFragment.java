package com.example.coffee.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.coffee.R;
import com.example.coffee.activity.AdminActivity;
import com.example.coffee.activity.LoginActivity;
import com.example.coffee.activity.MainActivity;
import com.example.coffee.activity.MemberActivity;
import com.example.coffee.activity.RegisterActivity;
import com.example.coffee.adapter.CoffeeHomeAdapter;
import com.example.coffee.adapter.CoffeeHomeAdapter2;
import com.example.coffee.model.Coffee;
import com.example.coffee.model.News;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {


    private CircleImageView avatar;
    private ImageButton btGotoMember;
    private AppCompatButton btLoginRegister;
    private ImageView btSetting;
    private RecyclerView rev_home,rev_home2;
    private CoffeeHomeAdapter adapter;
    private CoffeeHomeAdapter2 adapter2;
    private ImageSlider imageSlider;
    List<Coffee> coffeeList, coffeeList2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        avatar = view.findViewById(R.id.avatar_user);
        btSetting = view.findViewById(R.id.btSetting);
        btGotoMember = view.findViewById(R.id.btGoToInformationMembership);
        btLoginRegister = view.findViewById(R.id.btLogin_SignUp_Home_Page);
        imageSlider = view.findViewById(R.id.slider_news);
        rev_home = view.findViewById(R.id.rev_home);
        rev_home2 = view.findViewById(R.id.rev_home2);
        btGotoMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AdminActivity.class));
            }
        });
        btLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RegisterActivity.class));
            }
        });
        // Hi???n th??? d??? li???u l??n Recyclerview1
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
                    Coffee coffee = new Coffee();
                    coffee.setId(id);
                    coffee.setUrlImg(imgUrlCoffee);
                    coffee.setCoffeeName(coffeeName);
                    coffee.setCoffeeDescription(description);
                    coffee.setCategory(category);
                    coffee.setPrice(price);
                    coffeeList.add(coffee);
                }
                adapter = new CoffeeHomeAdapter(coffeeList,getContext());
                rev_home.setAdapter(adapter);
                rev_home.setHasFixedSize(true);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //Hi???n th??? d??? li???u l??n RecyclerView2
        coffeeList2 = new ArrayList<>();
        LinearLayoutManager manager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rev_home2.setLayoutManager(manager2);
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Coffee");
        Query query = reference2.orderByChild("category").equalTo("Tr??");
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
        //Hi???n th??? d??? li???u l??n Slider
        addNewsSlider();
        return view;
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