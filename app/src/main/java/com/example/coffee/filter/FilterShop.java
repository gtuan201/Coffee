package com.example.coffee.filter;

import android.widget.Filter;

import com.example.coffee.adapter.ShopAdapter;
import com.example.coffee.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class FilterShop extends Filter {
    public List<Shop> shopFilterList;
    private final ShopAdapter shopAdapter;

    public FilterShop(List<Shop> shopFilterList, ShopAdapter shopAdapter) {
        this.shopFilterList = shopFilterList;
        this.shopAdapter = shopAdapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length()>0){
            constraint = constraint.toString().toLowerCase();
            List<Shop> list = new ArrayList<>();
            for (int i=0; i < shopFilterList.size();i++){
                if (shopFilterList.get(i).getName().toLowerCase().contains(constraint)){
                    list.add(shopFilterList.get(i));
                }
            }
            results.count = list.size();
            results.values = list;
        }
        else {
            results.count = shopFilterList.size();
            results.values = shopFilterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        shopAdapter.shopList = (List<Shop>) results.values;
        shopAdapter.notifyDataSetChanged();
    }
}
