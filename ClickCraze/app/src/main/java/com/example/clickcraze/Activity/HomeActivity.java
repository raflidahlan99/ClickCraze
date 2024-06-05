package com.example.clickcraze.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.clickcraze.Fragment.CartFragment;
import com.example.clickcraze.Fragment.HomeFragment;
import com.example.clickcraze.Fragment.ProfileFragment;
import com.example.clickcraze.Fragment.SearchFragment;
import com.example.clickcraze.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        BottomNavigationView bottomNav = findViewById(R.id.bottomnav);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                if (item.getItemId() == R.id.menu_home) {
                    selectedFragment = new HomeFragment();
                } else if (item.getItemId() == R.id.menu_search) {
                    selectedFragment = new SearchFragment();
                } else if (item.getItemId() == R.id.menu_cart) {
                    selectedFragment =new CartFragment();
                } else if (item.getItemId() == R.id.menu_profile) {
                    selectedFragment = new ProfileFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
                return true;
            }
        });
    }
}