package com.example.toyshishop.data.asyncParams;

import com.example.toyshishop.data.entities.OrderItemAndProduct;
import com.example.toyshishop.data.entities.OrdersAndCustomers;

import java.util.ArrayList;

public class SpecificOrderAsyncTaskParams {
    private ArrayList<OrderItemAndProduct> orderItemAndProduct;
    private OrdersAndCustomers ordersAndCustomers;

    public SpecificOrderAsyncTaskParams(ArrayList<OrderItemAndProduct> orderItemAndProduct, OrdersAndCustomers ordersAndCustomers) {
        this.orderItemAndProduct = orderItemAndProduct;
        this.ordersAndCustomers = ordersAndCustomers;
    }

    public ArrayList<OrderItemAndProduct> getOrderItemAndProduct() {
        return orderItemAndProduct;
    }

    public void setOrderItemAndProduct(ArrayList<OrderItemAndProduct> orderItemAndProduct) {
        this.orderItemAndProduct = orderItemAndProduct;
    }

    public OrdersAndCustomers getOrdersAndCustomers() {
        return ordersAndCustomers;
    }

    public void setOrdersAndCustomers(OrdersAndCustomers ordersAndCustomers) {
        this.ordersAndCustomers = ordersAndCustomers;
    }
}
