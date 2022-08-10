package com.example.coffee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee.R;
import com.example.coffee.adapter.PurchaseHistoryAdapter;
import com.example.coffee.model.Order;
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

public class PurchaseHistoryFragment extends Fragment {

    private List<Order> list;
    private PurchaseHistoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.purchase_history_fragment, container, false);
        RecyclerView rev_purchase_history = view.findViewById(R.id.rev_purchase_history);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rev_purchase_history.setLayoutManager(manager);
        list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bill");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = reference.child("customer").orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String id = ""+dataSnapshot.child("id").getValue();
                    String name = ""+dataSnapshot.child("fullname").getValue();
                    String sdt = ""+dataSnapshot.child("phone").getValue();
                    String address = ""+dataSnapshot.child("address").getValue();
                    String shopName = ""+dataSnapshot.child("shopName").getValue();
                    String status = ""+dataSnapshot.child("status").getValue();
                    String purchaseMethod = ""+dataSnapshot.child("purchase_method").getValue();
                    String timeComplete = ""+dataSnapshot.child("timeComplete").getValue();
                    Order order = new Order();
                    if (status.equals("Đã hoàn thành")){
                        order.setId(id);
                        order.setPurchaseMethod(purchaseMethod);
                        order.setFullname(name);
                        order.setPhoneNumber(sdt);
                        order.setStatus(status);
                        order.setTimeCompleteOrder(timeComplete);
                        if (purchaseMethod.equals("ship")){
                            order.setAddress(address);
                        }
                        else {
                            order.setAddress(shopName);
                        }
                        list.add(order);
                    }
                }
                adapter = new PurchaseHistoryAdapter(list,getContext());
                rev_purchase_history.setAdapter(adapter);
                rev_purchase_history.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}