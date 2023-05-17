package com.example.techapp.model;

import com.google.gson.annotations.SerializedName;

public class Order {
    @SerializedName("product_id")
    private int productId;

    @SerializedName("quantity")
    private int quantity;

    @SerializedName("total_money")
    private int totalMoney;

    // Các getter và setter

    public Order() {
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

    public int getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(int totalMoney) {
        this.totalMoney = totalMoney;
    }
}
