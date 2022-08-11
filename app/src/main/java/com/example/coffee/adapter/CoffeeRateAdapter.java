package com.example.coffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.example.coffee.activity.RatingActivity;
import com.example.coffee.model.Cart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CoffeeRateAdapter extends RecyclerView.Adapter<CoffeeRateAdapter.CoffeeRateViewHolder>{

    private final List<Cart> list;
    private final Context context;

    public CoffeeRateAdapter(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CoffeeRateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_rate_coffee,parent,false);
        return new CoffeeRateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CoffeeRateViewHolder holder, int position) {
        Cart cart = list.get(position);
        if (cart == null){
            return;
        }
        Glide.with(holder.imgCoffeeRate).load(cart.getImgCart()).error(R.drawable.error_img).into(holder.imgCoffeeRate);
        holder.name.setText(cart.getNameCart());
        holder.size.setText(String.format("Size: %s | ", cart.getSizeCart()));
        holder.ice.setText(String.format("Phần trăm đá: %s", cart.getIceCart()));
        holder.quantity.setText(String.format("Số lượng: %s", cart.getQuantityCart()));
        holder.totalPrice.setText(String.format("Thành tiền: %s VNĐ", cart.getTotalPriceCart()));
        holder.btRating.setOnClickListener(v -> {
            Intent intent = new Intent(context, RatingActivity.class);
            intent.putExtra("id",cart.getCoffeeID());
            intent.putExtra("imgUrl",cart.getImgCart());
            intent.putExtra("name",cart.getNameCart());
            intent.putExtra("size",cart.getSizeCart());
            intent.putExtra("ice",cart.getIceCart());
            intent.putExtra("quantity",cart.getQuantityCart());
            context.startActivities(new Intent[]{intent});
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        Query query =  reference.child(cart.getNameCart()).child("Review").orderByChild("status").equalTo("Đã đánh giá");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isReview = snapshot.exists();
                if (isReview){
                    holder.btRating.setText("Đã đánh giá");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public static class CoffeeRateViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCoffeeRate;
        TextView name,size,ice,quantity,totalPrice;
        AppCompatButton btRating;
        public CoffeeRateViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCoffeeRate = itemView.findViewById(R.id.imgCoffeeRate);
            name = itemView.findViewById(R.id.nameRate);
            size = itemView.findViewById(R.id.sizeRate);
            ice = itemView.findViewById(R.id.iceRate);
            quantity = itemView.findViewById(R.id.quantityRate);
            totalPrice = itemView.findViewById(R.id.priceRate);
            btRating = itemView.findViewById(R.id.btRating);
        }
    }
}
