# 💻 Job Research Application

Job Research Application is a Spring Boot backend for managing job opportunity records, including position, company, location, skills, work mode, status, posted date, description, application URL, salary, and source. It provides REST APIs for the React frontend, supports JWT authentication, role-based access control, CRUD operations, server-side pagination and sorting, and field-based dynamic filtering using Spring Data JPA Specification. Job records are populated both through manual entry from the frontend and through an automated n8n workflow that collects job listings from multiple sources. The backend is packaged as a Docker container, deployed on Render, and integrates with PostgreSQL.


---

## 🧱 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **PostgreSQL (hosted on Supabase)**
- **Spring Data JPA for pagination, sorting, and Specification-based filtering**
- **Maven**
- **CORS Configuration** (for frontend integration)

---

## 🔐 Authentication & Roles

- JWT authentication
- Two roles:
    - `USER`: can view jobs
    - `ADMIN`: can create, update, and delete jobs

---

## 📦 REST API Overview

| Method | Endpoint           | Access     | Description                                                                        |
| ------ | ------------------ | ---------- |------------------------------------------------------------------------------------|
| POST   | `/login`           | Public     | Authenticates the user and returns a JWT                                           |
| GET    | `/health`          | Public     | Lightweight health check, used to wake the backend from idle on Render's free tier |
| GET    | `/api/jobs/filter` | USER/ADMIN | Retrieves a paginated job list with sorting and field-based dynamic filtering      |
| POST   | `/api/jobs`        | ADMIN      | Create new job entry                                                               |
| PUT    | `/api/jobs/{id}`   | ADMIN      | Update existing job                                                                |
| DELETE | `/api/jobs/{id}`   | ADMIN      | Delete job entry                                                                   |

---

## 🤖 Automated Job Ingestion (n8n)
In addition to manual entry through the frontend, job records are written to this backend by an external n8n workflow that collects job listings from multiple sources (Jooble, We Work Remotely, RemoteOK, Remotive, and Himalayas)


- The workflow authenticates via POST /login like any other client, then calls POST /api/jobs for each newly discovered listing.
- The n8n integration uses the following fields:

  - scrapedFrom`: the platform from which the job listing was collected
    (e.g. Jooble, We Work Remotely, RemoteOK), populated by the n8n pipeline.
  - externalJobId: the identifier of the listing on its original platform, used by the workflow to avoid creating duplicate records across runs.



- The GET /health endpoint exists specifically to support this integration: since the backend can spin down when idle on Render's free tier, the workflow pings this endpoint with retries before authenticating, so a cold backend doesn't cause a scheduled run to fail.
---
### Job List Query Parameters

The `/api/jobs/filter` endpoint supports pagination, sorting, and field-based dynamic filtering.

Example:

```http
GET /api/jobs/filter?page=0&size=10&sortBy=postedDate&sortDir=desc&position=frontend&company=supabase&location=remote&mode=Remote&status=APPLIED
```


| Parameter  | Description             |
| ---------- | ----------------------- |
| `page`     | Page number, starting from 0 |
| `size`     | Number of records per page |
| `sortBy`   | Field used for sorting  |
| `sortDir`  | Sort direction: `asc` or `desc` |
| `position` | Filter jobs by position |
| `company`  | Filter jobs by company name |
| `location` | Filter jobs by location |
| `mode`     | Filter jobs by work mode |
| `status`   | Filter jobs by job status |


---

## 🧩 Testing

This project includes tests for `JobService`, `JwtService`, `JWTAuthorizationFilter`, and `JobController`, covering job sorting, JWT generation, authorization filter behavior, and API responses.

### Run tests locally

**Windows:**

```powershell
.\mvnw.cmd test -DskipTests=false
```

macOS / Linux:

```
./mvnw test -DskipTests=false
```

### Current Tests

| Test Class                   | Type                           | What it verifies                                                                |
|------------------------------|--------------------------------|---------------------------------------------------------------------------------|
| `JobServiceTest`             | Unit test (Mockito)            | Fallback to `postedDate` for invalid sort fields, ascending sorting by company, and an exception when updating a non-existent job                   |
| `JwtServiceTest`             | Unit test                      | Generated JWT subject, `roles` claim, and expiration approximately 24 hours after token creation                                   |
| `JWTAuthorizationFilterTest` | Unit test (Mockito)            | Authentication is set for a valid JWT, while requests without a token remain unauthenticated and continue through the filter chain |
| `JobControllerTest`          | Web layer test (`@WebMvcTest`) | `GET /api/jobs/{id}` returns `200 OK` with job data when found and `404 Not Found` when the job does not exist                |


## 🚀 Deployment

The backend is deployed on Render, with PostgreSQL hosted on Supabase.
- Deployed: [`https://jobresearch-backend.onrender.com`]

---

