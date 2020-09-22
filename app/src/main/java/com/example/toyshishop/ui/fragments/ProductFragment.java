package com.example.toyshishop.ui.fragments;

import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toyshishop.MainActivity;
import com.example.toyshishop.R;
import com.example.toyshishop.data.entities.Cart;
import com.example.toyshishop.data.entities.Products;
import com.example.toyshishop.data.repository.CartRepository;
import com.example.toyshishop.data.repository.OrderRepository;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class ProductFragment extends Fragment {
    protected Products product;
    protected View rootView;


    public ProductFragment() {
        // Required empty public constructor
    }

    public ProductFragment(Products product){
        this.product = product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_product, container, false);

        setHasOptionsMenu(true);

        setUI();    //setup UI
        new OrderRepository(getContext()).setProductTotalSalesProvider(product.getProductId());

        MaterialButton removeButton = rootView.findViewById(R.id.removeCartQuantityButton);
        MaterialButton addButton = rootView.findViewById(R.id.addCartQuantityButton);
        final EditText quantityTextView = rootView.findViewById(R.id.orderQuantityEditText);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!quantityTextView.getText().toString().isEmpty()) {
                    String countEditText = quantityTextView.getText().toString();
                    int orderCount = Integer.parseInt(countEditText);
                        quantityTextView.setText(String.valueOf(orderCount - 1));
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int orderCount;

                if (!quantityTextView.getText().toString().isEmpty()) {
                    String countEditText = quantityTextView.getText().toString();
                    orderCount = Integer.parseInt(countEditText);
                    quantityTextView.setText(String.valueOf(orderCount + 1));
                }
            }
        });

        quantityTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int num;
                try {
                    num = Integer.parseInt(s.toString());
                }catch (NumberFormatException e){
                    return;
                }

                if(product.getQuantity() == 0){
                    quantityTextView.setText("1");
                    return;
                }

               if(num < 1){
                   quantityTextView.setText("1");
               }else if(num > product.getQuantity()){
                   quantityTextView.setText(String.valueOf(product.getQuantity()));
               }

               ((TextView)rootView.findViewById(R.id.productTotalPriceTextView)).setText("Τελική τιμή: " + Integer.parseInt(quantityTextView.getText().toString()) * product.getPrice() + "€");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //set back button to toolbar
        ((MainActivity)getActivity()).setNavigtionListener();

        menu.clear();
        inflater.inflate(R.menu.add_toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.addToolbarMenuAddItem);
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                     TextView quantityTextView = rootView.findViewById(R.id.orderQuantityEditText);
                     try{
                         //check if quantity is valid integer
                         int quantity = Integer.parseInt(quantityTextView.getText().toString());

                         //if product quantity is 0 the don't add to cart
                         if(product.getQuantity() == 0){
                             Toast.makeText(getContext(), "Το προϊόν δεν είναι διαθέσιμο.", Toast.LENGTH_SHORT).show();
                             return false;
                         }

                         //check if quantity is not less than 1
                         if(quantity < 1)
                             throw new NumberFormatException();

                         //add product to car
                         new CartRepository(getContext()).insertCartItem(new Cart(product.getProductId(), quantity));
                         getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).addToBackStack(null).commit();

                     }catch (NumberFormatException e){
                         Toast.makeText(getContext(), "Συμπληρώστε τη ποσότητα σωστά. Η ποσότητα δε μπορεί να είναι 0.", Toast.LENGTH_SHORT).show();
                         return false;
                     }
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setUI(){
        TextView nameTextView = rootView.findViewById(R.id.productFragmentNameTextView);
        TextView descriptionTextView = rootView.findViewById(R.id.productFragmentDescriptionTextView);
        TextView priceTextView = rootView.findViewById(R.id.productFragmentPriceTextView);
        TextView oldPriceTextView = rootView.findViewById(R.id.productFragmentOldPriceTextView);
        TextView reserveTextView = rootView.findViewById(R.id.productReserveTextView);
        TextView totalPriceTextView = rootView.findViewById(R.id.productTotalPriceTextView);
        ImageView imageView = rootView.findViewById(R.id.productFragmentImageView);

        nameTextView.setText(product.getProductName());
        descriptionTextView.setText(product.getDescription());
        priceTextView.setText(product.getPrice() + "€");
        reserveTextView.setText("Απόθεμα: " + product.getQuantity());
        totalPriceTextView.setText("Τελική τιμή: " + product.getPrice());
        if(product.getImagePath() != null){
            imageView.setImageURI(Uri.parse(product.getImagePath()));
        }

        if(product.getPrice() < product.getOldPrice()){
            oldPriceTextView.setText(product.getOldPrice() + "€");
            oldPriceTextView.setPaintFlags(oldPriceTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);   // strike the text of oldpriceTextView
        }

    }
}
