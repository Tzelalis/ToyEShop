package com.example.toyshishop.data.repository;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.toyshishop.CartRecyclerViewAdapter;
import com.example.toyshishop.R;
import com.example.toyshishop.SwipeToDeleteCallback;
import com.example.toyshishop.data.EshopDatabase;
import com.example.toyshishop.data.entities.Cart;
import com.example.toyshishop.data.entities.CartAndProduct;
import com.example.toyshishop.ui.fragments.CartFragment;
import com.example.toyshishop.ui.fragments.CartSelectCustomerFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class CartRepository {
    private EshopDatabase database;
    private String DB_NAME = "db_eshop";
    private Context context;
    private CartFragment cartFragment;

    public CartRepository(Context context) {
        this.context = context;
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
    }

    public CartRepository(Context context, CartFragment cartFragment) {
        this.context = context;
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.context = context;
        this.cartFragment = cartFragment;
    }

    public void insertCartItem(Cart cartItem) {
        new InsertCartItemAsync().execute(cartItem);
    }

    public void setCartItemsToCartProvider() {
        new SetCartItemsToCartAsync().execute();
    }

    public void deleteCartProvider(Cart cart) {
        new DeleteCartItemAsync().execute(cart);
    }

    public void updateCartProvider(Cart cart){
        new UpdateCartItemAsync().execute(cart);
    }

    public void CartNextStepProvider(){
        new CartNextStepAsync().execute();
    }

    //insert item on cart
    private class InsertCartItemAsync extends AsyncTask<Cart, Void, Void> {

        @Override
        protected Void doInBackground(Cart... carts) {
            try {
                database.cartDao().insertItem(carts[0]);
            } catch (SQLiteConstraintException e) {
                //call cancel because product exists already on cart
                cancel(true);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(context, "Το προϊόν προστέθηκε στο καλάθι", Toast.LENGTH_SHORT).show();
        }

        //show toast
        @Override
        protected void onCancelled() {
            Toast.makeText(context, "Το προϊόν υπάρχει ήδη στο καλάθι", Toast.LENGTH_SHORT).show();
        }
    }

    private class SetCartItemsToCartAsync extends AsyncTask<Void, Void, CartRecyclerViewAdapter> {

        @Override
        protected CartRecyclerViewAdapter doInBackground(Void... voids) {
            List<CartAndProduct> cartAndProductsList = database.cartAndProductDao().getAllCartWithProducts();

            ArrayList<CartAndProduct> productsArrayList = new ArrayList<>(cartAndProductsList);

            CartRecyclerViewAdapter.OnItemClickListener listener = new CartRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(CartAndProduct cartAndProduct) {

                }
            };


            //return custom cartandproduct adapter for recycler view
            return new CartRecyclerViewAdapter(productsArrayList, listener);
        }


        @Override
        protected void onPostExecute(CartRecyclerViewAdapter adapter) {
            //set adapter to recycler view
            RecyclerView recyclerView = ((FragmentActivity) context).findViewById(R.id.cartRecyclerView);

            //create layout manager and set to recyclerview
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            //set adapter to recycler view
            recyclerView.setAdapter(adapter);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            //swipe left and delete
            ItemTouchHelper itemTouchHelper = new
                    ItemTouchHelper(new SwipeToDeleteCallback(adapter, recyclerView.getContext()));
            itemTouchHelper.attachToRecyclerView(recyclerView);

            //set adapter to fragment var
            cartFragment.setAdapter(adapter);
        }
    }

    private class DeleteCartItemAsync extends AsyncTask<Cart, Void, Void> {

        @Override
        protected Void doInBackground(Cart... carts) {
            database.cartDao().deleteItem(carts[0]);
            return null;
        }
    }

    private class UpdateCartItemAsync extends AsyncTask<Cart, Void, Void> {

        @Override
        protected Void doInBackground(Cart... carts) {
            database.cartDao().updateCart(carts[0]);
            return null;
        }
    }

    private class CartNextStepAsync extends AsyncTask<Void, Void, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            if(database.cartDao().getCartSize() > 0)
                return  true;

            return false;
        }

        @Override
        protected void onPostExecute(Boolean flag) {
            if(flag){
                ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartSelectCustomerFragment()).addToBackStack(null).commit();
                return;
            }

            Toast.makeText(context, "Το καλάθι είναι άδειο. Προσθέστε προϊόντα πριν πάτε στο επόμενο βήμα.", Toast.LENGTH_SHORT).show();
        }
    }
}
