package com.example.coffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.example.coffee.activity.DetailActivity;
import com.example.coffee.model.Cart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private final List<Cart> cartList;
    private Context context;

    public CartAdapter(List<Cart> cartList, Context context) {
        this.cartList = cartList;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        if (cart == null){
            return;
        }
        Glide.with(holder.imgCart).load(cart.getImgCart()).into(holder.imgCart);
        holder.nameCart.setText(cart.getNameCart());
        holder.priceCart.setText(String.format("%s VNĐ", cart.getTotalPriceCart()));
        holder.sizeCart.setText(String.format("Size: %s", cart.getSizeCart()));
        holder.iceCart.setText(String.format("Đá: %s", cart.getIceCart()));
        holder.quantityCart.setText(String.format("Số lượng: %s",cart.getQuantityCart()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("name",cart.getNameCart());
                context.startActivities(new Intent[]{intent});
            }
        });
        holder.btRemoveToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Order");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                reference.child(user.getUid()).child("Cart").child(cart.getCoffeeID())
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context,"Đã xóa "+cart.getNameCart()+" khỏi giỏ hàng",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Lỗi không thể xóa sản phẩm khỏi giỏ hàng",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cartList!=null){
            return cartList.size();
        }
        return 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCart;
        TextView nameCart,priceCart,sizeCart,iceCart,quantityCart;
        ImageButton btRemoveToCart;
        public CartViewHolder(@NonNull View view) {
            super(view);
            imgCart = view.findViewById(R.id.img_cart);
            nameCart = view.findViewById(R.id.name_cart);
            priceCart = view.findViewById(R.id.price_cart);
            sizeCart = view.findViewById(R.id.size_cart);
            iceCart = view.findViewById(R.id.ice_cart);
            quantityCart = view.findViewById(R.id.quantity_cart);
            btRemoveToCart = view.findViewById(R.id.btRemoveToCart);
        }
    }
}
