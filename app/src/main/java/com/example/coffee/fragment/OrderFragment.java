package com.example.coffee.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.coffee.R;
import com.example.coffee.adapter.TabOrderAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class OrderFragment extends Fragment {


    private TabLayout tabOrder;
    private TabOrderAdapter adapter;
    private AppCompatButton btMore;
    private ViewPager2 vpg_order;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        tabOrder = view.findViewById(R.id.tab_order);
        btMore = view.findViewById(R.id.more_information);
        vpg_order = view.findViewById(R.id.vpg_order);
        adapter = new TabOrderAdapter(getActivity());
        vpg_order.setAdapter(adapter);
        new TabLayoutMediator(tabOrder, vpg_order, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Thông tin đơn hàng");
                        break;
                    case 1:
                        tab.setText("Lịch sử mua hàng");
                        break;
                }
            }
        }).attach();
        btMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpBox();
            }
        });
        return view;
    }

    private void openPopUpBox() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_box);
        Window window = dialog.getWindow();
        if (window == null) return;
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);
        dialog.show();
    }
}