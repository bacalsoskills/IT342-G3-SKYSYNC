package edu.cit.Skysync.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.dto.ScheduleRequestDTO;
import edu.cit.Skysync.entity.ScheduleEntity;
import edu.cit.Skysync.service.ScheduleService;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {
    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // New endpoint with JSON body support
    @PostMapping("/create")
    public ResponseEntity<ScheduleEntity> createSchedule(
            @RequestParam Long userId,
            @RequestBody ScheduleRequestDTO scheduleRequest) {
        
        return ResponseEntity.ok(
            scheduleService.scheduleActivity(
                userId,
                scheduleRequest.getActivityId(),
                scheduleRequest.getStartTime(),
                scheduleRequest.getEndTime()
            )
        );
    }

    // Original endpoint kept for backward compatibility
    @PostMapping("/create/legacy")
    public ResponseEntity<ScheduleEntity> createScheduleLegacy(
            @RequestParam("userId") Long userId,
            @RequestParam("activityId") Long activityId,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        return ResponseEntity.ok(
            scheduleService.scheduleActivity(userId, activityId, startTime, endTime)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduleEntity>> getUserSchedules(@PathVariable Long userId) {
        return ResponseEntity.ok(scheduleService.getUserSchedules(userId));
    }

    @GetMapping("/user/{userId}/between")
    public ResponseEntity<List<ScheduleEntity>> getSchedulesBetweenDates(
            @PathVariable Long userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(
            scheduleService.getSchedulesBetweenDates(userId, start, end)
        );
    }
}