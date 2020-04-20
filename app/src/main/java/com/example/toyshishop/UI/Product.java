package com.example.toyshishop.UI;

import androidx.annotation.Nullable;

public class Product {
    private String name;
    private String shortDescription;
    private String description;
    private double price;
    private double oldPrice;
    private int imgID;




    public Product(String name, String description, String shortDescription, double price, double oldPrice) {
        this.name = name;
        this.shortDescription = shortDescription;
        this.description = description;
        this.price = price;
        this.oldPrice = oldPrice;
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public int getImgID() {
        return imgID;
    }
}
