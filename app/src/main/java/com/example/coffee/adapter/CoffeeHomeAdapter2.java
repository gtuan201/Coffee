package com.example.coffee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.example.coffee.model.Coffee;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class CoffeeHomeAdapter2 extends RecyclerView.Adapter<CoffeeHomeAdapter2.CoffeeHomeViewHolder2>{


    private final List<Coffee> coffeeList;
    private final Context context;

    public CoffeeHomeAdapter2(List<Coffee> coffeeList, Context context) {
        this.coffeeList = coffeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public CoffeeHomeViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_rev_home2,parent,false);
        return new CoffeeHomeViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeHomeViewHolder2 holder, int position) {
        Coffee coffee = coffeeList.get(position);
        if (coffee == null){
            return;
        }
        holder.name.setText(coffee.getCoffeeName());
        Glide.with(holder.imgView).load(coffee.getUrlImg()).error(R.drawable.error_img).into(holder.imgView);

    }

    @Override
    public int getItemCount() {
        if (coffeeList != null){
            return coffeeList.size();
        }
        return 0;
    }

    public static class CoffeeHomeViewHolder2 extends RecyclerView.ViewHolder {

        private final RoundedImageView imgView;
        private final TextView name;
        public CoffeeHomeViewHolder2(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.imgCoffeeHome2);
            name = itemView.findViewById(R.id.coffee_name2);
        }
    }
}
