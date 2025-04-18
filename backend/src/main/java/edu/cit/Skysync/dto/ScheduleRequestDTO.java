package edu.cit.Skysync.dto;

import java.time.LocalDateTime;

public class ScheduleRequestDTO {
    private Long activityId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    // Getters
    public Long getActivityId() {
        return activityId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    // Setters
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}