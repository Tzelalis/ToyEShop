package com.example.toyshishop.data.dao;

import com.example.toyshishop.data.entities.Customers;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CustomersDao {
    @Insert
    void insertCustomer(Customers customer);

    @Update
    void updateCustomer(Customers customer);

    @Delete
    void deleteCustomer(Customers customer);

    @Query("SELECT * FROM customers")
    List<Customers> fetchAllCustomers();

    @Query("SELECT * FROM customers WHERE cid = :id")
    Customers fetchCustomer(int id);

}
