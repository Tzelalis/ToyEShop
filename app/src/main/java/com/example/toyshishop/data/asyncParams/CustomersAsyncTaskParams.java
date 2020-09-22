package com.example.toyshishop.data.asyncParams;

import android.app.Activity;

import com.example.toyshishop.data.entities.Customers;

public class CustomersAsyncTaskParams {
    private Customers customer;
    private Activity activity;

    public CustomersAsyncTaskParams(Customers customer, Activity activity) {
        this.customer = customer;
        this.activity = activity;
    }

    public Customers getCustomer() {
        return customer;
    }

    public Activity getActivity() {
        return activity;
    }
}