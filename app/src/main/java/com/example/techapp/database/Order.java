package com.example.techapp.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity
public class Order {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int product_id;
    private String image;
    private String name;
    private int amount;
    private int totalPrice;

    public Order(int product_id, String image,
                 String name, int amount, int totalPrice) {
        this.product_id = product_id;
        this.image = image;
        this.name = name;
        this.amount = amount;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Dao
    public interface OrderDAO{
        @Insert
        void insert(Order order);

        @Query("SELECT * From `Order`")
        List<Order> getALl();

        @Delete
        void delete(Order order);

        @Query("delete from `Order` where id = :id")
        void deleteOrderById(int id);
    }
}
