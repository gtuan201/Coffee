package com.example.coffee.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.coffee.fragment.InformationOrderFragment;
import com.example.coffee.fragment.PurchaseHistoryFragment;

public class TabOrderAdapter extends FragmentStateAdapter {
    public TabOrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new PurchaseHistoryFragment();
            case 0:
            default:
                return new InformationOrderFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
