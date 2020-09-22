package com.example.toyshishop.data.dao;

import com.example.toyshishop.data.entities.CartAndProduct;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface CartAndProductDao {
    @Query("SELECT * FROM cart c INNER JOIN products p ON c.product_id = p.pid")
    List<CartAndProduct> getAllCartWithProducts();
}
