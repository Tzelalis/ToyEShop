package com.example.toyshishop.ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.toyshishop.FakeCartRecyclerViewAdapter;
import com.example.toyshishop.MainActivity;
import com.example.toyshishop.R;
import com.example.toyshishop.data.asyncParams.SpecificOrderAsyncTaskParams;
import com.example.toyshishop.data.repository.OrderRepository;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SpecificOrderFragment extends Fragment {
    private SpecificOrderAsyncTaskParams specificOrderAsyncTaskParams;
    private FakeCartRecyclerViewAdapter adapter;


    public SpecificOrderFragment(SpecificOrderAsyncTaskParams specificOrderAsyncTaskParams) {
        this.specificOrderAsyncTaskParams = specificOrderAsyncTaskParams;
    }

    public void setAdapter(FakeCartRecyclerViewAdapter adapter) {
        this.adapter = adapter;
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
        View rooView = inflater.inflate(R.layout.fragment_specific_order, container, false);

        setUI(rooView);

        //setRecyclerView
        new OrderRepository(getContext(), this).setSpecificOrderItemsToRecyclerViewProvider(specificOrderAsyncTaskParams);

        return rooView;
    }

    private void setUI(View rootView){
        TextView customerIdTextView = rootView.findViewById(R.id.specificOrderCustomerIdTextView);
        TextView customerLastNameTextView = rootView.findViewById(R.id.specificOrderCustomerLastnameTextView);
        TextView customerFirstNameTextView = rootView.findViewById(R.id.specificOrderCustomerFirstNameTextView);
        TextView customerPhoneTextView = rootView.findViewById(R.id.specificOrderCustomerPhoneTextView);
        TextView totalPriceTextView = rootView.findViewById(R.id.specificOrderTotalPriceTextBox);
        TextView dateTextView = rootView.findViewById(R.id.specificOrderDateTextBox);



        customerIdTextView.setText(specificOrderAsyncTaskParams.getOrdersAndCustomers().getCustomer().getTextCustomerId());
        customerFirstNameTextView.setText(specificOrderAsyncTaskParams.getOrdersAndCustomers().getCustomer().getFirstName());
        customerLastNameTextView.setText(specificOrderAsyncTaskParams.getOrdersAndCustomers().getCustomer().getLastName());
        customerPhoneTextView.setText(specificOrderAsyncTaskParams.getOrdersAndCustomers().getCustomer().getPhoneNumber());
        totalPriceTextView.setText("Συνολικό ποσό: " + specificOrderAsyncTaskParams.getOrdersAndCustomers().getOrder().getTotalPrice() + "€");

        long timestamp = specificOrderAsyncTaskParams.getOrdersAndCustomers().getOrder().getCreatedDate();
        dateTextView.setText(DateFormat.format("EEE, d MMM yyyy HH:mm:ss", new Date(timestamp)).toString());


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.second_toolbar, menu);

        //set back button to toolbar
        ((MainActivity)getActivity()).setNavigtionListener();

        //delete customer after alert dialog accept
        MenuItem deleteItem = menu.findItem(R.id.secondToolbarDeleteButton  );
        deleteItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //show alert to delete forever a customer if user press delete
                AlertDialog dialog =  new MaterialAlertDialogBuilder(getContext())
                        .setTitle(getActivity().getResources().getString(R.string.deleteOrderTitle))
                        .setMessage(getActivity().getResources().getString(R.string.deleteOrderSupportText) + specificOrderAsyncTaskParams.getOrdersAndCustomers().getCustomer().getLastName() +
                                specificOrderAsyncTaskParams.getOrdersAndCustomers().getCustomer().getLastName() + ";")
                        .setPositiveButton(getActivity().getResources().getString(R.string.deleteOrderAcceptButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new OrderRepository(getContext()).deleteOrderProvider(specificOrderAsyncTaskParams.getOrdersAndCustomers().getOrder());
                            }
                        })
                        .setNeutralButton(getActivity().getResources().getString(R.string.deleteOrderCancelButton), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).show();

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getContext().getResources().getColor(R.color.design_default_color_error));   //set delete button color red

                return false;
            }
        });

    }


}
