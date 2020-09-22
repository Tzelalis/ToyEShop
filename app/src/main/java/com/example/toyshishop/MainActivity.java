package com.example.toyshishop;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.toyshishop.ui.fragments.AddCustomerFragment;
import com.example.toyshishop.ui.fragments.CartFragment;
import com.example.toyshishop.ui.fragments.CustomersFragment;
import com.example.toyshishop.ui.fragments.OrdersFragment;
import com.example.toyshishop.ui.fragments.ProductFragment;
import com.example.toyshishop.ui.fragments.ProductsAdminFragment;
import com.example.toyshishop.ui.fragments.ShopFragment;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private ShopFragment shopFragment;
    private ProductFragment productFragment;
    private CustomersFragment customersFragment;
    private AddCustomerFragment addCustomerFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_nav_drawer_layout);

        //add custom toolbar
        //we use inflate with menuItemListener and no setSupportActionBar(toolbar) because we want to inflate toolbar with custom menu
        toolbar = findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.main_toolbar_menu);
        setSupportActionBar(toolbar);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return true;
            }
        });

        //hideSoftKeyboard();


        //Navigation Drawer Listener (call onNavigationItemSelected when a item pressed)
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    //on back button pressed if navigation drawer is open close it
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    //Navigation Drawer Listener Method
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.customers_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomersFragment()).addToBackStack(null).commit();
                break;
            case R.id.product_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductsAdminFragment()).addToBackStack(null).commit();
                break;
            case R.id.shisha_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShopFragment(0)).addToBackStack(null).commit();
                break;
            case R.id.flavors_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShopFragment(1)).addToBackStack(null).commit();
                break;
            case R.id.accessories_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShopFragment(2)).addToBackStack(null).commit();
                break;
            case R.id.cart_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).addToBackStack(null).commit();
                break;
            case R.id.orders_nav:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).addToBackStack(null).commit();
                break;
        }

        drawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);

        //add toggle button on toolbar for navigation drawer
        setNavigatorButton();
        return true;
    }

    //need this function to call onOptionsItemSelected on fragments
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return false;
    }

    //add toggle button on toolbar for navigation drawer
    public void setNavigatorButton() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
    }

    //method to set navigation button to back button
    public void setNavigtionListener() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

//    https://stackoverflow.com/questions/4165414/how-to-hide-soft-keyboard-on-android-after-clicking-outside-edittext
//    public static void hideSoftKeyboard() {
//        InputMethodManager inputMethodManager =
//                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(
//                this.getCurrentFocus().getWindowToken(), 0);
//    }
}
