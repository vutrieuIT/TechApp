package com.example.techapp.api;

import com.example.techapp.model.Category;
import com.example.techapp.model.ResponseModel;
import com.example.techapp.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIService {

    // đăng nhập
    @FormUrlEncoded // form-data postman
    @POST("/user/login")
    Call<ResponseModel<User>> login(
            @Field("userName") String userName,
            @Field("password") String password
    );

    @FormUrlEncoded // form-data postman
    @POST("/user/register")
    Call<ResponseModel<User>> register(
            @Field("fullName") String fullName,
            @Field("userName") String userName,
            @Field("password") String password,
            @Field("email") String email,
            @Field("sdt") String sdt
    );

    @GET("/category")
    Call<ResponseModel<List<Category>>> getCategories();

}
