package com.example.toyshishop.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.toyshishop.CustomersRecyclerViewAdapter;
import com.example.toyshishop.MainActivity;
import com.example.toyshishop.R;
import com.example.toyshishop.data.repository.CustomersRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;


public class CustomersFragment extends Fragment {
    private CustomersRecyclerViewAdapter adapter;


    public CustomersFragment() {
        // Required empty public constructor
    }

    public void setAdapter(CustomersRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_customers, container, false);

        //Floating button listener
        FloatingActionButton addCustomerFAB = rootView.findViewById(R.id.addCustomerFAB);
        addCustomerFAB.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddCustomerFragment()).addToBackStack(null).commit();
            }
        });

        //call method to set customers to recycler view
        new CustomersRepository(getContext(), this).setCustomersToAdminProvider();

        setHasOptionsMenu(true);


        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //set navigation button to toolbar
        ((MainActivity)getActivity()).setNavigatorButton();

        MenuItem searchItem = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }


}