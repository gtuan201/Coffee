package com.example.coffee.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.coffee.R;
import com.example.coffee.activity.PhotoActivity;
import com.example.coffee.model.Review;

import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>{

    private final List<Review> reviewList;
    private Context context;

    public ReviewAdapter(List<Review> reviewList, Context context) {
        this.reviewList = reviewList;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_review,parent,false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        if (review == null){
            return;
        }
        int [] images = {R.drawable.imgrandom1,R.drawable.imgrandom2,R.drawable.imgrandom3,R.drawable.imgrandom4,R.drawable.imgrandom5};
        if (review.getImgUser().equals("")){
            Random random = new Random();
            holder.imgUser.setImageResource(images[random.nextInt(images.length)]);
        }
        else {
            Glide.with(holder.imgUser).load(review.getImgUser()).into(holder.imgUser);
        }
        holder.nameUser.setText(review.getUserName());
        holder.tv_review.setText(review.getReview());
        float rate = Float.parseFloat(review.getRating());
        holder.ratingBar.setRating(rate);
        Glide.with(holder.imgReview).load(review.getImgReview()).into(holder.imgReview);
        holder.dateTime.setText(String.format("%s %s", review.getDate(), review.getTime()));
        holder.imgReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("url",review.getImgReview());
                context.startActivities(new Intent[]{intent});
            }
        });
    }

    @Override
    public int getItemCount() {
        if (reviewList != null){
            return reviewList.size();
        }
        return 0;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgUser;
        TextView nameUser,tv_review,dateTime;
        AppCompatRatingBar ratingBar;
        ImageView imgReview;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = itemView.findViewById(R.id.imgUserReview);
            nameUser = itemView.findViewById(R.id.nameReview);
            tv_review = itemView.findViewById(R.id.tv_review);
            ratingBar = itemView.findViewById(R.id.rateReview);
            imgReview = itemView.findViewById(R.id.img_review);
            dateTime = itemView.findViewById(R.id.dateTime);
        }
    }
}
