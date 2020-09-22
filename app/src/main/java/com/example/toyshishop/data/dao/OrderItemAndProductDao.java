package com.example.toyshishop.data.dao;

import com.example.toyshishop.data.entities.OrderItemAndProduct;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface OrderItemAndProductDao {
    @Query("SELECT * FROM orders_items o INNER JOIN products p ON o.product_id = p.pid WHERE o.order_id = :id")
    List<OrderItemAndProduct> getOrderItemsAndProductsByOrderId(int id);
}
