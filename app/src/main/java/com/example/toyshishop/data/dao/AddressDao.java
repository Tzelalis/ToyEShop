package com.example.toyshishop.data.dao;

import com.example.toyshishop.data.entities.Address;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface AddressDao {

    @Insert
    void insertAddress(Address address);

    @Update
    void updateAddress(Address address);

    @Delete
    void deleteAddress(Address address);

    @Query("SELECT * FROM addresses")
    LiveData<List<Address>> fetchAllAddresses();
}
