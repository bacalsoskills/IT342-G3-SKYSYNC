package edu.cit.Skysync.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.Skysync.entity.ActivityEntity;
import edu.cit.Skysync.service.ActivityService;
import edu.cit.Skysync.service.UserService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;
    private final UserService userService;
    
    public ActivityController(ActivityService activityService, UserService userService) {
        this.activityService = activityService;
        this.userService = userService;
    }
    
    @GetMapping("/recommendations")
    public List<ActivityEntity> getRecommendedActivities(@RequestParam int weatherCode) {
        return activityService.getRecommendedActivities(weatherCode);
    }
    
    @PostMapping("/save/{userId}")
    public ResponseEntity<ActivityEntity> saveActivity(@PathVariable Long userId, 
                                                    @RequestBody ActivityEntity activity) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(activityService.saveUserActivity(activity, user)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ActivityEntity>> getUserActivities(@PathVariable Long userId) {
        return userService.getUserById(userId)
                .map(user -> ResponseEntity.ok(activityService.getUserActivities(user)))
                .orElse(ResponseEntity.notFound().build());
    }
}