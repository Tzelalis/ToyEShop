package com.example.toyshishop.data.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

//
@Entity(tableName = "orders", foreignKeys = {@ForeignKey(entity = Customers.class, parentColumns = "cid", childColumns = "customer_id", onUpdate = CASCADE, onDelete = CASCADE)})
public class Orders {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "order_id")
    private int orderId;

    @NonNull
    @ColumnInfo(name = "customer_id")
    private int customerId;

    @NonNull
    @ColumnInfo(name = "order_address_id")
    private int addressID;

    @NonNull
    @ColumnInfo(name = "created_date")
    private long createdDate;

    @NonNull
    @ColumnInfo(name = "total_price")
    private double totalPrice;

    public Orders(int customerId, int addressID, long createdDate, double totalPrice) {
        this.customerId = customerId;
        this.addressID = addressID;
        this.createdDate = createdDate;
        this.totalPrice = totalPrice;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getAddressID() {
        return addressID;
    }

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
