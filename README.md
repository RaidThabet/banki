# Banki

A secure banking application built with Spring Boot, React, and Keycloak for authentication.

## Prerequisites

- [Docker](https://www.docker.com/get-started/) and Docker Compose
- Java 21 (for local development)
- [Node.js](https://nodejs.org/) version 22 (for frontend development)
- Maven (wrapper included)

## Architecture

The application consists of the following services:

- **banki-backend**: Spring Boot application (runs locally on port 8080)
- **banki-postgres**: PostgreSQL database for application data (port 5432) - Docker
- **keycloak**: Keycloak authentication server (accessed via Nginx) - Docker
- **keycloak-db**: PostgreSQL database for Keycloak (port 5433) - Docker
- **nginx**: Reverse proxy for Keycloak (port 9090) - Docker

> **Note**: Currently, the Spring backend is configured to run locally for development. Only the database and authentication services run in Docker.

## Getting Started

### Environment Configuration

Create a `.env` file in the project root (or use the existing one) with the following variables:

```properties
# Main app database
DB_URL=jdbc:postgresql://localhost
DB_PORT=5432
DB_USERNAME=isimmconnect-user
DB_PASSWORD=isimmconnect-pass
DB_NAME=isimm_connect

# Keycloak database
KEYCLOAK_DB_USERNAME=keycloakdb-user
KEYCLOAK_DB_PASSWORD=keycloakdb-pass
KEYCLOAK_DB_NAME=keycloak

# Keycloak app
KEYCLOAK_FULL_URL=http://localhost:9090
KEYCLOAK_REALM_NAME=banki-app
KEYCLOAK_ADMIN_USER=permanent_admin
KEYCLOAK_ADMIN_PASSWORD=admin-password

# Keycloak client
KEYCLOAK_CLIENT_ID=spring-boot-app
KEYCLOAK_CLIENT_SECRET=vz8Wf5xpk892RuE8RaA6xQ7HwGvDX74D
```

### Running the Application

#### Step 1: Start Infrastructure Services

Start PostgreSQL and Keycloak services using Docker Compose:

```bash
docker compose up
```

This will start:
- PostgreSQL database (port 5432)
- Keycloak with its database (accessible at http://localhost:9090)

#### Step 2: Run Spring Boot Backend Locally

After the infrastructure services are running, start the Spring Boot application:

```bash
./mvnw spring-boot:run
```

Or if using IntelliJ IDEA:
1. Install the Lombok plugin and enable annotation processing - [learn more](https://bootify.io/next-steps/spring-boot-with-lombok.html)
2. Run the `BankiApplication` main class
3. For custom configuration, add `-Dspring.profiles.active=local` in VM options and create an `application-local.yml` file

The application will be available at:
- **Backend API**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui/docs.html
- **API Docs**: http://localhost:8080/api-docs
- **Keycloak Admin Console**: http://localhost:9090 (username: `admin`, password: `admin-password`)

#### Stopping the Services

To stop the Docker services:

```bash
docker compose down -v
```

Stop the Spring Boot application with `Ctrl+C` in the terminal or stop the run configuration in your IDE.

### Docker Image (For Future Deployment)

> **Note**: Docker deployment for the Spring backend is currently disabled. Use local development setup for now.

When ready, you can build a Docker image using Spring Boot plugin:

```bash
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=tp.securite/banki
```

Or use the provided Dockerfile:

```bash
docker build -f spring.Dockerfile -t tp.securite/banki .
```

When running the container, ensure you set the appropriate environment variables or mount a `.env` file.

## Technology Stack

- **Backend**: Spring Boot 3.5.7 (Java 21)
- **Database**: PostgreSQL 18.0
- **Authentication**: Keycloak 26.3.3 with OAuth2/JWT
- **Frontend**: React with TypeScript
- **Styling**: Tailwind CSS
- **Build Tools**: Maven, Webpack
- **API Documentation**: SpringDoc OpenAPI (Swagger)

## Security

The application uses OAuth2 Resource Server with JWT tokens issued by Keycloak. The backend validates JWT tokens against the Keycloak realm configured in the environment variables.

To access protected endpoints, include a valid JWT token in the Authorization header:

```
Authorization: Bearer <your-jwt-token>
```

## API Documentation

Once the application is running, access the interactive API documentation at:

- **Swagger UI**: http://localhost:8080/swagger-ui/docs.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Further Reading

* [Maven docs](https://maven.apache.org/guides/index.html)
* [Spring Boot reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
* [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
* [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
* [Keycloak Documentation](https://www.keycloak.org/documentation)
* [PostgreSQL Documentation](https://www.postgresql.org/docs/)  
