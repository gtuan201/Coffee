package com.example.coffee.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.coffee.R;
import com.example.coffee.adapter.OnBoardingViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import me.relex.circleindicator.CircleIndicator;

public class OnBoardingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private TextView skip, next;
    private OnBoardingViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        viewPager = findViewById(R.id.view_pager);
        circleIndicator = findViewById(R.id.circle_indicator);
        skip = findViewById(R.id.tv_skip);
        next = findViewById(R.id.tv_next);
        viewPagerAdapter = new OnBoardingViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        circleIndicator.setViewPager(viewPager);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    startActivity(new Intent(OnBoardingActivity.this,MainActivity.class));
                }
                else {
                    startActivity(new Intent(OnBoardingActivity.this,LoginActivity.class));
                }
                finish();

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() < 2) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });
    }
}