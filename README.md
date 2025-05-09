# 📋 Jobresearch API

Jobresearch is a Spring Boot backend application for managing and tracking job applications. It supports secure JWT-based authentication, role-based access control, and complete CRUD operations. The backend is packaged as a Docker container, deployed via Render, and integrates with PostgreSQL. 

---

## 🚀 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **PostgreSQL**
- **Maven**
- **Docker**
- **Swagger (SpringDoc)**
- **CORS Configuration** (for frontend integration)

---

## 🔐 Authentication & Roles

- JWT-based authentication
- Two roles:
    - `USER`: can view jobs
    - `ADMIN`: can create, update, and delete jobs

---

## 📦 REST API Overview

| Method | Endpoint            | Access    | Description              |
|--------|---------------------|-----------|--------------------------|
| POST   | `/login`            | Public    | Authenticates user       |
| GET    | `/api/jobs`         | USER/ADMIN | Retrieve job list       |
| POST   | `/api/jobs`         | ADMIN     | Create new job entry     |
| PUT    | `/api/jobs/{id}`    | ADMIN     | Update existing job      |
| DELETE | `/api/jobs/{id}`    | ADMIN     | Delete job entry         |

---

## 🔗 Swagger Documentation

- Local: `http://localhost:8080/swagger-ui/index.html`
- Deployed: [`https://jobresearch-backend.onrender.com`]


---

## 🐳 Docker Deployment

1. Build the JAR:
   ```bash
   mvn clean package -DskipTests

2. Run the container:
   ```bash 
   docker build -t jobresearch 
   docker run -p 8080:8080 jobresearch
