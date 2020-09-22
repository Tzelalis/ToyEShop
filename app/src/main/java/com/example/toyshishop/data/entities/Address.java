package com.example.toyshishop.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "addresses", foreignKeys = {@ForeignKey(entity = Customers.class, parentColumns = "cid", childColumns = "customer_id", onDelete = CASCADE, onUpdate = CASCADE)})
public class Address {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "address_id")
    private int addressId;

    @ColumnInfo(name = "customer_id")
    private int customerId;

    @ColumnInfo(name = "address_name")
    private String address_name;

    @ColumnInfo(name = "address_number")
    private int addressNumber;

    @ColumnInfo(name = "post_code")
    private int postCode;

    @ColumnInfo(name = "area")
    private String area;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "country")
    private String country;

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public int getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(int addressNumber) {
        this.addressNumber = addressNumber;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
