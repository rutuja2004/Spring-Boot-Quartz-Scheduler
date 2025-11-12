# Spring Boot Quartz Scheduler - User Manual

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Technologies & Libraries](#technologies--libraries)
3. [API Endpoints](#api-endpoints)
4. [Request/Response Examples](#requestresponse-examples)
5. [Parameter Explanations](#parameter-explanations)
6. [Testing with Postman](#testing-with-postman)
7. [Project Structure](#project-structure)
8. [How It Works](#how-it-works)

---

## 🎯 Project Overview

**Spring Boot Quartz Scheduler** is a job scheduling application that allows you to:
- Schedule one-time jobs for future execution
- Schedule repeating jobs with custom intervals
- Monitor all scheduled jobs and their status
- Manage background tasks through REST API endpoints

**Base URL:** `http://localhost:8080`

---

## 🛠️ Technologies & Libraries

### **Core Technologies**
- **Java 17** - Programming language
- **Spring Boot 3.1.4** - Main application framework
- **Maven** - Build tool and dependency management

### **Key Dependencies**
| Library | Purpose | Version |
|---------|---------|---------|
| `spring-boot-starter-web` | REST API endpoints, Embedded Tomcat | 3.1.4 |
| `spring-boot-starter-quartz` | Job scheduling engine | 3.1.4 |
| `spring-boot-starter-logging` | Application logging (SLF4J + Logback) | 3.1.4 |
| `spring-boot-starter-test` | Unit testing capabilities | 3.1.4 |

### **Quartz Configuration**
- **Job Store:** In-memory (RAMJobStore)
- **Thread Pool:** 5 threads
- **Server Port:** 8080
- **Default Job:** Runs every 60 seconds automatically

---

## 🌐 API Endpoints

### **Available Endpoints**

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/scheduler/test` | Test if application is running |
| POST | `/api/scheduler/oneoff` | Schedule one-time job |
| POST | `/api/scheduler/repeat` | Schedule repeating job |
| GET | `/api/scheduler/jobs` | List all scheduled jobs |

---

## 📝 Request/Response Examples

### **1. Test Endpoint**

**Request:**
```http
GET http://localhost:8080/api/scheduler/test
```

**Response:**
```
Status: 200 OK
Body: "Scheduler controller is working!"
```

---

### **2. Schedule One-Time Job**

**Request:**
```http
POST http://localhost:8080/api/scheduler/oneoff
Content-Type: application/x-www-form-urlencoded

jobName=welcomeEmail
group=EMAIL_SERVICES
message=Welcome new user to platform
runAt=2024-11-12 15:30:00
```

**Response:**
```
Status: 200 OK
Body: "One-off job scheduled successfully"
```

**Error Response:**
```
Status: 400 Bad Request
Body: "Error scheduling job: [error details]"
```

---

### **3. Schedule Repeating Job**

**Request:**
```http
POST http://localhost:8080/api/scheduler/repeat
Content-Type: application/x-www-form-urlencoded

jobName=newsletter
group=EMAIL_SERVICES
message=Sending weekly newsletter
intervalMillis=10000
repeatCount=5
```

**Response:**
```
Status: 200 OK
Body: "Repeating job scheduled successfully"
```

**Error Response:**
```
Status: 400 Bad Request
Body: "Error scheduling job: [error details]"
```

---

### **4. List All Jobs**

**Request:**
```http
GET http://localhost:8080/api/scheduler/jobs
```

**Response:**
```
Status: 200 OK
Body: 
=== ALL SCHEDULED JOBS WITH DETAILS ===

📁 GROUP: EMAIL_SERVICES
==================================================

🔧 JOB NAME: newsletter
   Class: HelloJob
   Message: Sending weekly newsletter

   ⏰ TRIGGER DETAILS:
      Trigger Name: newsletterTrigger
      State: NORMAL
      Next Fire Time: Tue Nov 11 15:45:19 IST 2025
      Previous Fire Time: Tue Nov 11 15:45:09 IST 2025
      Repeat Interval: 10000 ms
      Repeat Count: 5
      Times Triggered: 2
----------------------------------------

📁 GROUP: DEFAULT
==================================================

🔧 JOB NAME: helloJob
   Class: HelloJob
   Message: Hello from auto cron job!

   ⏰ TRIGGER DETAILS:
      Trigger Name: helloJobTrigger
      State: NORMAL
      Next Fire Time: Tue Nov 11 15:46:00 IST 2025
      Previous Fire Time: Tue Nov 11 15:45:00 IST 2025
      Cron Expression: 0 0/1 * * * ?
----------------------------------------
```

---

## 📊 Parameter Explanations

### **One-Time Job Parameters (`/oneoff`)**

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| `jobName` | String | Yes | Unique identifier for the job | "welcomeEmail" |
| `group` | String | Yes | Category to organize jobs | "EMAIL_SERVICES" |
| `message` | String | Yes | Custom message for job execution | "Welcome new user" |
| `runAt` | Date | Yes | Exact execution time | "2024-11-12 15:30:00" |

**Date Format:** `yyyy-MM-dd HH:mm:ss`

### **Repeating Job Parameters (`/repeat`)**

| Parameter | Type | Required | Description | Example |
|-----------|------|----------|-------------|---------|
| `jobName` | String | Yes | Unique identifier for the job | "newsletter" |
| `group` | String | Yes | Category to organize jobs | "EMAIL_SERVICES" |
| `message` | String | Yes | Custom message for job execution | "Sending newsletter" |
| `intervalMillis` | Long | Yes | Time between executions (milliseconds) | 10000 (10 seconds) |
| `repeatCount` | Integer | Yes | Number of repetitions | 5 (runs 6 times total) |

### **Time Conversion Guide**

| Duration | Milliseconds |
|----------|--------------|
| 1 second | 1,000 |
| 1 minute | 60,000 |
| 1 hour | 3,600,000 |
| 1 day | 86,400,000 |

### **Repeat Count Logic**
- `0` = Run only once (no repeats)
- `5` = Run 6 times total (initial + 5 repeats)
- `-1` = Run forever (infinite)

---

## 🧪 Testing with Postman

### **Setup Steps**

1. **Start Application**
   ```bash
   mvn spring-boot:run
   ```

2. **Test Connection**
   - Method: GET
   - URL: `http://localhost:8080/api/scheduler/test`
   - Expected: "Scheduler controller is working!"

### **Sample Test Cases**

#### **Test Case 1: Email Services Group**

**Job 1 - Newsletter:**
```
POST http://localhost:8080/api/scheduler/repeat
Body (form-data):
jobName: newsletter
group: EMAIL_SERVICES
message: Sending weekly newsletter
intervalMillis: 10000
repeatCount: 5
```

**Job 2 - Welcome Email:**
```
POST http://localhost:8080/api/scheduler/repeat
Body (form-data):
jobName: welcomeEmail
group: EMAIL_SERVICES
message: Sending welcome email to new users
intervalMillis: 15000
repeatCount: 3
```

#### **Test Case 2: System Maintenance Group**

**Job 1 - Health Check:**
```
POST http://localhost:8080/api/scheduler/repeat
Body (form-data):
jobName: healthCheck
group: SYSTEM_MAINTENANCE
message: Running system health check
intervalMillis: 5000
repeatCount: 12
```

**Job 2 - Cache Clear:**
```
POST http://localhost:8080/api/scheduler/repeat
Body (form-data):
jobName: cacheClear
group: SYSTEM_MAINTENANCE
message: Clearing application cache
intervalMillis: 25000
repeatCount: 3
```

#### **Test Case 3: One-Time Job**

```
POST http://localhost:8080/api/scheduler/oneoff
Body (form-data):
jobName: urgentReport
group: REPORTS
message: Generate urgent financial report
runAt: 2024-11-12 16:00:00
```

#### **Test Case 4: View All Jobs**

```
GET http://localhost:8080/api/scheduler/jobs
```

---

## 📁 Project Structure

```
spring-boot-quartz-scheduler/
├── src/main/java/com/example/quartz/
│   ├── QuartzSchedulerApplication.java    # Main application class
│   ├── controller/
│   │   └── SchedulerController.java       # REST API endpoints
│   ├── service/
│   │   └── SchedulerService.java          # Business logic
│   ├── job/
│   │   └── HelloJob.java                  # Job implementation
│   └── config/
│       └── QuartzConfig.java              # Quartz configuration
├── src/main/resources/
│   └── application.properties             # Application settings
└── pom.xml                                # Maven dependencies
```

### **Key Files Explained**

| File | Purpose |
|------|---------|
| `SchedulerController.java` | Defines REST API endpoints for job management |
| `SchedulerService.java` | Contains business logic for scheduling operations |
| `HelloJob.java` | Actual job implementation that gets executed |
| `QuartzConfig.java` | Configures default jobs and Quartz settings |
| `application.properties` | Server port, Quartz settings, logging configuration |

---

## ⚙️ How It Works

### **Job Execution Flow**

1. **Schedule Job** → API call creates JobDetail + Trigger
2. **Store in Memory** → Quartz stores job information in RAM
3. **Monitor Time** → Quartz continuously checks for due jobs
4. **Execute Job** → When time matches, HelloJob.execute() runs
5. **Log Results** → Execution details printed to terminal
6. **Reschedule** → If repeating, Quartz schedules next execution

### **What You See in Terminal**

**Job Execution Log:**
```
*** JOB EXECUTED ***
Job Name: newsletter
Group: EMAIL_SERVICES
Message: Sending weekly newsletter
Execution Time: 2025-11-11T15:45:19
Fire Instance ID: NON_CLUSTERED1699123456789
******************
```

**Default Job (Every Minute):**
```
*** JOB EXECUTED ***
Job Name: helloJob
Group: DEFAULT
Message: Hello from auto cron job!
Execution Time: 2025-11-11T15:46:00
Fire Instance ID: NON_CLUSTERED1699123461234
******************
```

### **Key Concepts**

- **Fire Instance ID**: Unique identifier for each job execution
- **Job Groups**: Organize related jobs together
- **Triggers**: Define when and how often jobs run
- **Automatic Execution**: Once scheduled, jobs run without manual intervention
- **Thread Safety**: Multiple jobs can run simultaneously

---

## 🚀 Getting Started

### **Prerequisites**
- Java 17 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)
- Postman (for API testing)

### **Quick Start**

1. **Clone/Download Project**
2. **Navigate to Project Directory**
   ```bash
   cd spring-boot-quartz-scheduler
   ```
3. **Compile Project**
   ```bash
   mvn clean compile
   ```
4. **Run Application**
   ```bash
   mvn spring-boot:run
   ```
5. **Test API**
   - Open Postman
   - Test: `GET http://localhost:8080/api/scheduler/test`
6. **Schedule Jobs**
   - Use POST endpoints to create jobs
   - Monitor terminal for execution logs

### **Stopping Application**
- Press `Ctrl+C` in terminal
- Or kill process on port 8080: `taskkill /PID [process_id] /F`

---

## 📞 Support & Troubleshooting

### **Common Issues**

| Issue | Solution |
|-------|----------|
| Port 8080 already in use | Kill existing process or change port in application.properties |
| 404 Not Found | Ensure application is running and URL is correct |
| Job not executing | Check job parameters and application logs |
| Date format error | Use exact format: "yyyy-MM-dd HH:mm:ss" |

### **Logs Location**
- **Console Output**: Terminal where application is running
- **Application Logs**: Check IDE console or terminal output

---

**Document Version:** 1.0  
**Last Updated:** November 2024  
**Project:** Spring Boot Quartz Scheduler