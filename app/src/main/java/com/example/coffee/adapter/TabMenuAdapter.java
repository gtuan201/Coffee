package com.example.coffee.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.coffee.fragment.CloudFeeFragment;
import com.example.coffee.fragment.CoffeeFragment;
import com.example.coffee.fragment.MilkTeaFragment;
import com.example.coffee.fragment.TeaFragment;

public class TabMenuAdapter extends FragmentStateAdapter {

    public TabMenuAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new TeaFragment();
            case 2:
                return new MilkTeaFragment();
            case 3:
                return new CloudFeeFragment();
            case 0:
            default:
                return new CoffeeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
