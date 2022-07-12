package com.example.coffee.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.coffee.onboarding.Onboarding1;
import com.example.coffee.onboarding.Onboarding2;
import com.example.coffee.onboarding.Onboarding3;

public class OnBoardingViewPagerAdapter extends FragmentStatePagerAdapter {

    public OnBoardingViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1:
                return new Onboarding2();
            case 2:
                return new Onboarding3();
            case 0:
            default:
                return new Onboarding1();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
