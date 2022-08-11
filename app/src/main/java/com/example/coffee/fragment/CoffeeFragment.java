package com.example.coffee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.coffee.R;
import com.example.coffee.adapter.MenuAdapter;
import com.example.coffee.model.Coffee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoffeeFragment extends Fragment {
    private RecyclerView recyclerView;
    private MenuAdapter adapter;
    private List<Coffee> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coffee, container, false);
        recyclerView = view.findViewById(R.id.rev_coffee);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(),3,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(manager);
        list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        reference.orderByChild("category").equalTo("Cafe")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            String imgUrl = ""+dataSnapshot.child("ImgUrl").getValue();
                            String name = ""+dataSnapshot.child("name").getValue();
                            String strPrice = ""+dataSnapshot.child("price").getValue();
                            String strRate = ""+dataSnapshot.child("rate").getValue();
                            Coffee coffee = new Coffee();
                            coffee.setUrlImg(imgUrl);
                            coffee.setCoffeeName(name);
                            coffee.setPrice(strPrice);
                            coffee.setRate(strRate);
                            list.add(coffee);
                        }
                        adapter = new MenuAdapter(list,getContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setHasFixedSize(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return view;
    }
}