package com.example.coffee.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView nav = findViewById(R.id.nav_home);
        nav.setSelectedItemId(R.id.icon_home);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).commit();
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.icon_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new HomeFragment()).commit();
                        return true;
                    case R.id.icon_menu:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new MenuFragment()).commit();
                        return true;
                    case R.id.icon_cart:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new CartFragment()).commit();
                        return true;
                    case R.id.icon_order:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new OrderFragment()).commit();
                        return true;
                    case R.id.icon_notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment,new NotificationFragment()).commit();
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