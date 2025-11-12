package com.example.quartz.controller;

import com.example.quartz.service.SchedulerService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/scheduler")
public class SchedulerController {

    private final SchedulerService schedulerService;

    @Autowired
    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @GetMapping("/test")
    public String test() {
        return "Scheduler controller is working!";
    }

    @PostMapping("/oneoff")
    public ResponseEntity<String> scheduleOneOff(
            @RequestParam String jobName,
            @RequestParam String group,
            @RequestParam String message,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date runAt) {
        try {
            schedulerService.scheduleOneOff(jobName, group, message, runAt);
            return ResponseEntity.ok("One-off job scheduled successfully");
        } catch (SchedulerException e) {
            return ResponseEntity.badRequest().body("Error scheduling job: " + e.getMessage());
        }
    }

    @PostMapping("/repeat")
    public ResponseEntity<String> scheduleRepeat(
            @RequestParam String jobName,
            @RequestParam String group,
            @RequestParam String message,
            @RequestParam long intervalMillis,
            @RequestParam int repeatCount) {
        try {
            schedulerService.scheduleSimpleRepeat(jobName, group, message, intervalMillis, repeatCount);
            return ResponseEntity.ok("Repeating job scheduled successfully");
        } catch (SchedulerException e) {
            return ResponseEntity.badRequest().body("Error scheduling job: " + e.getMessage());
        }
    }

    @GetMapping("/jobs")
    public ResponseEntity<String> listAllJobs() {
        try {
            String jobsList = schedulerService.getAllJobs();
            return ResponseEntity.ok(jobsList);
        } catch (SchedulerException e) {
            return ResponseEntity.badRequest().body("Error getting jobs: " + e.getMessage());
        }
    }
}