package com.frontend.frontend.api

import com.frontend.frontend.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/users") // Matches UserController's createUser endpoint
    fun registerUser(@Body user: User): Call<User>

    @POST("/api/auth/login") // Matches AuthController's login endpoint
    fun loginUser(@Body loginRequest: Map<String, String>): Call<Map<String, String>> // Expecting token in response
}
