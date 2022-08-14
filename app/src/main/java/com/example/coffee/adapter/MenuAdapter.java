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
import com.example.coffee.model.Coffee;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder>{


    private List<Coffee> list;
    private Context context;

    public MenuAdapter(List<Coffee> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_menu,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        Coffee coffee = list.get(position);
        if ((coffee == null)){
            return;
        }
        holder.nameMenu.setText(coffee.getCoffeeName());
        holder.priceMenu.setText(coffee.getPrice());
        if (coffee.getRate().equals("0.0") || coffee.getRate().equals("")){
            holder.ratingMenu.setText("0");
        }
        else {
            holder.ratingMenu.setText(coffee.getRate());
        }
        Glide.with(holder.imgMenu).load(coffee.getUrlImg()).error(R.drawable.error_img).into(holder.imgMenu);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name",coffee.getCoffeeName());
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

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMenu;
        TextView nameMenu,priceMenu,ratingMenu;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMenu = itemView.findViewById(R.id.img_menu);
            nameMenu = itemView.findViewById(R.id.nameMenu);
            priceMenu = itemView.findViewById(R.id.priceMenu);
            ratingMenu = itemView.findViewById(R.id.tvRatingMenu);
        }
    }
}
