package com.example.toyshishop.data.repository;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toyshishop.FakeCartRecyclerViewAdapter;
import com.example.toyshishop.OrderRecyclerViewAdapter;
import com.example.toyshishop.R;
import com.example.toyshishop.data.EshopDatabase;
import com.example.toyshishop.data.asyncParams.SpecificOrderAsyncTaskParams;
import com.example.toyshishop.data.entities.Cart;
import com.example.toyshishop.data.entities.CartAndProduct;
import com.example.toyshishop.data.entities.OrderItemAndProduct;
import com.example.toyshishop.data.entities.OrderItems;
import com.example.toyshishop.data.entities.Orders;
import com.example.toyshishop.data.entities.OrdersAndCustomers;
import com.example.toyshishop.data.entities.Products;
import com.example.toyshishop.ui.fragments.OrdersFragment;
import com.example.toyshishop.ui.fragments.SpecificOrderFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class OrderRepository {

    private EshopDatabase database;
    private String DB_NAME = "db_eshop";
    private Context context;
    private OrdersFragment ordersFragment;
    private SpecificOrderFragment specificOrderFragment;

    public OrderRepository(Context context) {
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.context = context;
    }

    public OrderRepository(Context context, SpecificOrderFragment specificOrderFragment) {
        this.context = context;
        this.specificOrderFragment = specificOrderFragment;
    }

    public OrderRepository(Context context, OrdersFragment ordersFragment) {
        this.context = context;
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.ordersFragment = ordersFragment;
    }

    public void addOrderProvider(int customerId) {
        new addOrderAsync().execute(customerId);
    }

    public void setOrdersAndCustomersToOrdersRecyclerViewProvider(){
        new SetOrdersAndCustomersToOrdersRecyclerViewAsync().execute();
    }

    public void setProductTotalSalesProvider(int productId){
        new SetProductTotalSalesAsync().execute(productId);
    }

    public void deleteOrderProvider(Orders order){
        new DeleteOrderAsync().execute(order);
    }

    public void setTotalSalesToTextViewProvider(){
        new SetTotalSalesToTextViewAsync().execute();
    }

    public void setSpecificOrderItemsToRecyclerViewProvider(SpecificOrderAsyncTaskParams specificOrderAsyncTaskParams){
        new SetSpecificOrderItemsToRecyclerViewAsync().execute(specificOrderAsyncTaskParams);
    }

    private class addOrderAsync extends AsyncTask<Integer, Void , Void> {


        @Override
        protected Void doInBackground(Integer... customerId) {
            ArrayList<Cart> cartItems = new ArrayList<Cart>(database.cartDao().fetchCart());

            double totalPrice = 0;
            for(Cart cartItem : cartItems){
                totalPrice += database.productsDao().getProductPriceById(cartItem.getProductId()) * cartItem.getQuantity();
            }

            //insertOrder and get back orderId
            long orderId = database.ordersDao().insertOrder(new Orders(customerId[0], 0, System.currentTimeMillis(), totalPrice));

            //insert orderItems
            for(Cart cartItem : cartItems){
                database.orderItemsDao().insertOrderItem(new OrderItems(orderId, cartItem.getProductId(), cartItem.getQuantity()));

                //update product quantity
                Products product = database.productsDao().fetchProductById(cartItem.getProductId());
                product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                database.productsDao().updateProduct(product);
            }



            database.cartDao().clearCart(); //clear cart

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(context, "Η παραγγελεία ολοκληρώθηκε.", Toast.LENGTH_SHORT).show();
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).commit();
        }
    }

    private class SetOrdersAndCustomersToOrdersRecyclerViewAsync extends AsyncTask<Void, Void, OrderRecyclerViewAdapter>{

        @Override
        protected OrderRecyclerViewAdapter doInBackground(Void... voids) {
            ArrayList<OrdersAndCustomers> ordersAndCustomers = new ArrayList<>(database.ordersAndCustomersDao().fetchAllOrdersAndCustomers());

            OrderRecyclerViewAdapter.OnItemClickListener listener = new OrderRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(OrdersAndCustomers item) {
//                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SpecificOrderFragment()).addToBackStack(null).commit();
                    new SetSpecificOrderAndCustomerAndProducts().execute(item);
                }
            };

            //return adapter
            return new OrderRecyclerViewAdapter(ordersAndCustomers, listener);
        }

        @Override
        protected void onPostExecute(OrderRecyclerViewAdapter adapter) {
            RecyclerView recyclerView = ((FragmentActivity)context).findViewById(R.id.ordersRecyclerView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            //divider between items
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            //set adapter to recycler view
            recyclerView.setAdapter(adapter);

            ordersFragment.setAdapter(adapter);
        }
    }

    private class SetSpecificOrderAndCustomerAndProducts extends AsyncTask<OrdersAndCustomers, Void, SpecificOrderAsyncTaskParams>{

        @Override
        protected SpecificOrderAsyncTaskParams doInBackground(OrdersAndCustomers... ordersAndCustomers) {
            ArrayList<OrderItemAndProduct> orderItemAndProducts = new ArrayList<>(database.orderItemAndProductDao().getOrderItemsAndProductsByOrderId(ordersAndCustomers[0].getOrder().getOrderId()));

            //create SpecificOrderAsyncTaskParams to return 2 objects
            return new SpecificOrderAsyncTaskParams(orderItemAndProducts, ordersAndCustomers[0]);
        }

        @Override
        protected void onPostExecute(SpecificOrderAsyncTaskParams specificOrderAsyncTaskParams) {
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SpecificOrderFragment(specificOrderAsyncTaskParams)).addToBackStack(null).commit();
        }
    }

    private class SetProductTotalSalesAsync extends AsyncTask<Integer, Void, List<Integer>>{
        @Override
        protected List<Integer> doInBackground(Integer... integers) {
            return database.orderItemsDao().getProductTotalSales(integers[0]);      //return quantity per order
        }

        @Override
        protected void onPostExecute(List<Integer> ordersQuantity) {
            int totalSales = 0;
            for(int quantity : ordersQuantity){
                totalSales += quantity;
            }

            TextView totalSalesTextView = ((FragmentActivity)context).findViewById(R.id.productTotalSalesTextView);
            totalSalesTextView.setText("Συνολικές πωλήσεις: " + totalSales);
        }
    }

    private class DeleteOrderAsync extends AsyncTask<Orders, Void, Void>{

        @Override
        protected Void doInBackground(Orders... orders) {
            ArrayList<OrderItemAndProduct> orderItemsArrayList = new ArrayList<OrderItemAndProduct>(database.orderItemAndProductDao().getOrderItemsAndProductsByOrderId(orders[0].getOrderId()));

            //add products quantity from order with delete
            for(OrderItemAndProduct orderItemAndProduct : orderItemsArrayList){
                orderItemAndProduct.getProduct().setQuantity(orderItemAndProduct.getProduct().getQuantity() + orderItemAndProduct.getOrderItems().getQuantity());
                database.productsDao().updateProduct(orderItemAndProduct.getProduct());
            }

            database.ordersDao().deleteOrder(orders[0]);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrdersFragment()).addToBackStack(null).commit();
        }
    }

    public class SetTotalSalesToTextViewAsync extends AsyncTask<Void, Void, ArrayList<Integer>>{

        @Override
        protected ArrayList<Integer> doInBackground(Void... voids) {
            return new ArrayList<Integer>(database.orderItemsDao().getTotalOrderItemsQuantities());        //get total orders from all products
        }

        @Override
        protected void onPostExecute(ArrayList<Integer> quantities) {
            int totalSales = 0;
            for(int quantity : quantities){
                totalSales += quantity;
            }

            ((TextView)((FragmentActivity)context).findViewById(R.id.totalSalesTextView)).setText("Συνολικές πωλήσεις: " + totalSales); //set to totalSalesTextView total sales of all products (orderitems)
        }
    }

    //reuse cartrecycler view to don't create new, same laoyout/samerecyclerview
    private class SetSpecificOrderItemsToRecyclerViewAsync extends AsyncTask<SpecificOrderAsyncTaskParams, Void , FakeCartRecyclerViewAdapter>{

        @Override
        protected FakeCartRecyclerViewAdapter doInBackground(SpecificOrderAsyncTaskParams... params) {

//                List<OrderItemAndProduct> ordersList = database.orderItemAndProductDao().getOrderItemsAndProductsByOrderId(orders[0].getOrderId());
//
//
//                ArrayList<OrderItemAndProduct> orderItemAndProductsArrayList = new ArrayList<>(ordersList);
//
                ArrayList<CartAndProduct> cartArrayList = new ArrayList<>();
                for(OrderItemAndProduct orderItemAndProduct : params[0].getOrderItemAndProduct()){
                    cartArrayList.add(new CartAndProduct(new Cart(orderItemAndProduct.getOrderItems().getProductId(), orderItemAndProduct.getOrderItems().getQuantity()), orderItemAndProduct.getProduct()));
                }

                ArrayList<CartAndProduct> productsArrayList = new ArrayList<>(cartArrayList);

                FakeCartRecyclerViewAdapter.OnItemClickListener listener = new FakeCartRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(CartAndProduct cartAndProduct) {

                    }
                };



                //return custom cartandproduct adapter for recycler view
                return new FakeCartRecyclerViewAdapter(productsArrayList, listener);
        }

        @Override
        protected void onPostExecute(FakeCartRecyclerViewAdapter adapter) {
            //set adapter to recycler view
            RecyclerView recyclerView = ((FragmentActivity) context).findViewById(R.id.specificOrderRecyclerView);

            //create layout manager and set to recyclerview
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            //divider between items
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            //set adapter to recycler view
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
    }
}
