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
   - API: http://banki.local/api/
   - Swagger UI: http://banki.local/api/swagger-ui/index.html
   - Keycloak Admin: http://admin.keycloak.local (permanent_admin/admin-password)

4. **Get a token and test**:
   ```bash
   curl -X POST http://keycloak.local/realms/banki-app/protocol/openid-connect/token \
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
- [Project Structure](#project-structure)
- [Security](#security)
- [Technology Stack](#technology-stack)
- [Troubleshooting](#troubleshooting)
- [Further Reading](#further-reading)

## Features

### Account Management
- Create and manage bank accounts
- View account balance and status
- Enable/disable accounts
- Multiple accounts per user support

### Beneficiary Management
- Add beneficiaries for easy transfers
- View all beneficiaries
- Remove beneficiaries
- Secure beneficiary validation

### Transaction Processing
- Create transactions (deposits, withdrawals, transfers)
- View transaction history per account
- Transaction status tracking (PENDING, SUCCESS, FAILED)
- Automatic transaction status updates (simulated processing after 1 minute)
- Transfer to beneficiaries

### Security & Auditing
- OAuth2/JWT authentication via Keycloak
- User auto-provisioning from Keycloak tokens
- Audit logging for all actions
- IP address tracking
- Custom access denied handling
- Role-based access control

### API Documentation
- Interactive Swagger UI
- Custom response examples
- Comprehensive endpoint documentation

## Architecture

The application consists of the following containerized services, all accessed through custom domains on port 80:

```
┌─────────────────────────────────────────────────────────────┐
│                      Nginx Proxy (Port 80)                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────┐  │
│  │ banki.local  │  │keycloak.local│  │admin.keycloak.   │  │
│  │    /api/*    │  │              │  │     local        │  │
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

- **nginx**: Reverse proxy listening on port 80, routing to different services based on domain
  - `banki.local/api/*` → Spring Boot backend
  - `keycloak.local` → Keycloak token issuer
  - `admin.keycloak.local` → Keycloak admin console
- **banki-backend**: Spring Boot application (internal Docker network, exposed via nginx)
- **keycloak**: Keycloak authentication server (internal port 8080, exposed via nginx)
- **banki-postgres**: PostgreSQL database for application data (Port 5432)
- **keycloak-db**: PostgreSQL database for Keycloak (Port 5433)

### Domain Routing

All services use **port 80** (standard HTTP) with domain-based routing:

| Domain | Purpose | Backend Target |
|--------|---------|----------------|
| `banki.local/api/*` | Spring Boot REST API | `banki-backend:8080` |
| `keycloak.local` | Keycloak token issuer & authentication | `keycloak:8080` |
| `admin.keycloak.local` | Keycloak admin console | `keycloak:8080` |

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

The `.env` file is already configured in the project root. Verify it contains:

```properties
PRODUCTION=false
API_PATH=

# Main app database
DB_URL=jdbc:postgresql://banki-postgres
DB_PORT=5432
DB_USERNAME=isimmconnect-user
DB_PASSWORD=isimmconnect-pass
DB_NAME=isimm_connect

# Keycloak database
KEYCLOAK_DB_USERNAME=keycloakdb-user
KEYCLOAK_DB_PASSWORD=keycloakdb-pass
KEYCLOAK_DB_NAME=keycloak

# Keycloak app
KEYCLOAK_FULL_URL=http://keycloak.local
KEYCLOAK_REALM_NAME=banki-app
KEYCLOAK_ADMIN_USER=admin
KEYCLOAK_ADMIN_PASSWORD=admin-password

# Keycloak client
KEYCLOAK_CLIENT_ID=spring-boot-app
KEYCLOAK_CLIENT_SECRET=vz8Wf5xpk892RuE8RaA6xQ7HwGvDX74D
```

> **Important**: The `KEYCLOAK_FULL_URL` uses `http://keycloak.local` (port 80, standard HTTP) which is the hostname you added to your hosts file. All services are accessed via port 80 through different domains.

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

**First-time startup may take 2-3 minutes** as Docker builds the Spring Boot image and Keycloak initializes the realm.

Check the status of all containers:
```bash
docker compose ps
```

View logs for a specific service:
```bash
docker compose logs -f banki-backend
docker compose logs -f keycloak
```

### Step 4: Access the Application

Once all services are running, you can access them through their respective domains:

- **Backend API**: http://banki.local/api/
- **Swagger UI**: http://banki.local/api/swagger-ui/index.html
- **API Docs (JSON)**: http://banki.local/api/api-docs
- **Keycloak Admin Console**: http://admin.keycloak.local
  - Username: `admin`
  - Password: `admin-password`
- **Keycloak Token Endpoint**: http://keycloak.local/realms/banki-app/protocol/openid-connect/token

> **Note**: All services use **port 80** (standard HTTP), so you don't need to specify a port in the URLs.

### Step 5: Testing with Postman or cURL

#### 1. Get an Access Token

**Using Postman:**
```http
POST http://keycloak.local/realms/banki-app/protocol/openid-connect/token
Content-Type: application/x-www-form-urlencoded

grant_type=password
&client_id=spring-boot-app
&client_secret=vz8Wf5xpk892RuE8RaA6xQ7HwGvDX74D
&username=<keycloak-username>
&password=<keycloak-password>
```

**Using cURL:**
```bash
curl -X POST http://keycloak.local/realms/banki-app/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=spring-boot-app" \
  -d "client_secret=vz8Wf5xpk892RuE8RaA6xQ7HwGvDX74D" \
  -d "username=<keycloak-username>" \
  -d "password=<keycloak-password>"
```

#### 2. Use the Token

Copy the `access_token` from the response and include it in subsequent API requests:

```
Authorization: Bearer <your-access-token>
```

#### 3. Example API Calls

**Get all accounts:**
```http
GET http://banki.local/api/accounts
Authorization: Bearer <your-access-token>
```

**Create a new account:**
```http
POST http://banki.local/api/accounts
Authorization: Bearer <your-access-token>
Content-Type: application/json

{
  "balance": 1000.0
}
```

**Get transactions for an account:**
```http
GET http://banki.local/api/accounts/{account_id}/transactions
Authorization: Bearer <your-access-token>
```

### Stopping the Application

To stop all services:
```bash
docker compose down
```

To stop and remove all data (volumes):
```bash
docker compose down -v
```

## Development Workflow

### Making Code Changes

The project is configured with Docker build caching for fast development iterations:

1. **Edit your Java code** in `src/main/java/`

2. **Rebuild and restart** the backend container:
   ```bash
   docker compose up -d --build banki-backend
   ```

3. **View logs** to verify changes:
   ```bash
   docker compose logs -f banki-backend
   ```

**Build times:**
- First build: ~2-3 minutes (downloads all dependencies)
- Subsequent builds (code changes only): ~10-30 seconds
- Dependency changes (`pom.xml` modified): ~1-2 minutes

### Working with the Database

**Access PostgreSQL directly:**
```bash
# Application database
docker exec -it banki-postgres psql -U isimmconnect-user -d isimm_connect

# Keycloak database
docker exec -it banki-keycloak-db psql -U keycloakdb-user -d keycloak
```

**Reset the database:**
```bash
docker compose down -v
docker compose up -d
```

### Viewing Logs

**All services:**
```bash
docker compose logs -f
```

**Specific service:**
```bash
docker compose logs -f banki-backend
docker compose logs -f keycloak
docker compose logs -f nginx
```

**Filter logs:**
```bash
# Show only errors
docker compose logs banki-backend | grep -i error

# Show last 50 lines
docker compose logs --tail=50 banki-backend
```

### Adding New Dependencies

1. Add dependency to `pom.xml`
2. Rebuild the container:
   ```bash
   docker compose build --no-cache banki-backend
   docker compose up -d
   ```

### Keycloak Configuration Changes

**Import a new realm:**
1. Export realm from Keycloak admin console
2. Place JSON files in `keycloak-export/` directory
3. Restart Keycloak:
   ```bash
   docker compose restart keycloak
   ```

**Reset Keycloak data:**
```bash
docker compose down -v
docker compose up -d
```

## API Documentation

The application uses SpringDoc OpenAPI with custom Swagger annotations for comprehensive API documentation.

### Accessing Swagger UI

Navigate to: **http://banki.local/api/swagger-ui/index.html**

> **Note**: All API endpoints are prefixed with `/api/` when accessed through the domain.

### Available Endpoints

#### Accounts API (`/api/accounts`)
- `GET /api/accounts` - List all accounts for authenticated user
- `GET /api/accounts/{account_id}` - Get specific account details
- `POST /api/accounts` - Create a new account
- `PUT /api/accounts/{account_id}/enable` - Enable an account
- `PUT /api/accounts/{account_id}/disable` - Disable an account
- `DELETE /api/accounts/{account_id}` - Delete an account

#### Beneficiaries API (`/api/beneficiaries`)
- `GET /api/beneficiaries` - List all beneficiaries
- `POST /api/beneficiaries` - Add a new beneficiary
- `DELETE /api/beneficiaries/{beneficiary_id}` - Remove a beneficiary

#### Transactions API (`/api/accounts/{account_id}/transactions`)
- `GET /api/accounts/{account_id}/transactions` - List all transactions for an account
- `POST /api/accounts/{account_id}/transactions` - Create a new transaction

### Swagger Features

- **Custom Response Examples**: Each endpoint includes realistic JSON response examples
- **Error Documentation**: 400, 401, 403, 404, and 500 errors documented with examples
- **Request/Response Schemas**: Full DTO schemas with field descriptions
- **Authentication**: Built-in OAuth2 authentication in Swagger UI

## Project Structure

```
banki/
├── src/
│   └── main/
│       ├── java/tp/securite/banki/
│       │   ├── BankiApplication.java          # Main application class
│       │   ├── config/                        # Configuration classes
│       │   │   ├── SecurityConfig.java        # Spring Security configuration
│       │   │   ├── JwtAuthConverter.java      # JWT to Authentication converter
│       │   │   ├── DomainConfig.java          # JPA Auditing config
│       │   │   └── CustomAccessDeniedHandler.java
│       │   ├── controller/                    # REST Controllers
│       │   │   ├── AccountController.java
│       │   │   ├── BeneficiaryController.java
│       │   │   ├── TransactionController.java
│       │   │   └── GlobalExceptionHandler.java
│       │   ├── domain/                        # JPA Entities
│       │   │   ├── Account.java
│       │   │   ├── Beneficiary.java
│       │   │   ├── Transaction.java
│       │   │   ├── User.java
│       │   │   └── AuditLog.java
│       │   ├── model/                         # DTOs
│       │   │   ├── AccountDTO.java
│       │   │   ├── BeneficiaryDTO.java
│       │   │   ├── TransactionDTO.java
│       │   │   └── CreateTransactionDTO.java
│       │   ├── service/                       # Business logic
│       │   │   ├── AccountsService.java
│       │   │   ├── BeneficiaryService.java
│       │   │   └── TransactionService.java
│       │   ├── repos/                         # JPA Repositories
│       │   ├── filters/                       # Security filters
│       │   │   └── UserProvisioningFilter.java
│       │   ├── swagger/                       # Swagger annotations
│       │   │   ├── AccountControllerResponses.java
│       │   │   ├── BeneficiaryControllerResponses.java
│       │   │   └── TransactionControllerResponses.java
│       │   ├── exceptions/                    # Custom exceptions
│       │   │   ├── BusinessException.java
│       │   │   └── ErrorCode.java
│       │   └── util/                          # Utility classes
│       └── resources/
│           └── application.yml                # Application configuration
├── keycloak-export/                           # Keycloak realm export
│   ├── banki-app-realm.json                  # Realm configuration
│   └── banki-app-users-0.json                # Pre-configured users
├── docker-compose.yml                         # Docker Compose configuration
├── spring.Dockerfile                          # Backend Dockerfile (multi-stage build)
├── .dockerignore                              # Docker build context exclusions
├── nginx.conf                                 # Nginx reverse proxy configuration
├── .env                                       # Environment variables
├── pom.xml                                    # Maven configuration
└── README.md                                  # This file
```

### Docker Build Optimization

The project uses a **multi-stage Dockerfile** with dependency caching for fast rebuilds:

**Stage 1: Builder**
```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS builder
COPY pom.xml .
RUN mvn dependency:go-offline -B  # Cached layer
COPY src ./src
RUN mvn clean package -DskipTests
```

**Stage 2: Runtime**
```dockerfile
FROM eclipse-temurin:21-jre
COPY --from=builder /app/target/*.jar app.jar
```

**Benefits:**
- Maven dependencies are cached and only re-downloaded when `pom.xml` changes
- Source code changes only trigger recompilation, not dependency download
- Final image is smaller (JRE instead of full JDK + Maven)
- Typical rebuild time: **10-30 seconds** (vs 2-3 minutes for full build)

The `.dockerignore` file prevents cache invalidation from IDE files, build artifacts, and other non-source files.

## Security

### Authentication & Authorization

The application uses **OAuth2 Resource Server** with **JWT tokens** issued by Keycloak.

#### Flow:
1. User authenticates with Keycloak (username/password)
2. Keycloak issues a JWT token with `iss: http://keycloak.local/realms/banki-app`
3. Client includes token in `Authorization: Bearer <token>` header
4. Spring Boot validates token signature and claims against Keycloak
5. User is auto-provisioned in the application database if not exists
6. Request proceeds with authenticated user context

#### JWT Validation:
- **Issuer**: Must be `http://keycloak.local/realms/banki-app`
- **Signature**: Validated using Keycloak's public keys (JWK Set)
- **Expiration**: Token must not be expired
- **Claims**: Subject (sub) claim contains user ID

### User Provisioning

The `UserProvisioningFilter` automatically creates user records in the application database when a user authenticates for the first time. This ensures users from Keycloak are seamlessly integrated.

### Audit Logging

All actions are logged in the `AuditLog` entity with:
- **Action**: Description of what happened
- **User**: Who performed the action
- **IP Address**: IPv4 address of the client
- **Timestamp**: When the action occurred
- **Details**: Additional contextual information

### Access Control

- All endpoints require authentication (except Swagger UI)
- Users can only access their own accounts, beneficiaries, and transactions
- Account ownership is validated on every request
- Custom access denied handler provides clear error messages

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

## Troubleshooting

### Issue: "Unable to connect" to domains (banki.local, keycloak.local, admin.keycloak.local)

**Cause**: Either services are not running or hosts file is not configured correctly.

**Solution**:
```bash
# Check if all services are running
docker compose ps

# Check Keycloak logs
docker compose logs keycloak

# Check Nginx logs
docker compose logs nginx

# Check backend logs
docker compose logs banki-backend

# Verify hosts file configuration
ping banki.local
ping keycloak.local
ping admin.keycloak.local
```

Wait for Keycloak to fully initialize (look for "Started Keycloak" in logs). Ensure all three domains are in your `/etc/hosts` file.

### Issue: "Token validation failed" or "Invalid issuer"

**Cause**: The custom domains are not configured in your hosts file, or the `KEYCLOAK_FULL_URL` in `.env` is incorrect.

**Solution**: 
1. Verify your hosts file contains all three domains:
   ```
   127.0.0.1 banki.local
   127.0.0.1 keycloak.local
   127.0.0.1 admin.keycloak.local
   ```
2. Verify `.env` has `KEYCLOAK_FULL_URL=http://keycloak.local`
3. Restart services: `docker compose restart`

See [Step 1](#step-1-configure-hosts-file) for detailed instructions.

### Issue: 401 Unauthorized when accessing Swagger UI or API endpoints

**Cause**: Spring Boot cannot resolve `keycloak.local` from inside the Docker container.

**Solution**: The `docker-compose.yml` includes `extra_hosts` mapping for the backend container. Verify it exists:
```yaml
banki-backend:
  extra_hosts:
    - "keycloak.local:172.17.0.1"
```

If issues persist, check backend logs:
```bash
docker compose logs banki-backend | grep -i error
```

### Issue: "Port already in use" error (port 80, 5432, or 5433)

**Cause**: Another application is using one of the required ports.

**Solution**:
```bash
# Check which process is using the port
sudo lsof -i :80      # Linux/macOS
sudo lsof -i :5432    # PostgreSQL
sudo lsof -i :5433    # Keycloak DB

# Windows
netstat -ano | findstr :80
netstat -ano | findstr :5432
netstat -ano | findstr :5433

# Stop the conflicting process or change ports in docker-compose.yml
```

> **Note**: Port 80 requires sudo/admin privileges. If you cannot use port 80, update the `nginx` ports in `docker-compose.yml` to something like `8080:80` and access via `http://banki.local:8080/api/`.

### Issue: Docker rebuild takes too long (re-downloading dependencies)

**Cause**: Docker cache is invalidated by changes in non-source files.

**Solution**: The project includes a `.dockerignore` file that prevents cache invalidation. Verify it exists and contains:
```dockerignore
target/
.idea/
.git/
node_modules/
.env
```

The `spring.Dockerfile` uses multi-stage caching:
- Maven dependencies are cached in a separate layer
- Only changes to `pom.xml` trigger dependency re-download
- Code changes only rebuild the application, not dependencies

**To verify caching works:**
```bash
# First build
docker compose build banki-backend

# Make a code change in src/
# Then rebuild - should see "CACHED" for dependency layer
docker compose build banki-backend
```

You should see output like:
```
=> CACHED [builder 3/5] COPY pom.xml .
=> CACHED [builder 4/5] RUN mvn dependency:go-offline -B
```

### Issue: Changes to code not reflected after rebuild

**Cause**: Container is using old image or needs restart.

**Solution**:
```bash
# Rebuild and restart the specific service
docker compose up -d --build banki-backend

# Or full restart
docker compose down
docker compose up -d --build
```

### Issue: Database connection errors

**Cause**: Database containers may not be ready when the backend starts.

**Solution**: Spring Boot will automatically retry. If issues persist:
```bash
# Restart backend only
docker compose restart banki-backend

# Or check database logs
docker compose logs banki-postgres
docker compose logs keycloak-db
```

### Issue: Keycloak realm not imported or users missing

**Cause**: Realm files in `keycloak-export/` may be corrupted, missing, or not mounted correctly.

**Solution**:
```bash
# Verify export files exist
ls -la keycloak-export/

# Should contain:
# - banki-app-realm.json
# - banki-app-users-0.json

# Remove volumes and restart
docker compose down -v
docker compose up -d

# Check Keycloak logs for import messages
docker compose logs keycloak | grep -i import
```

### Issue: Swagger redirects incorrectly or shows duplicate paths

**Cause**: Spring Boot context path configuration or nginx rewrite rules.

**Solution**: Ensure your `application.yml` has:
```yaml
server:
  forward-headers-strategy: framework
```

And nginx includes the `X-Forwarded-Prefix` header:
```nginx
proxy_set_header X-Forwarded-Prefix /api;
```

Access Swagger at: `http://banki.local/api/swagger-ui/index.html`

### Issue: IP address showing as `0:0:0:0:0:0:0:1` (IPv6 localhost)

**Cause**: Java prefers IPv6 by default when running locally.

**Solution**: This is expected behavior. The application automatically converts IPv6 localhost (`::1`) to IPv4 format (`127.0.0.1`) for audit logging. No action needed.

## Further Reading

### Spring Framework
* [Spring Boot Reference](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/)
* [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
* [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)

### Authentication
* [Keycloak Documentation](https://www.keycloak.org/documentation)
* [OAuth 2.0 RFC](https://datatracker.ietf.org/doc/html/rfc6749)
* [JWT Introduction](https://jwt.io/introduction)

### Database & Infrastructure
* [PostgreSQL Documentation](https://www.postgresql.org/docs/)
* [Docker Documentation](https://docs.docker.com/)
* [Nginx Documentation](https://nginx.org/en/docs/)

### Build Tools
* [Maven Documentation](https://maven.apache.org/guides/index.html)
* [Lombok Features](https://projectlombok.org/features/)

### API Documentation
* [SpringDoc OpenAPI](https://springdoc.org/)
* [OpenAPI Specification](https://swagger.io/specification/)

---

**Project**: Banki - Secure Banking Application  
**Course**: Sécurité - ING2 S1  
**Year**: 2025  
