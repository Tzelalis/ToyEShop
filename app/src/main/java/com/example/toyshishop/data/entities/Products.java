package com.example.toyshishop.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "products")
public class Products {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "pid")
    private   int productId;

    @ColumnInfo(name = "product_name")
    private String productName;

    @ColumnInfo(name = "category_id")
    private int productCategory;

    @ColumnInfo(name = "product_description")
    private String description;

    @ColumnInfo(name = "product_price")
    private double price;

    @ColumnInfo(name = "product_old_price")
    private double oldPrice;

    @ColumnInfo(name = "product_quantity")
    private int quantity;

    @ColumnInfo(name = "image_path")
    private String imagePath;

    public Products(String productName, String description, int productCategory, double price, double oldPrice, int quantity, String imagePath) {
        this.productName = productName;
        this.productCategory = productCategory;
        this.description = description;
        this.price = price;
        this.oldPrice = oldPrice;
        this.quantity = quantity;
        this.imagePath = imagePath;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(int productCategory) {
        this.productCategory = productCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTextProductId(){
        if(productId >=10000)
            return String.valueOf(productId);
        else if (productId >= 1000)
            return "0" + productId;
        else if (productId >= 100)
            return "00" + productId;
        else if (productId >= 10)
            return "000" + productId;
        else
            return "0000" + productId;
    }

    public String getDiscount(){
        int discount = (int) ((1 - (price / oldPrice)) * 100);

        if(discount > 0 && oldPrice > 0) {
            return discount + "%";
        }else{
            return null;
        }
    }

    public String getCategoryName(){
        if(productCategory==0){
            return "Ναργιλέδες";
        }else if(productCategory==1){
            return "Γεύσεις";
        }else
            return "Αξεσουάρ";
    }
}
