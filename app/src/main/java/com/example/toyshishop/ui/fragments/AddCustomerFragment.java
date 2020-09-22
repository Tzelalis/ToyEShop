package com.example.toyshishop.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.toyshishop.MainActivity;
import com.example.toyshishop.R;
import com.example.toyshishop.data.entities.Customers;
import com.example.toyshishop.data.repository.CustomersRepository;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


public class AddCustomerFragment extends Fragment {
    View rootView;

    public AddCustomerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fragment has menu
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_add_customer, container, false);


        //add custom toolbar
        //we use inflate with menuItemListener and no setSupportActionBar(toolbar) because we want to inflate toolbar with custom menu
        Toolbar toolbar = rootView.findViewById(R.id.addCustomerToolbar);
        toolbar.setTitle("Προσθήκη Πελάτη");
        toolbar.inflateMenu(R.menu.second_toolbar);

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
                boolean flag = true;    //flag to let insert customer

                //get fields
                TextInputEditText firstNameEditText = rootView.findViewById(R.id.addCustomerFirstNameEditText);
                TextInputEditText lastNameEditText = rootView.findViewById(R.id.addCustomerLastNameEditText);
                TextInputEditText phoneNumberEditText = rootView.findViewById(R.id.addCustomerPhoneNumberEditText);
                TextInputEditText emailEditText = rootView.findViewById(R.id.addCustomerEmailEditText);

                //Check if all necessarily fields are completed
                String firstname = firstNameEditText.getText().toString().trim();
                if (firstname.isEmpty()) {
                    firstNameEditText.setError("Συμπληρώστε το όνομα του πελάτη");
                    flag = false;
                }

                String lastName = lastNameEditText.getText().toString().trim();
                if (lastName.isEmpty()) {
                    lastNameEditText.setError("Συμπληρώστε το επίθετο του πελάτη");
                    flag = false;
                }

                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                if (phoneNumber.isEmpty()) {
                    phoneNumberEditText.setError("Συμπληρώστε το τηλέφωνο του πελάτη");
                    flag = false;
                }

                String email = emailEditText.getText().toString().trim();

                if (flag) {
                    new CustomersRepository(getContext()).insertCustomer(new Customers(firstname, lastName, phoneNumber, email));
                }
                return true;
            }
        });
    }
}
