package com.example.coffee.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.coffee.FilterCoffee;
import com.example.coffee.R;
import com.example.coffee.activity.DetailActivity;
import com.example.coffee.model.Coffee;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {

    public List<Coffee> list,filterCoffeeList;
    private Context context;
    private FilterCoffee filterCoffee;

    public SearchAdapter(List<Coffee> list, List<Coffee> filterCoffeeList, Context context) {
        this.list = list;
        this.filterCoffeeList = filterCoffeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_search,parent,false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Coffee coffee = list.get(position);
        if (coffee == null){
            return;
        }
        Glide.with(holder.imgSearch).load(coffee.getUrlImg()).error(R.drawable.error_img).into(holder.imgSearch);
        holder.nameSearch.setText(coffee.getCoffeeName());
        holder.descriptionSearch.setText(coffee.getCoffeeDescription());
        holder.priceSearch.setText(coffee.getPrice());
        holder.categorySearch.setText(coffee.getCategory());
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

    public static class SearchViewHolder extends RecyclerView.ViewHolder {

        ImageView imgSearch;
        TextView nameSearch,descriptionSearch,priceSearch,categorySearch;
        public SearchViewHolder(@NonNull View view) {
            super(view);
            imgSearch = view.findViewById(R.id.img_search);
            nameSearch = view.findViewById(R.id.name_search);
            descriptionSearch = view.findViewById(R.id.description_search);
            priceSearch = view.findViewById(R.id.priceSearch);
            categorySearch = view.findViewById(R.id.category_search);
        }
    }

    @Override
    public Filter getFilter() {
        if (filterCoffee==null){
            filterCoffee = new FilterCoffee(filterCoffeeList,this);
        }
        return filterCoffee;
    }
}
