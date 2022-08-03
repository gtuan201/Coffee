package com.example.coffee.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.coffee.R;
import com.example.coffee.activity.FavouriteActivity;
import com.example.coffee.activity.SearchActivity;
import com.example.coffee.adapter.TabMenuAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuFragment extends Fragment {

    private AppCompatButton btFavouriteList,btSearch;
    private TabLayout tabMenu;
    private ViewPager2 viewPager2;
    private TabMenuAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        tabMenu = view.findViewById(R.id.tab_menu);
        btFavouriteList = view.findViewById(R.id.btFavoriteMenu);
        btSearch = view.findViewById(R.id.btSearchMenu);
        viewPager2 = view.findViewById(R.id.vpg_tabmenu);
        adapter = new TabMenuAdapter(getActivity());
        viewPager2.setAdapter(adapter);
        new TabLayoutMediator(tabMenu, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Cà Phê");
                        break;
                    case 1:
                        tab.setText("Trà");
                        break;
                    case 2:
                        tab.setText("Trà Sữa");
                        break;
                    case 3:
                        tab.setText("CloudFee");
                        break;
                }
            }
        }).attach();
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });
        btFavouriteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FavouriteActivity.class));
            }
        });
        return view;
    }

}