package com.example.coffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.example.coffee.activity.DetailActivity;
import com.example.coffee.model.Coffee;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class CoffeeHomeAdapter extends RecyclerView.Adapter<CoffeeHomeAdapter.CoffeeViewHolder>{
    private final List<Coffee> coffeeList;
    private final Context context;

    public CoffeeHomeAdapter(List<Coffee> coffeeList, Context context) {
        this.coffeeList = coffeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public CoffeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_rev_home,parent,false);
        return new CoffeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeViewHolder holder, int position) {
        Coffee coffee  = coffeeList.get(position);
        if (coffee == null){
            return;
        }
        Glide.with(holder.imageView).load(coffee.getUrlImg()).error(R.drawable.error_img).into(holder.imageView);
        holder.coffeeName.setText(coffee.getCoffeeName());
        holder.coffeeDescription.setText(coffee.getCoffeeDescription());
        holder.coffeeCategory.setText(coffee.getCategory());
        holder.coffeePrice.setText(coffee.getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("name",coffee.getCoffeeName());
                context.startActivities(new Intent[]{intent});
            }
        });

    }


    @Override
    public int getItemCount() {
        if (coffeeList != null){
            return coffeeList.size();
        }
        return 0;
    }

    public static class CoffeeViewHolder extends RecyclerView.ViewHolder {
        private final RoundedImageView imageView;
        private final TextView coffeeName,coffeeDescription,coffeeCategory,coffeePrice;
        public CoffeeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imgCoffeeHome);
            coffeeName = itemView.findViewById(R.id.coffee_name);
            coffeeDescription = itemView.findViewById(R.id.coffee_description);
            coffeeCategory = itemView.findViewById(R.id.coffee_category);
            coffeePrice = itemView.findViewById(R.id.coffee_price);
        }
    }
}
