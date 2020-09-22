package com.example.toyshishop.data;

import com.example.toyshishop.data.dao.AddressDao;
import com.example.toyshishop.data.dao.CartAndProductDao;
import com.example.toyshishop.data.dao.CartDao;
import com.example.toyshishop.data.dao.CustomersDao;
import com.example.toyshishop.data.dao.OrderItemAndProductDao;
import com.example.toyshishop.data.dao.OrderItemsDao;
import com.example.toyshishop.data.dao.OrdersAndCustomersDao;
import com.example.toyshishop.data.dao.OrdersDao;
import com.example.toyshishop.data.dao.ProductsDao;
import com.example.toyshishop.data.entities.Address;
import com.example.toyshishop.data.entities.Cart;
import com.example.toyshishop.data.entities.CartAndProduct;
import com.example.toyshishop.data.entities.Customers;
import com.example.toyshishop.data.entities.OrderItemAndProduct;
import com.example.toyshishop.data.entities.OrderItems;
import com.example.toyshishop.data.entities.Orders;
import com.example.toyshishop.data.entities.OrdersAndCustomers;
import com.example.toyshishop.data.entities.Products;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Address.class, Customers.class, Orders.class, OrderItems.class, Products.class, Cart.class, CartAndProduct.class, OrdersAndCustomers.class, OrderItemAndProduct.class}, version = 1)
public abstract class EshopDatabase extends RoomDatabase {
    public abstract AddressDao addressDao();
    public abstract CustomersDao customersDao();
    public abstract OrdersDao ordersDao();
    public abstract OrderItemsDao orderItemsDao();
    public abstract ProductsDao productsDao();
    public abstract CartDao cartDao();
    public abstract CartAndProductDao cartAndProductDao();
    public abstract OrdersAndCustomersDao ordersAndCustomersDao();
    public abstract OrderItemAndProductDao orderItemAndProductDao();
}
