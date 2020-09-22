package com.example.toyshishop.data.entities;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_with_product")
public class CartAndProduct {
    @PrimaryKey
    @NonNull
    @Embedded
    private Cart cartItem;

    @NonNull
    @Embedded
    private Products product;

    public CartAndProduct(@NonNull Cart cartItem, @NonNull Products product) {
        this.cartItem = cartItem;
        this.product = product;
    }

    @NonNull
    public Cart getCartItem() {
        return cartItem;
    }

    public void setCartItem(@NonNull Cart cartItem) {
        this.cartItem = cartItem;
    }

    @NonNull
    public Products getProduct() {
        return product;
    }

    public void setProduct(@NonNull Products product) {
        this.product = product;
    }
}
