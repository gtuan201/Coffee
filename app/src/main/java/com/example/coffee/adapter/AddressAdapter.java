package com.example.coffee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee.R;
import com.example.coffee.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    private final List<Address> addressList;
    private final Context context;

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
        holder.nameAddress.setText(String.format("%s | ", address.getNameAddress()));
        holder.phoneAddress.setText(address.getPhoneAddress());
        holder.tvAddress.setText(address.getAddress());
        if (address.getIsDefault().equals("Yes")) {
            holder.tvIsDefault.setText("Mặc định");
        }
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
