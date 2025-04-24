package com.frontend.mobile.model

data class ScheduleResponseDTO(
    val scheduleId: Long,
    val date: String,
    val dayOfWeek: String,
    val startTime: String,
    val endTime: String,
    val activity: ActivityDTO,
    val user: User
)
