# Service Monitor Backend

Backend service for a centralized monitoring dashboard system.

This project was built as part of the NUXA Fullstack Developer Technical Test.

Repository:
https://github.com/ikhsanmaaa/service-monitor.git

---

# Features

## Inventory API
- Create monitored service
- Get all monitored services
- Get service by ID
- Update monitored service
- Delete monitored service

## Monitoring Engine
- Automated health check scheduler
- Manual force re-check endpoint
- HTTP status monitoring
- Latency tracking
- Last checked timestamp
- Service UP/DOWN detection

## Testing
- Controller unit test
- Integration test with PostgreSQL

---

# Tech Stack

- Java 21
- Spring Boot 3.5.x
- Spring Data JPA
- PostgreSQL
- Docker
- Maven
- Lombok

---

# Project Structure

```txt
src/main/java/com/ikhsan/servicemonitor
│
├── controller
├── service
├── repository
├── entity
├── dto
├── enums
├── scheduler
├── config
└── exception
````

---

# API Endpoints

## CRUD Endpoints

### Get All Services

```http
GET /api/services
```

### Get Service By ID

```http
GET /api/services/{id}
```

### Create Service

```http
POST /api/services/create
```

### Update Service

```http
PATCH /api/services/{id}
```

### Delete Service

```http
DELETE /api/services/{id}
```

---

## Monitoring Endpoint

### Force Re-check Service

```http
POST /api/services/{id}/check
```

---

# Database Setup

This project uses PostgreSQL running inside Docker.

## Run PostgreSQL Container

```bash
docker compose up -d
```

---

# Configure Database

Example `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/service_monitor
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
```

---

# Run Backend

```bash
./mvnw spring-boot:run
```

Backend will run on:

```txt
http://localhost:8080
```

---

# Monitoring Scheduler

The backend automatically checks all registered services every 30 seconds using Spring Scheduler.

```java
@Scheduled(fixedDelay = 30000)
```

The scheduler will:

* Send HTTP request to monitored services
* Measure latency
* Update UP/DOWN status
* Save response code
* Save last checked timestamp

---

# Architecture Decisions

Spring Boot was chosen because it provides a clean ecosystem for REST API development, scheduling, and database integration.

PostgreSQL was used to simulate a more realistic production environment compared to in-memory databases.

The backend uses layered architecture to separate responsibilities between controller, service, and repository layers.

---

# Challenge Log

One of the biggest challenges during development was handling realtime monitoring updates together with scheduled background checking.

At first, the scheduler was running correctly, but the frontend dashboard did not automatically reflect the latest service status because the data was only fetched once.

This issue was solved by implementing periodic frontend polling using TanStack Query.

Another challenge was handling external service responses. Some public endpoints returned redirects or inconsistent responses, which initially caused incorrect DOWN statuses. This was solved by improving response handling and debugging scheduler behavior using logs.

I also faced issues related to Dockerized PostgreSQL setup and synchronization between frontend polling and backend scheduler updates.

---

# Future Improvements

Possible future improvements:

* Monitoring history logs
* WebSocket realtime updates
* Authentication & authorization
* Search & filtering
* Pagination
* Notification system
* Retry strategy for failed checks

---

# Author

Ikhsan Maulana Akbar

