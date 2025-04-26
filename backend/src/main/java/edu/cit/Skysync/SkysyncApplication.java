package edu.cit.Skysync;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;      
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import edu.cit.Skysync.service.DailyWeatherNotificationService;
import jakarta.persistence.EntityManagerFactory;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class SkysyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(SkysyncApplication.class, args);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JobDetail dailyWeatherNotificationJobDetail() {
        return JobBuilder.newJob(DailyWeatherNotificationService.class)
                .withIdentity("dailyWeatherNotificationJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger dailyWeatherNotificationTrigger(JobDetail dailyWeatherNotificationJobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(dailyWeatherNotificationJobDetail)
                .withIdentity("dailyWeatherNotificationTrigger")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(00, 00))
                .build();
    }

    @Bean
    public Scheduler startScheduler(SchedulerFactoryBean schedulerFactoryBean, JobDetail dailyWeatherNotificationJobDetail, Trigger dailyWeatherNotificationTrigger) throws Exception {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        // Register the JobDetail and Trigger with the scheduler
        if (!scheduler.checkExists(dailyWeatherNotificationJobDetail.getKey())) {
            scheduler.addJob(dailyWeatherNotificationJobDetail, true);
        }
        if (!scheduler.checkExists(dailyWeatherNotificationTrigger.getKey())) {
            scheduler.scheduleJob(dailyWeatherNotificationTrigger);
        }

        scheduler.start(); // Explicitly start the scheduler

        System.out.println("Scheduler MetaData: " + scheduler.getMetaData());
        System.out.println("Registered Jobs: " + scheduler.getJobKeys(GroupMatcher.anyGroup()));
        System.out.println("Registered Triggers: " + scheduler.getTriggerKeys(GroupMatcher.anyGroup()));

        return scheduler;
    }
}