package com.example.toyshishop.data.dao;

import com.example.toyshishop.data.entities.OrdersAndCustomers;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface OrdersAndCustomersDao {
    @Query("SELECT * FROM orders o INNER JOIN customers c ON o.customer_id=c.cid")
    List<OrdersAndCustomers> fetchAllOrdersAndCustomers();
}
