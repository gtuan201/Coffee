package com.example.coffee.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee.R;
import com.example.coffee.adapter.OrderAdapter;
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

public class InformationOrderFragment extends Fragment {
    private RecyclerView rev_infor_order;
    private OrderAdapter adapter;
    private List<Order> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.information_order_fragment, container, false);
        rev_infor_order = view.findViewById(R.id.rev_infor_order);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rev_infor_order.setLayoutManager(manager);
        list = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bill");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Query query = reference.child("customer").orderByChild("uid").equalTo(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String nameOrder = ""+dataSnapshot.child("fullname").getValue();
                    String phoneOrder = ""+dataSnapshot.child("phone").getValue();
                    String addressOrder = ""+dataSnapshot.child("address").getValue();
                    String shopName = ""+dataSnapshot.child("shopName").getValue();
                    String purchaseMethod = ""+dataSnapshot.child("purchase_method").getValue();
                    String status = ""+dataSnapshot.child("status").getValue();
                    String totalPriceOrder = ""+dataSnapshot.child("totalPrice").getValue();
                    String id = ""+dataSnapshot.child("id").getValue();
                    Order order = new Order();
                    order.setId(id);
                    order.setFullname(nameOrder);
                    order.setPhoneNumber(phoneOrder);
                    order.setTotalprice(totalPriceOrder);
                    order.setStatus(status);
                    order.setPurchaseMethod(purchaseMethod);
                    if (purchaseMethod.equals("ship")){
                        order.setAddress(addressOrder);
                    }
                    else if (purchaseMethod.equals("pick up")){
                        order.setAddress(shopName);
                    }
                    list.add(order);
                }
                adapter = new OrderAdapter(list,getContext());
                rev_infor_order.setAdapter(adapter);
                rev_infor_order.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}