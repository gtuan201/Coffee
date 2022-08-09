package com.example.coffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.example.coffee.activity.DetailActivity;
import com.example.coffee.model.Cart;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder>{


    private final List<Cart> list;
    private Context context;
    public OrderDetailAdapter(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_order_detail,parent,false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        Cart cart = list.get(position);
        if (cart == null){
            return;
        }
        Glide.with(holder.imgOrderDetail).load(cart.getImgCart()).into(holder.imgOrderDetail);
        holder.nameCoffee.setText(cart.getNameCart());
        holder.priceCoffee.setText(String.format("%s VNĐ", cart.getTotalPriceCart()));
        holder.sizeCoffee.setText(String.format("Size: %s", cart.getSizeCart()));
        holder.iceCoffee.setText(String.format("Đá: %s", cart.getIceCart()));
        holder.quantityCoffee.setText(String.format("Số lượng: %s",cart.getQuantityCart()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name",cart.getNameCart());
                context.startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public static class OrderDetailViewHolder extends RecyclerView.ViewHolder {

        ImageView imgOrderDetail;
        TextView nameCoffee,sizeCoffee,iceCoffee,priceCoffee,quantityCoffee;
        public OrderDetailViewHolder(@NonNull View view) {
            super(view);
            imgOrderDetail = view.findViewById(R.id.img_order_detail);
            nameCoffee = view.findViewById(R.id.name_order_detail);
            sizeCoffee = view.findViewById(R.id.size_order_detail);
            iceCoffee = view.findViewById(R.id.ice_order_detail);
            priceCoffee = view.findViewById(R.id.price_order_detail);
            quantityCoffee = view.findViewById(R.id.quantity_order_detail);
        }
    }
}
