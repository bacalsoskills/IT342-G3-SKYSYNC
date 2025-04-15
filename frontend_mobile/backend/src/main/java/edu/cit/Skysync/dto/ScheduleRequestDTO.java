package edu.cit.Skysync.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ScheduleRequestDTO {
    private Long activityId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String dayOfWeek; // Optional: Can be added if you want to receive it from client
}