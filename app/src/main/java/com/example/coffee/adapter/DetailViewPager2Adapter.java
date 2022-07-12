package com.example.coffee.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.coffee.fragment.CommentFragment;
import com.example.coffee.fragment.DescriptionFragment;

public class DetailViewPager2Adapter extends FragmentStateAdapter {
    public DetailViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new CommentFragment();
            case 0:
            default:
                return new DescriptionFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
