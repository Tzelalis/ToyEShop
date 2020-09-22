package com.example.toyshishop.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.toyshishop.MainActivity;
import com.example.toyshishop.R;
import com.example.toyshishop.data.entities.Customers;
import com.example.toyshishop.data.repository.CustomersRepository;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class SpecificAdminCustomerFragment extends Fragment {
    private View rootView;
    private Customers customer;
    private MenuItem deleteItem;

    public SpecificAdminCustomerFragment() {
    }

    public SpecificAdminCustomerFragment(Customers customer) {
        this.customer = customer;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_specific_customer, container, false);

        setUIFields();

        return rootView;
    }

    private void setUIFields(){
        TextView firstNameTextView = rootView.findViewById(R.id.editCustomerFirstNameEditText);
        TextView lastNameTextView = rootView.findViewById(R.id.editCustomerLastNameEditText);
        TextView phoneNumberTextView = rootView.findViewById(R.id.editCustomerPhoneNumberEditText);
        TextView emailTextView = rootView.findViewById(R.id.editCustomerEmailEditText);

        firstNameTextView.setText(customer.getFirstName());
        lastNameTextView.setText(customer.getLastName());
        phoneNumberTextView.setText(customer.getPhoneNumber());

        //we do that for right layout on empty email
        if(customer.getEmail().isEmpty())
            emailTextView.setText(" ");
        else
            emailTextView.setText(customer.getEmail());
    }

    //change toolbar menu
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.second_toolbar, menu);

        //set back button to toolbar
        ((MainActivity)getActivity()).setNavigtionListener();

        //delete customer after alert dialog accept
        deleteItem = menu.findItem(R.id.secondToolbarDeleteButton);
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //show alert to delete forever a customer if user press delete
                AlertDialog dialog =  new MaterialAlertDialogBuilder(getContext())
                        .setTitle(getActivity().getResources().getString(R.string.deleteCustomerTitle))
                        .setMessage(getActivity().getResources().getString(R.string.deleteCustomerSupportText) + " " + customer.getFirstName() + " " + customer.getLastName() + ";")
                        .setPositiveButton(getActivity().getResources().getString(R.string.deleteCustomerAcceptButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new CustomersRepository(getContext()).deleteCustomer(customer);
                            }
                        })
                        .setNeutralButton(getActivity().getResources().getString(R.string.deleteCustomerCancelButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.design_default_color_error));   //set delete button color red

                return false;
            }
        });

        //set editItem listener
        MenuItem editItem = menu.findItem(R.id.secondToolbarEditButton);
        editItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                setTextViewsEnable(true);

                getActivity().startActionMode(actionModeCallBack);

                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    //enable action mode when editItem clicked to set new Menu bar
    private ActionMode.Callback actionModeCallBack = new ActionMode.Callback(){

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.accept_contextual_action_mode_menu, menu);

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.accept_contextual_accept_item:
                    if(!checkFieldsValid()){
                        return false;
                    }

                    updateCustomerVars();  //update customer with fields
                    new CustomersRepository(getContext(), mode).updateCustomer(customer);
                    break;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            setUIFields();  //set UI fields with customer infos
            setTextViewsEnable(false);
        }

        //update customer fields and return if it did it
        private void updateCustomerVars(){
            String firstName = ((TextView)rootView.findViewById(R.id.editCustomerFirstNameEditText)).getText().toString();
            String lastName = ((TextView)rootView.findViewById(R.id.editCustomerLastNameEditText)).getText().toString();
            String phoneNumber = ((TextView)rootView.findViewById(R.id.editCustomerPhoneNumberEditText)).getText().toString();
            String email = ((TextView)rootView.findViewById(R.id.editCustomerEmailEditText)).getText().toString();

            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setPhoneNumber(phoneNumber);
            customer.setEmail(email);
        }

        //check validation of fields and setError to fields
        private boolean checkFieldsValid(){
            boolean flag = true;

            String firstName = ((TextView)rootView.findViewById(R.id.editCustomerFirstNameEditText)).getText().toString();
            String lastName = ((TextView)rootView.findViewById(R.id.editCustomerLastNameEditText)).getText().toString();
            String phoneNumber = ((TextView)rootView.findViewById(R.id.editCustomerPhoneNumberEditText)).getText().toString();

            if(firstName.trim().isEmpty()){
                ((TextView)rootView.findViewById(R.id.editCustomerFirstNameEditText)).setError("Συμπληρώστε το όνομα του πελάτη.");
                flag=false;
            }

            if(lastName.trim().isEmpty()){
                ((TextView)rootView.findViewById(R.id.editCustomerLastNameEditText)).setError("Συμπληρώστε το επίθετο του πελάτη.");
                flag=false;
            }

            if(phoneNumber.trim().isEmpty()){
                ((TextView)rootView.findViewById(R.id.editCustomerPhoneNumberEditText)).setError("Συμπληρώστε το τηλέφωνο του πελάτη.");
                flag=false;
            }

            return flag;
        }
    };

    private void setTextViewsEnable(boolean flag){
        TextView firstNameTextView = rootView.findViewById(R.id.editCustomerFirstNameEditText);
        TextView lastNameTextView = rootView.findViewById(R.id.editCustomerLastNameEditText);
        TextView phoneNumberTextView = rootView.findViewById(R.id.editCustomerPhoneNumberEditText);
        TextView emailTextView = rootView.findViewById(R.id.editCustomerEmailEditText);

        firstNameTextView.setEnabled(flag);
        lastNameTextView.setEnabled(flag);
        phoneNumberTextView.setEnabled(flag);

        emailTextView.setEnabled(flag);

        //need to restore the space we add for right layout
        if(emailTextView.toString().equals(" ") && flag)
            emailTextView.setText("");
    }



}
