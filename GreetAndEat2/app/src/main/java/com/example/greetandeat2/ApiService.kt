package com.example.greetandeat2

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Path

interface ApiService {

    // Get all restaurants
    @GET("restaurants")
    suspend fun getRestaurants(): Response<List<Restaurant>>

    // Place a new order
    @POST("orders")
    suspend fun placeOrder(@Body order: Order): Response<Order>

    // Get orders by user ID
    @GET("orders/{userId}")
    suspend fun getOrders(@Path("userId") userId: String): Response<List<Order>>
}
