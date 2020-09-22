package com.example.toyshishop.data.dao;

import com.example.toyshishop.data.entities.OrderItems;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface OrderItemsDao {
    @Insert
    void insertOrderItem(OrderItems orderItems);

    @Query("SELECT quantity FROM orders_items WHERE product_id = :id")
    List<Integer> getProductTotalSales(int id);

    @Query("SELECT quantity FROM orders_items")
    List<Integer> getTotalOrderItemsQuantities();
}
