# Milk Collection System

A Spring Boot application to manage milk collection operations for a dairy business. It includes user management, farmer records, milk collection entries, and export capabilities (PDF/Excel/CSV). The project uses Spring Data JPA with PostgreSQL and Flyway for database migrations.

## 🧱 Tech Stack

- Java 17
- Spring Boot 3.2.3
- Spring Web, Spring Data JPA, Spring Security, Validation
- PostgreSQL (runtime)
- Flyway DB migrations
- JWT authentication (jjwt)
- PDF export (iText 7)
- Excel export (Apache POI)
- CSV export (OpenCSV)
- Lombok (compile-time)

## 🚀 Getting Started

### Prerequisites

- Java 17+ JDK
- Maven (bundled via `mvnw`)
- PostgreSQL database (or configure another datasource)

### Run Locally

From the project root:

```bash
# build
./mvnw.cmd clean package

# run
./mvnw.cmd spring-boot:run
```

### Configuration

Application properties are in `src/main/resources/application.yml`.

Ensure you configure the datasource settings (URL/username/password) for PostgreSQL.

### Database Migrations

Flyway migrations are located in `src/main/resources/db/migration`.
The project includes initial migrations for:
- Admin users
- Farmers
- Milk collections
- OTP records

## 🧪 Tests

Run unit tests with:

```bash
./mvnw.cmd test
```

## 📁 Project Structure

- `src/main/java/com/dairy/milkcollection` - main application code
- `src/main/resources` - configuration and Flyway migrations
- `src/test/java` - tests

## ✅ Notes

- The app currently uses PostgreSQL in production and H2 for tests.
- JWT configuration and security are set up under `security` packages.

---

For more details, inspect the `pom.xml` dependencies and the source packages.
