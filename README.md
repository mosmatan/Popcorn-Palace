# **Instructions**

## **🎮 Popcorn Palace – Setup, Build, and Run Guide**

This document provides step-by-step instructions to set up, build, run, and test the **Popcorn Palace** backend service.

---

## **📋 Prerequisites**

Before you begin, ensure the following tools are installed:

- **Java SDK 21**: [Download here](https://www.oracle.com/java/technologies/downloads/#java21)
- **Java IDE**: e.g., IntelliJ IDEA, Eclipse, or VS Code
- **Docker Desktop**: [Download here](https://www.docker.com/products/docker-desktop/)
- **Git**: [Download here](https://git-scm.com/)
- **Maven**: Comes bundled with most IDEs or install manually: [Install Maven](https://maven.apache.org/install.html)

---

## **🐳 Running with Docker Compose**

To run both the application and the PostgreSQL database using Docker:

### **1. Clone the Repository**

```
git clone https://github.com/mosmatan/Popcorn-Palace
cd popcorn-palace
```

### **2. Start Services**

Use the provided `compose.yml` to start the app and the PostgreSQL database:

```
docker compose up --build
```

This will:

- Build the Spring Boot app using the `Dockerfile`
- Start the app on port `8080`
- Start PostgreSQL with the following credentials:
  - Username: `popcorn-palace`
  - Password: `popcorn-palace`
  - Database: `popcorn-palace`
  - Port: `5432`

---

## **🛠️ Running Locally (Without Docker)**

### **1. Start PostgreSQL manually or use Docker:**

```
docker run --name popcorn-db -e POSTGRES_USER=popcorn-palace -e POSTGRES_PASSWORD=popcorn-palace -e POSTGRES_DB=popcorn-palace -p 5432:5432 -d postgres
```

### **2. Run the App with Maven**

```
./mvnw clean install
./mvnw spring-boot:run
```

---

## **✅ Running Tests**

To execute the test suite (unit and integration tests):

```
./mvnw test
```

> The app uses **H2** in-memory DB for testing and **PostgreSQL** for production by default.

Spring Boot Testing Docs:\
[https://docs.spring.io/spring-boot/reference/testing/index.html](https://docs.spring.io/spring-boot/reference/testing/index.html)

---

## **🔧 Configuration Details**

- **Database connection** is auto-configured via environment variables in `compose.yml`
- **ORM**: Spring Data JPA
- **Test DB**: H2
- **Production DB**: PostgreSQL

---

## **📟 API Documentation**

Below are the main API groups and their functionality:

### 🎬 Movies API
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /movies/all | Retrieve all movies |
| POST | /movies | Add a new movie |
| POST | /movies/update/{movieTitle} | Update an existing movie |
| DELETE | /movies/{movieTitle} | Delete a movie by title |

### **🕒 Showtimes API**
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /showtimes/{showtimeId} | Get a showtime by ID |
| POST | /showtimes | Add a new showtime |
| POST | /showtimes/update/{showtimeId} | Update a showtime |
| DELETE | /showtimes/{showtimeId} | Delete a showtime |

### **🎟️ Bookings API**
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /bookings | Book a ticket for a showtime |

---

## **📬 Contact**

- **LinkedIn**: [www.linkedin.com/in/matan-moskovich](https://www.linkedin.com/in/matan-moskovich)

---
