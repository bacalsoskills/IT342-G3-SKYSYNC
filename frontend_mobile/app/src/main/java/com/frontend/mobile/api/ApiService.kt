package com.frontend.mobile.api
import com.frontend.mobile.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/api/users/createUser")
    fun registerUser(@Body user: User): Call<User>

    @POST("/api/users/login") // Add in backend if missing
    fun loginUser(@Body loginRequest: Map<String, String>): Call<User>



}
