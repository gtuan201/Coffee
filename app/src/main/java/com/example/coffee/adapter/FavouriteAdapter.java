package com.example.coffee.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.example.coffee.R;
import com.example.coffee.model.Coffee;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>{


    private List<Coffee> list;
    private Context context;
    private ViewBinderHelper viewBinderHelper = new ViewBinderHelper();

    public FavouriteAdapter(List<Coffee> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_favourite,parent,false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Coffee coffee = list.get(position);
        if (coffee == null){
            return;
        }
        String name = coffee.getCoffeeName();
        viewBinderHelper.bind(holder.swipeRevealLayout,coffee.getCoffeeName());
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        reference.child(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String name = ""+snapshot.child("name").getValue();
                        String imgUrl = ""+snapshot.child("ImgUrl").getValue();
                        String price = ""+snapshot.child("price").getValue();
                        String category = ""+snapshot.child("category").getValue();
                        coffee.setCoffeeName(name);
                        coffee.setUrlImg(imgUrl);
                        coffee.setPrice(price);
                        coffee.setCategory(category);
                        Glide.with(holder.imgFavourite).load(imgUrl).error(R.drawable.error_img).into(holder.imgFavourite);
                        holder.nameFavourite.setText(name);
                        holder.priceFavourite.setText(price);
                        holder.categoryFavourite.setText(category);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("User");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                reference1.child(user.getUid()).child("Favourite").child(name)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context,"Đã xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"Xóa không thành công!",Toast.LENGTH_SHORT).show();
                            }
                        });
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

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFavourite;
        TextView nameFavourite,priceFavourite,categoryFavourite;
        LinearLayout layout;
        private SwipeRevealLayout swipeRevealLayout;
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFavourite = itemView.findViewById(R.id.imgFavourite);
            nameFavourite = itemView.findViewById(R.id.nameFavourite);
            priceFavourite = itemView.findViewById(R.id.priceFavourite);
            categoryFavourite = itemView.findViewById(R.id.categoryFavourite);
            swipeRevealLayout = itemView.findViewById(R.id.item_favourite);
            layout = itemView.findViewById(R.id.layout_delete);
        }
    }
}
