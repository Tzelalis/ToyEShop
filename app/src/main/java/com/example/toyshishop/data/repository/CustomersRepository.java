package com.example.toyshishop.data.repository;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.ActionMode;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.toyshishop.CustomersRecyclerViewAdapter;
import com.example.toyshishop.R;
import com.example.toyshishop.data.EshopDatabase;
import com.example.toyshishop.data.entities.Customers;
import com.example.toyshishop.ui.fragments.CustomersFragment;
import com.example.toyshishop.ui.fragments.SpecificAdminCustomerFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

public class CustomersRepository {
    private EshopDatabase database;
    private String DB_NAME = "db_eshop";
    private Context context;
    private CustomersFragment customersFragment;
    private ActionMode mode;

    public CustomersRepository(Context context) {
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.context = context;
    }

    public CustomersRepository(Context context, CustomersFragment customersFragment) {
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.context = context;
        this.customersFragment = customersFragment;
    }

    //get action mode to close contextual bar when update ends
    public CustomersRepository(Context context, ActionMode mode) {
        database = Room.databaseBuilder(context, EshopDatabase.class, DB_NAME).build();
        this.context = context;
        this.mode = mode;
    }


    public void insertCustomer(Customers customer) {
        new InsertCustomersAsync().execute(customer);
    }

    public void setCustomersToAdminProvider() {
        new SetCustomersToAdminAsync().execute();
    }

    public void setCustomersToCartProvider() {
        new SetCustomersToCartAsync().execute();
    }

    public Customers fetchCustomer(int id, Activity activity) {
        return database.customersDao().fetchCustomer(17);
    }

    public void deleteCustomer(Customers customer) {
        new DeleteCustomerAsync().execute(customer);
    }

    public void updateCustomer(Customers customer) {
        new UpdateCustomerAsync().execute(customer);
    }

    private class InsertCustomersAsync extends AsyncTask<Customers, Void, Void> {

        @Override
        protected Void doInBackground(Customers... customers) {
            database.customersDao().insertCustomer(customers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomersFragment()).addToBackStack(null).commit();
        }
    }

    private class DeleteCustomerAsync extends AsyncTask<Customers, Void, Void> {

        @Override
        protected Void doInBackground(Customers... customers) {
            database.customersDao().deleteCustomer(customers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomersFragment()).addToBackStack(null).commit();
        }
    }

    private class SetCustomersToAdminAsync extends AsyncTask<Customers, Void, CustomersRecyclerViewAdapter> {

        @Override
        protected CustomersRecyclerViewAdapter doInBackground(Customers... customers) {
            List<Customers> customersList = database.customersDao().fetchAllCustomers();
            ArrayList<Customers> customersArrayList = new ArrayList<>(customersList);

            CustomersRecyclerViewAdapter.OnItemClickListener listener = new CustomersRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Customers customer) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SpecificAdminCustomerFragment(customer)).addToBackStack(null).commit();
                }
            };

            //return custom customer adapter for recycler view
            return new CustomersRecyclerViewAdapter(customersArrayList, listener);
        }

        @Override
        protected void onPostExecute(CustomersRecyclerViewAdapter adapter) {
            //set adapter to recycler view
            RecyclerView recyclerView = ((FragmentActivity) context).findViewById(R.id.customers_recycling_view);


            //create layout manager and set to recyclerview
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            //divider between items
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            //set adapter to recycler view
            recyclerView.setAdapter(adapter);

            //set adapter to fragment var
            customersFragment.setAdapter(adapter);
        }
    }

    private class SetCustomersToCartAsync extends AsyncTask<Void, Void, ArrayList<Customers>> {

        @Override
        protected ArrayList<Customers> doInBackground(Void... voids) {
            return new ArrayList<>(database.customersDao().fetchAllCustomers());
        }

        @Override
        protected void onPostExecute(ArrayList<Customers> customers) {
            ArrayList<String> customerNamesList = new ArrayList<>();

            for (Customers customer : customers) {
                customerNamesList.add(customer.getTextCustomerId() + " " + customer.getFirstName() + " " + customer.getLastName());
            }

            Spinner spinner = ((FragmentActivity)context).findViewById(R.id.cartCustomersSpinner);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, customerNamesList);
            spinner.setAdapter(adapter);
        }
    }

    private class UpdateCustomerAsync extends AsyncTask<Customers, Void, Void> {

        @Override
        protected Void doInBackground(Customers... customers) {
            database.customersDao().updateCustomer(customers[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mode.finish();
        }
    }
}
