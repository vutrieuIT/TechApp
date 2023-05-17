package com.example.techapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderRequest {
    @SerializedName("user_id")
    private int userId;

    private List<Order> orders;

    // Các getter và setter


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}