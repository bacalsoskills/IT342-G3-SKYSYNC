package com.frontend.mobile.model

data class ScheduleRequestDTO(
    val activityId: Long,
    val startTime: String,
    val endTime: String
)
