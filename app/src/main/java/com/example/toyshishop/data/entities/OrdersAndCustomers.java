package com.example.toyshishop.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders_and_customers")
public class OrdersAndCustomers {
    @PrimaryKey
    @NonNull
    @Embedded
    private Orders order;

    @NonNull
    @Embedded
    private Customers customer;

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }
}
