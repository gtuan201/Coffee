package com.example.coffee.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.YuvImage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee.R;
import com.example.coffee.model.Address;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    private final List<Address> addressList;
    private final Context context;
    String id,isDefault;

    public AddressAdapter(List<Address> addressList, Context context) {
        this.addressList = addressList;
        this.context = context;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_address,parent,false);
        return new AddressViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = addressList.get(position);
        if (address == null){
            return;
        }
        final String currentId = address.getId();
        holder.nameAddress.setText(String.format("%s | ", address.getNameAddress()));
        holder.phoneAddress.setText(address.getPhoneAddress());
        holder.tvAddress.setText(address.getAddress());
        if (address.getIsDefault().equals("Yes")) {
            holder.tvIsDefault.setText("Mặc định");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Chọn làm địa chỉ mặc định?")
                        .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setDefault(address,dialog,currentId);
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    private void setDefault(Address address, DialogInterface dialog, String currentId) {
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("isDefault","Yes");
        HashMap<String,Object> map = new HashMap<>();
        map.put("isDefault","No");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            reference.child(user.getUid()).child("Address").child(address.getId())
                    .updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            dialog.cancel();
                        }
                    });
    }

    @Override
    public int getItemCount() {
        if (addressList != null){
            return addressList.size();
        }
        return 0;
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView nameAddress,phoneAddress,tvAddress,tvIsDefault;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            nameAddress = itemView.findViewById(R.id.nameAddress);
            phoneAddress = itemView.findViewById(R.id.phoneAddress);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvIsDefault = itemView.findViewById(R.id.tvIsDefault);
        }
    }
}
