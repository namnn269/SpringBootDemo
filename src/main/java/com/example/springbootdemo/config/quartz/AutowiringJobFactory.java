package com.example.springbootdemo.config.quartz;

import lombok.AllArgsConstructor;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AutowiringJobFactory implements JobFactory {

    private final ApplicationContext applicationContext;

    @Override
    public Job newJob(TriggerFiredBundle bundle,
                      Scheduler scheduler) throws SchedulerException {
        Class<? extends Job> jobClass = bundle.getJobDetail().getJobClass();
        return applicationContext.getBean(jobClass);
    }
}
