package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.coffee.R;
import com.example.coffee.adapter.SearchAdapter;
import com.example.coffee.model.Coffee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView tvClose;
    private RecyclerView rev_search;
    private List<Coffee> list;
    private SearchAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView = findViewById(R.id.search_coffee);
        tvClose = findViewById(R.id.tvCloseSearchView);
        rev_search = findViewById(R.id.rev_search);
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rev_search.setLayoutManager(manager);
        list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String imgUrl = ""+dataSnapshot.child("ImgUrl").getValue();
                    String name = ""+dataSnapshot.child("name").getValue();
                    String description = ""+dataSnapshot.child("description").getValue();
                    String price = ""+dataSnapshot.child("price").getValue();
                    String category = ""+dataSnapshot.child("category").getValue();
                    Coffee coffee = new Coffee();
                    coffee.setUrlImg(imgUrl);
                    coffee.setCoffeeName(name);
                    coffee.setCoffeeDescription(description);
                    coffee.setPrice(price);
                    coffee.setCategory(category);
                    list.add(coffee);
                }
                adapter = new SearchAdapter(list,list,SearchActivity.this);
                rev_search.setAdapter(adapter);
                rev_search.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}