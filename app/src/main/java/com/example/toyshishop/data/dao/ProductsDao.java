package com.example.toyshishop.data.dao;

import com.example.toyshishop.data.entities.Products;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ProductsDao {
    @Insert
    void insertProduct(Products product);

    @Update
    void updateProduct(Products product);

    @Delete
    void deleteProduct(Products product);

    @Query("SELECT * FROM products")
    List<Products> fetchAllProducts();

    @Query("SELECT * FROM products WHERE pid = :id")
    Products fetchProductById(int id);

    @Query("SELECT * FROM products WHERE category_id = :id")
    List<Products> fetchProductsByCategory(int id);

    @Query("SELECT SUM(product_price) FROM products WHERE pid = :id")
    double getSumOfProductPriceByProductId(int id);

    @Query("SELECT product_price FROM products WHERE pid = :productId")
    double getProductPriceById(int productId);
}
