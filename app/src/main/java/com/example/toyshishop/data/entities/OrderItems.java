package com.example.toyshishop.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "orders_items", primaryKeys = {"order_id", "product_id"},
        foreignKeys = {@ForeignKey(entity = Orders.class, parentColumns = "order_id", childColumns = "order_id", onDelete = CASCADE, onUpdate = CASCADE),
@ForeignKey(entity = Products.class, parentColumns = "pid", childColumns = "product_id", onDelete = CASCADE, onUpdate = CASCADE)})
public class OrderItems {
    @ColumnInfo(name = "order_id")
    private long orderId;

    @ColumnInfo(name = "product_id")
    private int productId;

    @ColumnInfo(name = "quantity")
    private int quantity;

    public OrderItems(long orderId, int productId, int quantity) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
