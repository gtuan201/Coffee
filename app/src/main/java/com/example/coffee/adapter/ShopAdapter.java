package com.example.coffee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee.filter.FilterShop;
import com.example.coffee.OnItemClickListener;
import com.example.coffee.R;
import com.example.coffee.model.Shop;

import java.util.List;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ShopViewHolder> implements Filterable {


    public List<Shop> shopList,shopFilterList;
    private Context context;
    private OnItemClickListener listener;
    private FilterShop filterShop;

    public ShopAdapter(List<Shop> shopList, List<Shop> shopFilterList, Context context, OnItemClickListener listener) {
        this.shopList = shopList;
        this.shopFilterList = shopFilterList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_shop,parent,false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, int position) {
        Shop shop = shopList.get(position);
        if (shop==null){
            return;
        }
        Glide.with(holder.imgShop).load(shop.getImgUrlShop()).error(R.drawable.error_img).into(holder.imgShop);
        holder.addressShop.setText(shop.getAddress());
        holder.nameShop.setText(shop.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClickItem(shop);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (shopList != null){
            return shopList.size();
        }
        return 0;
    }

    public static class ShopViewHolder extends RecyclerView.ViewHolder {
        ImageView imgShop;
        TextView addressShop,nameShop;
        public ShopViewHolder(@NonNull View view) {
            super(view);
            imgShop = view.findViewById(R.id.img_shop_bottom_sheet);
            addressShop = view.findViewById(R.id.address_shop_bottom_sheet);
            nameShop = view.findViewById(R.id.name_shop_bottom_sheet);
        }
    }
    @Override
    public Filter getFilter() {
        if (filterShop==null){
            filterShop = new FilterShop(shopFilterList,this);
        }
        return filterShop;
    }

}
