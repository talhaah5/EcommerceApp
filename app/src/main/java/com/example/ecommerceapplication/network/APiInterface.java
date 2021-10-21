package com.example.ecommerceapplication.network;

import com.example.ecommerceapplication.models.Product;
import com.example.ecommerceapplication.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APiInterface {

    @GET("products")
    Call<List<Product>> getProducts();

    @POST("auth/login")
    Call<User> login(@Body User user);

    @POST("/users")
    Call<User> signUp(@Body User user);
}
