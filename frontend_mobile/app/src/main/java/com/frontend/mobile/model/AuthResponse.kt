package com.frontend.mobile.model

data class AuthResponse(
    val email: String,
    val userId: Long,
    val token: String
)