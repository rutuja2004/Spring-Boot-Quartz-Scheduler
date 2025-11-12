package com.example.quartz.job;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;


public class HelloJob implements Job {
private static final Logger logger = LoggerFactory.getLogger(HelloJob.class);


public static final String MESSAGE_KEY = "message";


@Override
public void execute(JobExecutionContext context) throws JobExecutionException {
String message = (String) context.getMergedJobDataMap().getOrDefault(MESSAGE_KEY, "Hello from Quartz!");
String now = OffsetDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
String jobName = context.getJobDetail().getKey().getName();
String groupName = context.getJobDetail().getKey().getGroup();

String executionLog = String.format(
"\n*** JOB EXECUTED ***\n" +
"Job Name: %s\n" +
"Group: %s\n" +
"Message: %s\n" +
"Execution Time: %s\n" +
"Fire Instance ID: %s\n" +
"******************\n",
jobName, groupName, message, now, context.getFireInstanceId());

logger.info(executionLog);
System.out.println(executionLog);

}
}