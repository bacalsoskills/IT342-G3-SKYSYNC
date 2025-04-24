package edu.cit.Skysync.service;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import edu.cit.Skysync.repository.NotificationRepository;
import edu.cit.Skysync.repository.ScheduleRepository;

@Component
public class DeleteScheduleJob implements Job {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    @Transactional // Ensure database operations are transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            // Retrieve job data
            Long scheduleId = context.getJobDetail().getJobDataMap().getLong("scheduleId");

            // Debug log to verify the schedule ID
            System.out.println("Executing DeleteScheduleJob for schedule ID: " + scheduleId);

            // Set the schedule_id in notifications to NULL
            notificationRepository.findBySchedule_ScheduleId(scheduleId).forEach(notification -> {
                notification.setSchedule(null);
                notificationRepository.save(notification);
            });

            // Delete the schedule
            if (scheduleRepository.existsById(scheduleId)) {
                scheduleRepository.deleteById(scheduleId);
                System.out.println("Deleted schedule with ID: " + scheduleId);
            } else {
                System.out.println("Schedule with ID " + scheduleId + " not found.");
            }
        } catch (Exception e) {
            throw new JobExecutionException("Failed to delete schedule", e);
        }
    }
}
