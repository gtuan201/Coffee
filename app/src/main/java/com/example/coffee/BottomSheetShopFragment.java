package com.example.coffee;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee.adapter.ShopAdapter;
import com.example.coffee.model.Shop;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetShopFragment extends BottomSheetDialogFragment {
    private List<Shop> shopList;
    private OnItemClickListener listener;
    private ShopAdapter shopAdapter;

    public BottomSheetShopFragment(List<Shop> shopList, OnItemClickListener listener) {
        this.shopList = shopList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog =(BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_list_shop,null);
        bottomSheetDialog.setContentView(view1);
        RecyclerView rev_list_shop = view1.findViewById(R.id.rev_shop_bottom_sheet);
        SearchView searchView = view1.findViewById(R.id.search_view_shop_bottom_sheet);
        TextView tvClose = view1.findViewById(R.id.tvCloseSheet);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rev_list_shop.setLayoutManager(manager);
        shopList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Shop");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                shopList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    String imgShop = ""+dataSnapshot.child("ImgUrl").getValue();
                    String nameShop = ""+dataSnapshot.child("name").getValue();
                    String addressShop =""+dataSnapshot.child("address").getValue();
                    Shop shop = new Shop();
                    shop.setImgUrlShop(imgShop);
                    shop.setName(nameShop);
                    shop.setAddress(addressShop);
                    shopList.add(shop);
                }
                shopAdapter = new ShopAdapter(shopList,shopList, getContext(), new OnItemClickListener() {
                    @Override
                    public void OnClickItem(Shop shop) {
                        listener.OnClickItem(shop);
                    }
                });
                rev_list_shop.setAdapter(shopAdapter);
                rev_list_shop.setHasFixedSize(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                shopAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                shopAdapter.getFilter().filter(newText);
                return false;
            }
        });
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        return bottomSheetDialog;
    }
}
