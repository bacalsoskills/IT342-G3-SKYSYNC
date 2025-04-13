package com.example.frontend.api

import retrofit2.Call
import retrofit2.http.*

data class LoginRequest(val email: String, val password: String)
data class LoginResponse(val email: String, val userId: Long, val token: String)

data class User(val id: Long?, val firstName: String, val lastName: String, val email: String, val password: String?)

interface BackendApi {
    @POST("/api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("/api/auth/logout")
    fun logout(@Header("Authorization") token: String): Call<Void>

    @GET("/api/users")
    fun getUserById(@Query("id") userId: Long): Call<User>

    @POST("/api/users")
    fun createUser(@Body user: User): Call<User>

    @PUT("/api/users")
    fun updateUser(@Query("id") userId: Long, @Body user: User): Call<User>

    @DELETE("/api/users")
    fun deleteUser(@Query("id") userId: Long): Call<Void>
}