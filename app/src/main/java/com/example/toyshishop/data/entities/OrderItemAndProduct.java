package com.example.toyshishop.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "order_item_and_product")
public class OrderItemAndProduct {
    @NonNull
    @PrimaryKey
    @Embedded
    private OrderItems orderItems;

    @NonNull
    @Embedded
    private Products product;

    @NonNull
    public OrderItems getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(@NonNull OrderItems orderItems) {
        this.orderItems = orderItems;
    }

    @NonNull
    public Products getProduct() {
        return product;
    }

    public void setProduct(@NonNull Products product) {
        this.product = product;
    }
}
