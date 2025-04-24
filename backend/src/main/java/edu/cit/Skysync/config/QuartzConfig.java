package edu.cit.Skysync.config;

import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import jakarta.annotation.PostConstruct;

@Configuration
public class QuartzConfig {

    @Autowired
    private org.springframework.context.ApplicationContext applicationContext;

    @Bean
    public JobFactory springBeanJobFactory() {
        return new SpringBeanJobFactory() {
            @Override
            protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
                Object job = super.createJobInstance(bundle);
                applicationContext.getAutowireCapableBeanFactory().autowireBean(job);
                return job;
            }
        };
    }

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);
        return factory;
    }
}