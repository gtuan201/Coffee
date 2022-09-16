package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.coffee.R;
import com.example.coffee.adapter.FavouriteAdapter;
import com.example.coffee.model.Coffee;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {


    private RecyclerView rev_favourite;
    private List<Coffee> list;
    private FavouriteAdapter adapter;
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        ImageButton btBackFavourite = findViewById(R.id.btBackFavourite);
        rev_favourite = findViewById(R.id.rev_favourite);
        list = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rev_favourite.setLayoutManager(manager);
        DividerItemDecoration decoration = new DividerItemDecoration(FavouriteActivity.this,DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        rev_favourite.addItemDecoration(decoration);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        assert user != null;
        reference.child(user.getUid()).child("Favourite")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String name = ""+dataSnapshot.child("name").getValue();
                            Coffee coffee = new Coffee();
                            coffee.setCoffeeName(name);
                            list.add(coffee);
                        }
                        adapter = new FavouriteAdapter(list,FavouriteActivity.this);
                        rev_favourite.setAdapter(adapter);
                        rev_favourite.setHasFixedSize(true);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        btBackFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}