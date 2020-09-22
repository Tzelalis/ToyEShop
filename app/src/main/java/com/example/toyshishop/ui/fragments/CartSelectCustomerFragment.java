package com.example.toyshishop.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.toyshishop.R;
import com.example.toyshishop.data.repository.CustomersRepository;
import com.example.toyshishop.data.repository.OrderRepository;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartSelectCustomerFragment extends Fragment {

    private ArrayAdapter<String> adapter;

    public CartSelectCustomerFragment() {
    }

    public void setAdapter(ArrayAdapter<String> adapter) {
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_cart_select_customer, container, false);;

        new CustomersRepository(getContext()).setCustomersToCartProvider();





        //order button listener
        ((Button)rootView.findViewById(R.id.cartCompleteButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Spinner spinner = rootView.findViewById(R.id.cartCustomersSpinner);
                String customerText = (String)spinner.getSelectedItem();

                if(customerText == null){
                    Toast.makeText(getContext(), "Προσθέστε πελάτη για να μπορείτε να κάνατε παραγγελεία", Toast.LENGTH_SHORT).show();
                    return;
                }
                String arr[] = customerText.split(" ", 2);

                String customerId = arr[0];

                new OrderRepository(getContext()).addOrderProvider(Integer.parseInt(customerId));
            }
        });

        //back button listner
        ((Button)rootView.findViewById(R.id.cartBackButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).addToBackStack(null).commit();
            }
        });


        return rootView;
    }
}
