package com.frontend.mobile.model

data class ActivityDTO(
    val activityId: Long, // Add this field
    val name: String,
    val description: String,
    val weatherCondition: String
)