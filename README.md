# Banki - Secure Banking Application

A secure banking application built with Spring Boot and Keycloak for authentication. This application provides account management, beneficiary management, and transaction processing with comprehensive security and audit logging.

## Quick Start

1. **Add domains to your hosts file** (`/etc/hosts` on Linux/macOS, `C:\Windows\System32\drivers\etc\hosts` on Windows):
   ```
   127.0.0.1 banki.local
   127.0.0.1 keycloak.local
   127.0.0.1 admin.keycloak.local
   ```

2. **Start all services**:
   ```bash
   docker compose up -d
   ```

3. **Access the application**:
   - Frontend: https://banki.local
   - API: https://banki.local/api/
   - Swagger UI: https://banki.local/api/swagger-ui/index.html
   - Keycloak Admin: https://admin.keycloak.local (admin/admin-password)

4. **Accept SSL certificates** (required for proper functionality):
   - Open your browser and visit each URL:
     - https://banki.local
     - https://keycloak.local
     - https://admin.keycloak.local
   - Accept the self-signed certificate warnings on each domain
   - This is necessary for the frontend and authentication to work correctly

5. **Get a token and test**:
   ```bash
   curl -k -X POST https://keycloak.local/realms/banki-app/protocol/openid-connect/token \
     -d "grant_type=password" \
     -d "client_id=spring-boot-app" \
     -d "client_secret=vz8Wf5xpk892RuE8RaA6xQ7HwGvDX74D" \
     -d "username=<user>" \
     -d "password=<pass>"
   ```

For detailed setup instructions, see [Getting Started](#getting-started).

## Table of Contents

- [Quick Start](#quick-start)
- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [API Documentation](#api-documentation)
- [Security](#security)
- [Technology Stack](#technology-stack)
- [Troubleshooting](#troubleshooting)
- [Further Reading](#further-reading)

## Features

### Account Management
- Create and manage bank accounts
- View account balance and status
- Multiple accounts per user support

### Beneficiary Management
- Add beneficiaries for easy transfers
- View all beneficiaries
- 
### Transaction Processing
- Create transactions (deposits, withdrawals, transfers)
- View transaction history per account
- Transaction status tracking (PENDING, SUCCESS, FAILED)
- Automatic transaction status updates (simulated processing after 1 minute)
- Transfer to beneficiaries
- 
### Security & Auditing
- OAuth2/JWT authentication via Keycloak
- User auto-provisioning from Keycloak tokens
- OTP (One-Time Password) functionality for user login
- Audit logging for important actions
- IP address tracking
- Custom access denied handling
- SSL/TLS encryption for all communications

### API Documentation
- Interactive Swagger UI
- Custom response examples
- Comprehensive endpoint documentation

## Architecture

The application consists of the following containerized services, all accessed through custom domains via HTTPS (port 443) with automatic HTTP to HTTPS redirection:

```
┌─────────────────────────────────────────────────────────────┐
│              Nginx Proxy (Ports 80→443, 443)                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │ banki.local  │  │keycloak.local│  │admin.keycloak.   │  │
│  │  / (React)   │  │              │  │     local        │  │
│  │  /api/*      │  │              │  │                  │  │
│  └──────┬───────┘  └──────┬───────┘  └────────┬─────────┘  │
└─────────┼──────────────────┼───────────────────┼────────────┘
          │                  │                   │
          ▼                  ▼                   ▼
  ┌───────────────┐  ┌────────────────────────────┐
  │ Spring Boot   │  │       Keycloak             │
  │   Backend     │  │   (Internal: 8080)         │
  │ (Internal)    │◄─┤   - Token Issuer           │
  │               │  │   - Admin Console          │
  └───────┬───────┘  └───────┬────────────────────┘
          │                  │
          ▼                  ▼
  ┌───────────────┐  ┌────────────────┐
  │Application DB │  │  Keycloak DB   │
  │  (Port 5432)  │  │  (Port 5433)   │
  └───────────────┘  └────────────────┘
```

### Services

- **nginx**: Reverse proxy with SSL/TLS termination
  - Listens on port 80 (redirects to HTTPS) and port 443 (SSL/TLS)
  - Routes based on domain:
    - `banki.local/` → React frontend (static files)
    - `banki.local/api/*` → Spring Boot backend
    - `keycloak.local` → Keycloak token issuer
    - `admin.keycloak.local` → Keycloak admin console
- **banki-backend**: Spring Boot application (internal Docker network, exposed via nginx)
- **keycloak**: Keycloak authentication server (internal port 8080, exposed via nginx with SSL)
- **banki-postgres**: PostgreSQL database for application data (Port 5432)
- **keycloak-db**: PostgreSQL database for Keycloak (Port 5433)

### Domain Routing

All services use **HTTPS (port 443)** with domain-based routing. HTTP traffic on port 80 is automatically redirected to HTTPS:

| Domain | Purpose | Backend Target | Protocol |
|--------|---------|----------------|----------|
| `banki.local/` | React Frontend | Static files in nginx | HTTPS |
| `banki.local/api/*` | Spring Boot REST API | `banki-backend:8080` | HTTPS |
| `keycloak.local` | Keycloak token issuer & authentication | `keycloak:8080` | HTTPS |
| `admin.keycloak.local` | Keycloak admin console | `keycloak:8080` | HTTPS |

## Prerequisites

- [Docker](https://www.docker.com/get-started/) and Docker Compose
- Text editor with sudo/admin privileges (for modifying hosts file)

## Getting Started

### Step 1: Configure Hosts File

For the application to work correctly with Keycloak's JWT token validation and to access all services via custom domains, you need to add hostname mappings to your system's hosts file.

#### Linux / macOS

1. Open terminal with sudo privileges:
   ```bash
   sudo nano /etc/hosts
   ```

2. Add the following lines at the end of the file:
   ```
   127.0.0.1 banki.local
   127.0.0.1 keycloak.local
   127.0.0.1 admin.keycloak.local
   ```

3. Save and exit:
   - In nano: Press `Ctrl+X`, then `Y`, then `Enter`
   - In vim: Press `Esc`, type `:wq`, then `Enter`

4. Verify the changes:
   ```bash
   ping banki.local
   ping keycloak.local
   ping admin.keycloak.local
   ```
   You should see responses from `127.0.0.1` for all three domains

#### Windows

1. Open Notepad as Administrator:
   - Press `Windows key`
   - Type "Notepad"
   - Right-click on "Notepad" and select "Run as administrator"

2. Open the hosts file:
   - Click `File` → `Open`
   - Navigate to: `C:\Windows\System32\drivers\etc`
   - Change file type filter to "All Files (*.*)"
   - Select the `hosts` file and click "Open"

3. Add the following lines at the end of the file:
   ```
   127.0.0.1 banki.local
   127.0.0.1 keycloak.local
   127.0.0.1 admin.keycloak.local
   ```

4. Save the file (`Ctrl+S`)

5. Verify the changes (open Command Prompt):
   ```cmd
   ping banki.local
   ping keycloak.local
   ping admin.keycloak.local
   ```
   You should see responses from `127.0.0.1` for all three domains

### Step 2: Environment Configuration

The `.env` file is already configured in the project root

> **Important**: The `KEYCLOAK_FULL_URL` uses `https://keycloak.local` (HTTPS) which is the hostname you added to your hosts file. All services are accessed via HTTPS (port 443) with automatic HTTP to HTTPS redirection.

### Step 3: Start the Application

Start all services using Docker Compose:

```bash
docker compose up -d
```

This will:
1. Build the Spring Boot backend image (first time only)
2. Start PostgreSQL databases
3. Start Keycloak with pre-configured realm and users
4. Start Nginx reverse proxy
5. Start the Spring Boot application

**First-time startup may take a few minutes** as Docker builds the Spring Boot image and Keycloak initializes the realm.

Check the status of all containers:
```bash
docker compose ps
```

View logs for a specific service:
```bash
docker compose logs -f banki-backend
docker compose logs -f keycloak
```

### Step 4: Accept SSL Certificates

**IMPORTANT**: Before using the application, you must manually accept the self-signed SSL certificates for all domains:

1. Open your web browser
2. Visit each of the following URLs:
   - **https://banki.local**
   - **https://keycloak.local**
   - **https://admin.keycloak.local**
3. For each URL, you will see a security warning about the self-signed certificate
4. Click "Advanced" or "Continue to site" to accept the certificate
5. This step is **required** for proper authentication and frontend functionality

> **Why this is necessary**: The application uses self-signed SSL certificates for development. Browsers block requests to HTTPS endpoints with untrusted certificates unless you explicitly accept them. Without accepting all three certificates, authentication and API calls will fail.

### Step 5: Access the Application

Once all services are running and certificates are accepted, you can access:

- **Frontend Application**: https://banki.local
- **Backend API**: https://banki.local/api/
- **Swagger UI**: https://banki.local/api/swagger-ui/index.html
- **API Docs (JSON)**: https://banki.local/api/api-docs
- **Keycloak Admin Console**: https://admin.keycloak.local
  - Username: `permanent_admin`
  - Password: `admin-password`
- **Keycloak Token Endpoint**: https://keycloak.local/realms/banki-app/protocol/openid-connect/token

### Stopping the Application

To stop all services:
```bash
docker compose down
```

To stop and remove all data (volumes):
```bash
docker compose down -v
```

### Keycloak Configuration Changes

**Import a new realm:**
1. Export realm from Keycloak admin console
2. Place JSON files in `keycloak-export/` directory
3. Restart Keycloak:
   ```bash
   docker compose restart keycloak
   ```

## API Documentation

The application uses SpringDoc OpenAPI with custom Swagger annotations for comprehensive API documentation.

### Accessing Swagger UI

Navigate to: **https://banki.local/api/swagger-ui/index.html**

> **Note**: All API endpoints are prefixed with `/api/` when accessed through the domain. Make sure you have accepted the SSL certificate for `banki.local` before accessing Swagger UI.

### Docker Build Optimization

The project uses a **multi-stage Dockerfile** with dependency caching for fast rebuilds:

**Stage 1: Builder**

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS builder
COPY backend/pom.xml .
RUN mvn dependency:go-offline -B  # Cached layer
COPY backend/src ./src
RUN mvn clean package -DskipTests
```

**Stage 2: Runtime**
```dockerfile
FROM eclipse-temurin:21-jre
COPY --from=builder /app/target/*.jar app.jar
```

The `.dockerignore` file prevents cache invalidation from IDE files, build artifacts, and other non-source files.

## Security

### Authentication & Authorization

The application uses **OAuth2 Resource Server** with **JWT tokens** issued by Keycloak.

#### Flow:
1. User authenticates with Keycloak (username/password)
2. Keycloak issues a JWT token with `iss: https://keycloak.local/realms/banki-app`
3. Client includes token in `Authorization: Bearer <token>` header
4. Spring Boot validates token signature and claims against Keycloak
5. User is auto-provisioned in the application database if not exists
6. For sensitive operations (e.g., transactions), OTP verification is required
7. Request proceeds with authenticated user context

#### JWT Validation:
- **Issuer**: Must be `https://keycloak.local/realms/banki-app`
- **Signature**: Validated using Keycloak's public keys (JWK Set)
- **Expiration**: Token must not be expired
- **Claims**: Subject (sub) claim contains user ID
- **Transport Security**: All communications encrypted via SSL/TLS

### User Provisioning

The `UserProvisioningFilter` automatically creates user records in the application database when a user authenticates for the first time. This ensures users from Keycloak are seamlessly integrated.

### Audit Logging

All actions are logged in the `AuditLog` entity with:
- **Action**: Description of what happened
- **User**: Who performed the action
- **IP Address**: IPv4 address of the client
- **Timestamp**: When the action occurred
- **Details**: Additional contextual information

## Technology Stack

### Backend
- **Spring Boot** 3.5.7
- **Java** 21
- **Spring Security** with OAuth2 Resource Server
- **Spring Data JPA** with Hibernate
- **PostgreSQL** 18.0
- **Lombok** for boilerplate reduction
- **SpringDoc OpenAPI** for API documentation

### Authentication
- **Keycloak** 26.3.3
- OAuth2 / OpenID Connect
- JWT tokens

### Infrastructure
- **Docker** & Docker Compose
- **Nginx** as reverse proxy
- **Maven** for build automation

### API Documentation
- **Swagger UI** with custom annotations
- **OpenAPI 3.0** specification

**Project**: Banki - Secure Banking Application  
**Course**: Computer Security - ING2 S1  
**Academic Year**: 2025/2026  
