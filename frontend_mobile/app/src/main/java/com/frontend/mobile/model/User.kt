package com.frontend.mobile.model

data class User(
    val id: Long? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String? = null
)
