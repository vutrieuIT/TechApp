package com.example.techapp.model;

import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    private Integer id;

    @SerializedName("name")
    private String name;

    @SerializedName("moTa")
    private String moTa;

    @SerializedName("image")
    private String image;

    @SerializedName("soLuong")
    private int soLuong;

    @SerializedName("gia")
    private String gia;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    //    "name": "iphone 3",
//    "id": 1,
//    "categoryId": 6,
//    "quantity": 0,
//    "moTa": "sản phẩm điện thoại",
//    "image": "https://cdn.tgdd.vn/Products/Images/42/22230/iphone_3GS_b.jpg",
//    "soLuong": 100,
//    "gia": "1000"
}
