package com.example.quartz.service;


import com.example.quartz.job.HelloJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.util.Date;


@Service
public class SchedulerService {


private final Scheduler scheduler;


@Autowired
public SchedulerService(Scheduler scheduler) {
this.scheduler = scheduler;
}


// schedule a one-off job that runs at 'runAt' (Date)
public void scheduleOneOff(String jobName, String group, String message, Date runAt) throws SchedulerException {
JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
.withIdentity(jobName, group)
.usingJobData(HelloJob.MESSAGE_KEY, message)
.build();


Trigger trigger = TriggerBuilder.newTrigger()
.withIdentity(jobName + "Trigger", group)
.startAt(runAt)
.withSchedule(SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow())
.forJob(jobDetail)
.build();


scheduler.scheduleJob(jobDetail, trigger);
}


// schedule a repeating simple job (repeatCount = SimpleTrigger.REPEAT_INDEFINITELY for infinite)
public void scheduleSimpleRepeat(String jobName, String group, String message, long intervalMillis, int repeatCount) throws SchedulerException {
JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
.withIdentity(jobName, group)
.usingJobData(HelloJob.MESSAGE_KEY, message)
.build();


Trigger trigger = TriggerBuilder.newTrigger()
.withIdentity(jobName + "Trigger", group)
.startAt(Date.from(Instant.now().plusMillis(500)))
.withSchedule(SimpleScheduleBuilder.simpleSchedule()
.withIntervalInMilliseconds(intervalMillis)
.withRepeatCount(repeatCount)
.withMisfireHandlingInstructionNowWithExistingCount())
.forJob(jobDetail)
.build();


scheduler.scheduleJob(jobDetail, trigger);
}


// get all scheduled jobs with complete details
public String getAllJobs() throws SchedulerException {
StringBuilder result = new StringBuilder();
result.append("=== ALL SCHEDULED JOBS WITH DETAILS ===\n");

for (String groupName : scheduler.getJobGroupNames()) {
result.append("\n📁 GROUP: ").append(groupName).append("\n");
result.append("=".repeat(50)).append("\n");

for (JobKey jobKey : scheduler.getJobKeys(org.quartz.impl.matchers.GroupMatcher.jobGroupEquals(groupName))) {
org.quartz.JobDetail jobDetail = scheduler.getJobDetail(jobKey);

result.append("\n🔧 JOB NAME: ").append(jobKey.getName()).append("\n");
result.append("   Class: ").append(jobDetail.getJobClass().getSimpleName()).append("\n");

// Get job data (message, etc.)
org.quartz.JobDataMap jobDataMap = jobDetail.getJobDataMap();
if (jobDataMap.containsKey("message")) {
result.append("   Message: ").append(jobDataMap.getString("message")).append("\n");
}

// Get triggers for this job
for (org.quartz.Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
result.append("\n   ⏰ TRIGGER DETAILS:\n");
result.append("      Trigger Name: ").append(trigger.getKey().getName()).append("\n");
result.append("      State: ").append(scheduler.getTriggerState(trigger.getKey())).append("\n");
result.append("      Next Fire Time: ").append(trigger.getNextFireTime()).append("\n");
result.append("      Previous Fire Time: ").append(trigger.getPreviousFireTime()).append("\n");

// Check if it's a SimpleTrigger to get interval and repeat info
if (trigger instanceof org.quartz.SimpleTrigger) {
org.quartz.SimpleTrigger simpleTrigger = (org.quartz.SimpleTrigger) trigger;
result.append("      Repeat Interval: ").append(simpleTrigger.getRepeatInterval()).append(" ms\n");
result.append("      Repeat Count: ").append(simpleTrigger.getRepeatCount()).append("\n");
result.append("      Times Triggered: ").append(simpleTrigger.getTimesTriggered()).append("\n");
}

// Check if it's a CronTrigger
if (trigger instanceof org.quartz.CronTrigger) {
org.quartz.CronTrigger cronTrigger = (org.quartz.CronTrigger) trigger;
result.append("      Cron Expression: ").append(cronTrigger.getCronExpression()).append("\n");
}
}
result.append("-".repeat(40)).append("\n");
}
}
System.out.println(result.toString());
return result.toString();
}
}