package com.example.coffee;

import android.widget.Filter;

import com.example.coffee.adapter.SearchAdapter;
import com.example.coffee.model.Coffee;

import java.util.ArrayList;
import java.util.List;

public class FilterCoffee extends Filter {
    public List<Coffee> filterCoffeeList;
    private SearchAdapter adapter;

    public FilterCoffee(List<Coffee> filterCoffeeList, SearchAdapter adapter) {
        this.filterCoffeeList = filterCoffeeList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length()>0){
            constraint = constraint.toString().toLowerCase();
            List<Coffee> list = new ArrayList<>();
            for (int i =0; i<filterCoffeeList.size();i++){
                if (filterCoffeeList.get(i).getCoffeeName().toLowerCase().contains(constraint)){
                    list.add(filterCoffeeList.get(i));
                }
            }
            results.count = list.size();
            results.values = list;
        }
        else {
            results.count = filterCoffeeList.size();
            results.values = filterCoffeeList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.list = (List<Coffee>) results.values;
        adapter.notifyDataSetChanged();
    }
}
