package com.example.toyshishop.data.dao;

import com.example.toyshishop.data.entities.Orders;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface OrdersDao {

    @Insert
    long insertOrder(Orders order);

    @Delete
    void deleteOrder(Orders order);
}
