package com.example.toyshishop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_nav_drawer_layout);

        //add custom toolbar
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        //add toggle button on toolbar for navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Navigation Drawer Listener (call onNavigationItemSelected when a item pressed)
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //default fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShopFragment()).commit();
    }


    //on back button pressed if navigation drawer is open close it
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            super.onBackPressed();
        }
    }

    //Navigation Drawer Listener Method
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Toast.makeText(this, "Fragment: " + item.getItemId(), Toast.LENGTH_SHORT).show();

        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }
}
