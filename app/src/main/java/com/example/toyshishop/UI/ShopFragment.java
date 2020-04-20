package com.example.toyshishop.UI;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.toyshishop.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShopFragment extends Fragment {

    public ShopFragment() {
        // Required empty public constructor
    }

    protected int test;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate fragment and save to rootView to return it in the end
        View rootView = inflater.inflate(R.layout.fragment_shop, container, false);

        ArrayList<Product> productList = new ArrayList<Product>();
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 22, 18));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 18, -1));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 15, 15));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 16, 32));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 22, 18));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 18, -1));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 15, 15));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 16, 32));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 22, 18));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 18, -1));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 15, 15));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 16, 32));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 22, 18));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 18, -1));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 15, 15));
        productList.add(new Product("PAOK", "Λευκή Μπλούζα Nike, S-XL, mplamplampla", "Λευκή Μπλούζα Nike, S-XL", 16, 32));


        //create recycler and set the custom adapter to it
        RecyclerView recyclerView = rootView.findViewById(R.id.products_recycling_view);
        ProductsRecyclerViewAdapter adapter = new ProductsRecyclerViewAdapter(productList);
        recyclerView.setHasFixedSize(true);

        int spanCount;
        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            spanCount = 2; // columns
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            spanCount = 3; // columns
        }
        recyclerView.setAdapter(adapter);

        int spacing = 15; // dp
        spacing = Math.round(spacing * getResources().getDisplayMetrics().density);  //convert dp to px cause method need px input
        boolean includeEdge = true;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));



        // return rootView
        return rootView;
    }
}
