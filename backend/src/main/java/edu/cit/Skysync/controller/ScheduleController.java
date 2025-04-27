package edu.cit.Skysync.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.dto.ScheduleRequestDTO;
import edu.cit.Skysync.entity.ScheduleEntity;
import edu.cit.Skysync.service.ScheduleService;

@CrossOrigin(origins = "*")
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

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ScheduleEntity>> getUserSchedules(@PathVariable Long userId) {
        return ResponseEntity.ok(scheduleService.getUserSchedules(userId));
    }
    // New endpoint: Delete a schedule by ID
    
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long scheduleId) {
        boolean isDeleted = scheduleService.deleteScheduleById(scheduleId);
        if (isDeleted) {
            return ResponseEntity.ok("Schedule deleted successfully.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<ScheduleEntity> editSchedule(
            @PathVariable Long scheduleId,
            @RequestBody ScheduleRequestDTO scheduleRequest) {
        ScheduleEntity updatedSchedule = scheduleService.editSchedule(
            scheduleId,
            scheduleRequest.getStartTime(),
            scheduleRequest.getEndTime()
        );
        return ResponseEntity.ok(updatedSchedule);
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<ScheduleEntity> getScheduleByActivityId(@PathVariable Long activityId) {
        ScheduleEntity schedule = scheduleService.getScheduleByActivityId(activityId);
        return ResponseEntity.ok(schedule);
    }
}