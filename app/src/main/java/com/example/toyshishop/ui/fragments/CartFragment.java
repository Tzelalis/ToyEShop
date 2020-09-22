package com.example.toyshishop.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.toyshishop.CartRecyclerViewAdapter;
import com.example.toyshishop.MainActivity;
import com.example.toyshishop.R;
import com.example.toyshishop.data.repository.CartRepository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {

    private CartRecyclerViewAdapter adapter;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        //set cartitems to recyclerview
        new CartRepository(getContext(), this).setCartItemsToCartProvider();

        //next bottom button
        ((Button)rootView.findViewById(R.id.cartNextButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CartRepository(getContext()).CartNextStepProvider();    //next step if cart is not empty, if cart empty -> show toast
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //set navigation button to toolbar
        ((MainActivity) getActivity()).setNavigatorButton();
    }

    public void setAdapter(CartRecyclerViewAdapter adapter) {
        this.adapter = adapter;
    }
}
