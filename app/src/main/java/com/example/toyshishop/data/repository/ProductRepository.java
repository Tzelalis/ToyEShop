package com.example.toyshishop.data.repository;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.view.ActionMode;

import com.example.toyshishop.ProductsAdminRecyclerViewAdapter;
import com.example.toyshishop.ProductsRecyclerViewAdapter;
import com.example.toyshishop.R;
import com.example.toyshishop.data.EshopDatabase;
import com.example.toyshishop.data.entities.Products;
import com.example.toyshishop.ui.fragments.ProductFragment;
import com.example.toyshishop.ui.fragments.ProductsAdminFragment;
import com.example.toyshishop.ui.fragments.ShopFragment;
import com.example.toyshishop.ui.fragments.SpecificAdminProductFragment;
import com.example.toyshishop.ui.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class ProductRepository {
    private String DB_NAME = "db_eshop";
    private EshopDatabase database;
    private Context context;
    private ShopFragment productsShopFragment;
    private ProductsAdminFragment productsAdminFragment;
    private ActionMode mode;

    public ProductRepository(Context context) {
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.context = context;
    }

    public ProductRepository(Context context, ShopFragment frag) {
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.context = context;
        this.productsShopFragment = frag;
    }

    public ProductRepository(Context context, ProductsAdminFragment frag) {
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.context = context;
        this.productsAdminFragment = frag;
    }

    public ProductRepository(Context context, ActionMode mode) {
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.context = context;
        this.mode = mode;
    }

    public void insertProduct(Products product){
        new insertProductAsync().execute(product);
    }

    public void setProductsToAdminProvider(){
       new SetProductsToAdminAsync().execute();
    }

    public void setProductsByCategoryToShopProvider(int id){
        new SetProductsByCategoryToShopAsync().execute(id);
    }

    public void updateProductProvider(Products product){
        new UpdateProductAsync().execute(product);
    }

    public void deleteProduct(Products product){
        new deleteProductAsync().execute(product);
    }

    private class insertProductAsync extends AsyncTask<Products, Void, Void>{
        @Override
        protected Void doInBackground(Products... products) {
            database.productsDao().insertProduct(products[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductsAdminFragment()).addToBackStack(null).commit();
        }
    }

    private class deleteProductAsync extends AsyncTask<Products, Void, Void>{
        @Override
        protected Void doInBackground(Products... products) {
            database.productsDao().deleteProduct(products[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductsAdminFragment()).addToBackStack(null).commit();
        }
    }

    private class SetProductsByCategoryToShopAsync extends AsyncTask<Integer, Void, ProductsRecyclerViewAdapter>{

        @Override
        protected ProductsRecyclerViewAdapter doInBackground(Integer... id) {
            //get products from database
            List<Products> list = database.productsDao().fetchProductsByCategory(id[0]);
            ArrayList<Products> productsArrayList = new ArrayList<>(list);

            //create listener for recycler view
            ProductsRecyclerViewAdapter.OnItemClickListener listener = new ProductsRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Products item) {
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProductFragment(item)).addToBackStack(null).commit();
                }
            };

            //Create adapter and set as parameters product items and the listener we just created
            return new ProductsRecyclerViewAdapter(productsArrayList, listener);
        }

        @Override
        protected void onPostExecute(ProductsRecyclerViewAdapter adapter) {
            RecyclerView recyclerView = ((FragmentActivity) context).findViewById(R.id.products_recycler_view);

            //Set adapter to recyclerView
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);

            //set recycler view columns with orientation and give stantard space between items
            int spanCount;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                spanCount = 2; // columns
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                spanCount = 3; // columns
            }
            recyclerView.setAdapter(adapter);

            int spacing = 15; // dp
            spacing = Math.round(spacing * context.getResources().getDisplayMetrics().density);  //convert dp to px cause method need px input
            boolean includeEdge = true;
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            productsShopFragment.setAdapter(adapter);
        }
    }

    private class SetProductsToAdminAsync extends AsyncTask<Void, Void, ProductsAdminRecyclerViewAdapter>{

        @Override
        protected ProductsAdminRecyclerViewAdapter doInBackground(Void... voids) {
            List<Products> productsList = database.productsDao().fetchAllProducts();
            ArrayList<Products> productsArrayList = new ArrayList<>(productsList);

            //create listener for recycler view
            ProductsAdminRecyclerViewAdapter.OnItemClickListener listener = new ProductsAdminRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Products item) {
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SpecificAdminProductFragment(item)).addToBackStack(null).commit();
                }
            };

            //return adapter
            return new ProductsAdminRecyclerViewAdapter(productsArrayList, listener);
        }

        @Override
        protected void onPostExecute(ProductsAdminRecyclerViewAdapter adapter) {
            RecyclerView recyclerView = ((FragmentActivity)context).findViewById(R.id.products_admin_recycler_view);

            //create linear layout manager and set to recycler view
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            //divider between items
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            //set adapter to recycler view
            recyclerView.setAdapter(adapter);
            FragmentActivity fragmentActivity = ((FragmentActivity)context);
            productsAdminFragment.setAdapter(adapter);
        }
    }

    private class UpdateProductAsync extends  AsyncTask<Products, Void, Void>{

        @Override
        protected Void doInBackground(Products... products) {
            database.productsDao().updateProduct(products[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mode.finish();  //finish actionmode when update end
        }
    }
}
