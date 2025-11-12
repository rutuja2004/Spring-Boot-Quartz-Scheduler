package com.example.quartz.config;


import com.example.quartz.job.HelloJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.TriggerBuilder;
import org.quartz.JobBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;


@Configuration
public class QuartzConfig {


// Define a JobDetail as a bean (cron job that runs every minute as example)
@Bean
public JobDetail helloJobDetail() {
return JobBuilder.newJob(HelloJob.class)
.withIdentity("helloJob")
.usingJobData(HelloJob.MESSAGE_KEY, "Hello from auto cron job!")
.storeDurably()
.build();
}


@Bean
public CronTrigger helloJobTrigger(JobDetail helloJobDetail) {
return TriggerBuilder.newTrigger()
.forJob(helloJobDetail)
.withIdentity("helloJobTrigger")
.withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?"))
.build();
}
}