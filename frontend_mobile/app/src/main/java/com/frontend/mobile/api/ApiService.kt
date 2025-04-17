package com.frontend.mobile.api

import com.frontend.mobile.model.User
import com.frontend.mobile.model.AuthResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("users") // Matches POST /api/users
    fun registerUser(@Body user: User): Call<User>

    @POST("auth/login") // Matches POST /api/auth/login
    fun loginUser(@Body loginRequest: Map<String, String>): Call<AuthResponse>
}
