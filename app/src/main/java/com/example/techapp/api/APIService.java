package com.example.techapp.api;

import com.example.techapp.model.Category;
import com.example.techapp.model.Order1;
import com.example.techapp.model.OrderRequest;
import com.example.techapp.model.Product;
import com.example.techapp.model.ResponseModel;
import com.example.techapp.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

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

    @GET("/product")
    Call<List<Product>> getProduct();

    @GET("/product/{id}")
    Call<Product> getDetail(@Path("id") int id);


    // @Query("category_id") là param trên url
    @GET("/product")
    Call<List<Product>> getProducts(@Query("category_id") int categoryId);

    @GET("/product")
    Call<List<Product>> getProductsPaging(@Query("pageNumber") int pageNumber,
                                          @Query("pageSize") int pageSize);

    @Multipart
    @POST("/user/avatar")
    Call<User> uploadAvatar(@Part("id") int id,
                            @Part MultipartBody.Part avatar);

    @POST("/user/order")
    Call<Void> userOrder(@Body OrderRequest orderRequest);

    @GET("user/{id}/order")
    Call<List<Order1>> getUserOrders(@Path("id") int userId);

    @POST("/user/order/delete")
    Call<Void> deleteOrders(@Body List<Long> ids);
}
