package com.example.toyshishop.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.toyshishop.MainActivity;
import com.example.toyshishop.ProductsRecyclerViewAdapter;
import com.example.toyshishop.R;
import com.example.toyshishop.data.repository.ProductRepository;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {
    private ProductsRecyclerViewAdapter adapter;
    private int category;

    public ShopFragment() {
        // Required empty public constructor
    }

    public ShopFragment(int category){
        this.category = category;
    }

    protected int test;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate fragment and save to rootView to return it in the end
        View rootView = inflater.inflate(R.layout.fragment_shop, container, false);

        //set products to recyclerView with Async method
        new ProductRepository(getContext(), this).setProductsByCategoryToShopProvider(category);

        setHasOptionsMenu(true);

        // return rootView
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //set navigation button to toolbar
        ((MainActivity)getActivity()).setNavigatorButton();

        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
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

    public void setAdapter(ProductsRecyclerViewAdapter adapter){
        this.adapter = adapter;
    }
}
