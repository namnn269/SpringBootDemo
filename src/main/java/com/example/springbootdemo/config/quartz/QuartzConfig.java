package com.example.springbootdemo.config.quartz;

import com.example.springbootdemo.infrastructure.database.demo_project.DemoProjectDBConfig;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.concurrent.Executors;

@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory,
                                                     @Qualifier(DemoProjectDBConfig.DATA_SOURCE) DataSource dataSource) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setTaskExecutor(Executors.newVirtualThreadPerTaskExecutor());
        schedulerFactoryBean.setAutoStartup(false);
        return schedulerFactoryBean;
    }

//    @Bean
//    public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws SchedulerException {
//        Scheduler scheduler = schedulerFactoryBean.getScheduler();
//        scheduler.start();
//        return scheduler;
//    }

}
