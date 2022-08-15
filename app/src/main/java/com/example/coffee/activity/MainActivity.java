package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.coffee.R;
import com.example.coffee.fragment.HomeFragment;
import com.example.coffee.fragment.CartFragment;
import com.example.coffee.fragment.NotificationFragment;
import com.example.coffee.fragment.MenuFragment;
import com.example.coffee.fragment.OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast toast;
    final Fragment fragment_home = new HomeFragment();
    final Fragment fragment_menu = new MenuFragment();
    final Fragment fragment_cart = new CartFragment();
    final Fragment fragment_order = new OrderFragment();
    final Fragment fragment_notification = new NotificationFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment_home;
    BottomNavigationView nav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav = findViewById(R.id.nav_home);
        fm.beginTransaction().add(R.id.container_fragment,fragment_notification,"5").hide(fragment_notification).commit();
        fm.beginTransaction().add(R.id.container_fragment,fragment_order,"4").hide(fragment_order).commit();
        fm.beginTransaction().add(R.id.container_fragment,fragment_cart,"3").hide(fragment_cart).commit();
        fm.beginTransaction().add(R.id.container_fragment,fragment_menu,"2").hide(fragment_menu).commit();
        fm.beginTransaction().add(R.id.container_fragment,fragment_home,"1").commit();
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.icon_home:
                        fm.beginTransaction().hide(active).show(fragment_home).commit();
                        active = fragment_home;
                        return true;
                    case R.id.icon_menu:
                        fm.beginTransaction().hide(active).show(fragment_menu).commit();
                        active = fragment_menu;
                        return true;
                    case R.id.icon_cart:
                        fm.beginTransaction().hide(active).show(fragment_cart).commit();
                        active = fragment_cart;
                        return true;
                    case R.id.icon_order:
                        fm.beginTransaction().hide(active).show(fragment_order).commit();
                        active = fragment_order;
                        return true;
                    case R.id.icon_notification:
                        fm.beginTransaction().hide(active).show(fragment_notification).commit();
                        active = fragment_notification;
                        return true;

                }
                return false;
            }
        });
    }
    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            toast.cancel();
            getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        }
        else {
            toast = Toast.makeText(MainActivity.this,"Nhấn thoát một lần nữa để đóng ứng dụng",Toast.LENGTH_SHORT);
            toast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}