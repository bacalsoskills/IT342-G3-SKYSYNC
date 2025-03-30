package edu.cit.Skysync.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ScheduleRequestDTO {
    private Long activityId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}