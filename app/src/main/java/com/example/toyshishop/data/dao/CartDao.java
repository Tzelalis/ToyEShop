package com.example.toyshishop.data.dao;

import com.example.toyshishop.data.entities.Cart;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CartDao {
    @Insert
    void insertItem(Cart cartItem);

    @Update
    void updateCart(Cart cartItem);

    @Delete
    void deleteItem(Cart cartItem);

    @Query("SELECT * FROM cart")
    List<Cart> fetchCart();

    @Query("DELETE FROM cart")
    void clearCart();

    @Query("SELECT COUNT(*) FROM cart")
    int getCartSize();
}
