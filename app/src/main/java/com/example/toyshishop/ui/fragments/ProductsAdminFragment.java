package com.example.toyshishop.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.toyshishop.MainActivity;
import com.example.toyshishop.ProductsAdminRecyclerViewAdapter;
import com.example.toyshishop.R;
import com.example.toyshishop.data.repository.ProductRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

public class ProductsAdminFragment extends Fragment {
    ProductsAdminRecyclerViewAdapter adapter;

    public ProductsAdminFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_products_admin, container, false);

        //set listener to fab button
        FloatingActionButton addProductFAB = rootView.findViewById(R.id.addProductFAB);
        addProductFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddProductFragment()).addToBackStack(null).commit();
            }
        });

        new ProductRepository(getContext(), this).setProductsToAdminProvider();

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //set navigation button to toolbar
        ((MainActivity)getActivity()).setNavigatorButton();

        MenuItem item = menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView searchView = (androidx.appcompat.widget.SearchView) item.getActionView();
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

    public void setAdapter(ProductsAdminRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
}
