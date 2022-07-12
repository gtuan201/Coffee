package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.coffee.R;
import com.example.coffee.fragment.HomeFragment;
import com.example.coffee.fragment.CartFragment;
import com.example.coffee.fragment.NotificationFragment;
import com.example.coffee.fragment.MenuFragment;
import com.example.coffee.fragment.OrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView nav = findViewById(R.id.nav_home);
        nav.setSelectedItemId(R.id.icon_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).addToBackStack("Home").commit();
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.icon_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).addToBackStack("Home").commit();
                        return true;
                    case R.id.icon_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new MenuFragment()).addToBackStack("Search").commit();
                        return true;
                    case R.id.icon_cart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new CartFragment()).addToBackStack("Library").commit();
                        return true;
                    case R.id.icon_order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new OrderFragment()).addToBackStack("Write").commit();
                        return true;
                    case R.id.icon_notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new NotificationFragment()).addToBackStack("Notification").commit();
                        return true;

                }
                return false;
            }
        });
    }
}