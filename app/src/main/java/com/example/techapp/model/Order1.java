package com.example.techapp.model;

import com.google.gson.annotations.SerializedName;

public class Order1 {

    @SerializedName("orderId")
    private Long orderId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("image")
    private String image;

    @SerializedName("quantity")
    private Long quantity;

    @SerializedName("totalMoney")
    private Long totalMoney;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Long totalMoney) {
        this.totalMoney = totalMoney;
    }

    //    "quantity": 20,
//    "image": "https://cdn.tgdd.vn/Products/Images/42/22230/iphone_3GS_b.jpg",
//    "orderId": 11,
//    "totalMoney": 20000,
//    "productName": "iphone 3"
}
