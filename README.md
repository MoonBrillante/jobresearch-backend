# 📋 Job Research Application

Job Research Application is a Spring Boot backend for managing job opportunity records, including position, company, location, skills, work mode, status, posted date, and description. It provides REST APIs for the React frontend, supports JWT-based authentication, role-based access control, CRUD operations, paginated job retrieval, backend sorting, and field-based dynamic filtering using Spring Data JPA Specification. The backend is packaged as a Docker container, deployed on Render, and integrates with PostgreSQL.


---

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **PostgreSQL (hosted on Supabase)**
- **Spring Data JPA for pagination, sorting, and Specification-based filtering**
- **Maven**
- **CORS Configuration** (for frontend integration)

---

## 🔐 Authentication & Roles

- JWT-based authentication
- Two roles:
    - `USER`: can view jobs
    - `ADMIN`: can create, update, and delete jobs

---

## 📦 REST API Overview

| Method | Endpoint           | Access    | Description              |
|--------|--------------------|-----------|--------------------------|
| POST   | `/login`           | Public    | Authenticates user and returns JWT token      |
| GET    | `/api/jobs/filter` | USER/ADMIN | Retrieve paginated job list with sorting and field-based filtering       |
| POST   | `/api/jobs`        | ADMIN     | Create new job entry     |
| PUT    | `/api/jobs/{id}`   | ADMIN     | Update existing job      |
| DELETE | `/api/jobs/{id}`   | ADMIN     | Delete job entry         |

---

---
### Job List Query Parameters

The `/api/jobs/filter` endpoint supports pagination, sorting, and field-based filtering.

Example:

```http
GET /api/jobs/filter?page=0&size=10&sortBy=postedDate&sortDir=desc&position=frontend&company=supabase&location=remote&mode=Remote&status=APPLIED
```


| Parameter  | Description                     |
| ---------- | ------------------------------- |
| `page`     | Page number, starting from 0    |
| `size`     | Number of records per page      |
| `sortBy`   | Field used for sorting          |
| `sortDir`  | Sort direction: `asc` or `desc` |
| `position` | Filter jobs by position/title   |
| `company`  | Filter jobs by company name     |
| `location` | Filter jobs by location         |
| `mode`     | Filter jobs by work mode        |
| `status`   | Filter jobs by job status       |

---

---

## 🚀 Deployment

The backend is deployed on Render, with PostgreSQL hosted on Supabase.
- Deployed: [`https://jobresearch-backend.onrender.com`]

---

