package com.example.coffee.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffee.R;
import com.example.coffee.activity.DetailActivity;
import com.example.coffee.adapter.ReviewAdapter;
import com.example.coffee.model.Review;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {
    private RecyclerView revReview;
    private List<Review> list;
    private ReviewAdapter adapter;
    String imgUser,nameUser,strRate,imgReview,strReview,date,time,strQuantityReview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        revReview = view.findViewById(R.id.rev_review);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        revReview.addItemDecoration(decoration);
        decoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        revReview.setLayoutManager(manager);
        list = new ArrayList<>();
        String name = ((DetailActivity) getActivity()).getName();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Coffee");
        reference.child(name).child("Review")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                            imgUser = ""+dataSnapshot.child("imgUser").getValue();
                            nameUser = ""+dataSnapshot.child("username").getValue();
                            strRate = ""+dataSnapshot.child("rate").getValue();
                            strReview =""+dataSnapshot.child("review").getValue();
                            imgReview = ""+dataSnapshot.child("imgCoffeeReview").getValue();
                            time = ""+dataSnapshot.child("time").getValue();
                            date = ""+dataSnapshot.child("date").getValue();
                            Review review = new Review();
                            review.setImgUser(imgUser);
                            review.setUserName(nameUser);
                            review.setRating(strRate);
                            review.setImgReview(imgReview);
                            review.setReview(strReview);
                            review.setDate(date);
                            review.setTime(time);
                            list.add(review);
                        }
                        adapter = new ReviewAdapter(list, getContext());
                        revReview.setAdapter(adapter);
                        revReview.setHasFixedSize(true);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        return view;
    }
}