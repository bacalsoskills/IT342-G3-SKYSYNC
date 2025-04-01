package edu.cit.Skysync.event;

import org.springframework.context.ApplicationEvent;

import edu.cit.Skysync.entity.ScheduleEntity;

public class ScheduleCreatedEvent extends ApplicationEvent {
    public ScheduleCreatedEvent(ScheduleEntity schedule) {
        super(schedule);
    }
    
    public ScheduleEntity getSchedule() {
        return (ScheduleEntity) getSource();
    }
}