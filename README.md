# Software Architecture Practice Exercises - Week 09: MICROSERVICES

## Submission Information

- **Full Name**: Dương Hoàng Lan Anh
- **Student ID**: 21087481

---

# Sales Management System (Microservices Architecture)

This project implements a sales management system using a microservices architecture with Java Spring Boot, PostgreSQL, Kafka, Eureka, Spring Security with JWT, and Prometheus/Grafana for monitoring. The system consists of multiple services, each responsible for a specific domain, communicating via REST APIs and Kafka for asynchronous events.

## Table of Contents
- [Architecture Overview](#architecture-overview)
- [Services](#services)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Setup Instructions](#setup-instructions)
- [Running the System](#running-the-system)
- [API Endpoints](#api-endpoints)
- [Monitoring](#monitoring)
- [Troubleshooting](#troubleshooting)
- [Future Enhancements](#future-enhancements)

## Architecture Overview
The system follows a microservices architecture with the following components:
- **API Gateway**: Routes client requests to appropriate services and validates JWT tokens.
- **Product Service**: Manages product information (CRUD operations).
- **Customer Service**: Manages customer information.
- **Order Service**: Manages orders, integrates with Kafka for event-driven stock updates.
- **Auth Service**: Generates JWT tokens for authentication.
- **Eureka Service**: Provides service discovery for dynamic service location.
- **Kafka/Zookeeper**: Enables asynchronous communication (e.g., order creation triggers stock updates).
- **Prometheus/Grafana**: Monitors service metrics and visualizes performance.
- **PostgreSQL**: Each service has its own database (Database per Service principle).

### Diagram
```
Client
   |
   v
[API Gateway] ----> [Eureka Server] (Service Discovery)
   |                  [Product Service] ----> [PostgreSQL] ----> [Kafka (Stock Updates)]
   |                  [Order Service]   ----> [PostgreSQL] ----> [Kafka (Order Events)]
   |                  [Customer Service] ----> [PostgreSQL]
   |                  [Auth Service]
   |                  [Prometheus] <---- Metrics
   |                  [Grafana] <---- Visualization
```

## Services
1. **Product Service** (`product-service`):
   - Manages products (name, price, description, stock).
   - Consumes Kafka events to update stock when orders are created.
   - Port: `8081`
2. **Customer Service** (`customer-service`):
   - Manages customer information (name, email, address, phone).
   - Port: `8082`
3. **Order Service** (`order-service`):
   - Manages orders (create, view, cancel).
   - Publishes order events to Kafka.
   - Port: `8083`
4. **Auth Service** (`auth-service`):
   - Generates JWT tokens for authentication.
   - Port: `8084`
5. **API Gateway** (`api-gateway`):
   - Routes requests and validates JWT tokens.
   - Port: `8080`
6. **Eureka Service** (`eureka-service`):
   - Service discovery server.
   - Port: `8761`
7. **Kafka/Zookeeper**:
   - Asynchronous messaging for order events.
   - Ports: `9092` (Kafka), `2181` (Zookeeper)
8. **Prometheus/Grafana**:
   - Monitoring and visualization.
   - Ports: `9090` (Prometheus), `3000` (Grafana)

## Features
- **Microservices Architecture**: Independent services with separate databases.
- **Service Discovery**: Eureka for dynamic service location.
- **Asynchronous Messaging**: Kafka for order creation and stock updates.
- **Security**: JWT-based authentication with role-based access (`ADMIN`, `USER`).
- **Monitoring**: Prometheus for metrics collection, Grafana for visualization.
- **Containerization**: Docker and Docker Compose for deployment.
- **REST APIs**: CRUD operations for products, customers, and orders.

## Prerequisites
- **Java 17**: OpenJDK or Oracle JDK.
- **Maven**: For building the projects.
- **Docker**: For containerization.
- **Docker Compose**: For orchestrating services.
- **PostgreSQL Client** (optional): For database inspection (e.g., pgAdmin, psql).
- **Postman** or **curl**: For testing APIs.

## Setup Instructions
1. **Clone the Repository** (if applicable):
   ```bash
   git clone <repository-url>
   cd sales-management-system
   ```
   Alternatively, create the directory structure and copy the provided source code into respective folders.

2. **Directory Structure**:
   ```
   sales-management-system/
   ├── product-service/
   ├── customer-service/
   ├── order-service/
   ├── api-gateway/
   ├── eureka-service/
   ├── auth-service/
   ├── prometheus.yml
   ├── docker-compose.yml
   ├── README.md
   ```

3. **Ensure Docker and Docker Compose are Installed**:
   ```bash
   docker --version
   docker-compose --version
   ```

## Running the System
1. **Navigate to the Root Directory**:
   ```bash
   cd sales-management-system
   ```

2. **Build and Run with Docker Compose**:
   ```bash
   docker-compose up --build
   ```
   This will:
   - Build Docker images for all services.
   - Start PostgreSQL, Kafka, Zookeeper, Prometheus, and Grafana containers.
   - Launch all services on the `sales-network` network.

3. **Verify Services**:
   - **Eureka Dashboard**: `http://localhost:8761`
   - **API Gateway**: `http://localhost:8080`
   - **Prometheus**: `http://localhost:9090`
   - **Grafana**: `http://localhost:3000` (default credentials: `admin/admin`)

4. **Obtain a JWT Token**:
   ```bash
   curl -X POST http://localhost:8080/auth/login \
   -H "Content-Type: application/json" \
   -d '{"username":"admin","password":"password"}'
   ```
   Response:
   ```json
   {"token":"eyJhbGciOiJIUzI1NiJ9..."}
   ```

5. **Test APIs** (use the JWT token in the `Authorization` header):
   - **Create a Product**:
     ```bash
     curl -X POST http://localhost:8080/products \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <JWT_TOKEN>" \
     -d '{"name":"Laptop","description":"High-end laptop","price":999.99,"stock":10}'
     ```
   - **Create a Customer**:
     ```bash
     curl -X POST http://localhost:8080/customers \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <JWT_TOKEN>" \
     -d '{"name":"John Doe","email":"john@example.com","address":"123 Main St","phone":"1234567890"}'
     ```
   - **Create an Order**:
     ```bash
     curl -X POST http://localhost:8080/orders \
     -H "Content-Type: application/json" \
     -H "Authorization: Bearer <JWT_TOKEN>" \
     -d '{"customerId":1,"productIds":[1],"totalAmount":999.99,"status":"PENDING"}'
     ```

## API Endpoints
All requests must include the `Authorization: Bearer <JWT_TOKEN>` header except for `/auth/login`.

### Auth Service (`http://localhost:8080/auth`)
- `POST /login`: Authenticate and get JWT token.
  - Body: `{"username":"admin","password":"password"}`

### Product Service (`http://localhost:8080/products`)
- `POST /`: Create a product (ADMIN).
- `GET /{id}`: Get a product by ID (USER).
- `GET /`: Get all products (USER).
- `PUT /{id}`: Update a product (ADMIN).
- `DELETE /{id}`: Delete a product (ADMIN).

### Customer Service (`http://localhost:8080/customers`)
- `POST /`: Create a customer (ADMIN).
- `GET /{id}`: Get a customer by ID (USER).
- `GET /`: Get all customers (USER).
- `PUT /{id}`: Update a customer (ADMIN).
- `DELETE /{id}`: Delete a customer (ADMIN).

### Order Service (`http://localhost:8080/orders`)
- `POST /`: Create an order (USER).
- `GET /{id}`: Get an order by ID (USER).
- `GET /`: Get all orders (USER).
- `PUT /{id}`: Update an order (ADMIN).
- `DELETE /{id}`: Cancel an order (ADMIN).

## Monitoring
1. **Prometheus**:
   - Access: `http://localhost:9090`
   - Check metrics at `/actuator/prometheus` for each service.

2. **Grafana**:
   - Access: `http://localhost:3000`
   - Login: `admin/admin`
   - Add Prometheus as a data source (`http://prometheus:9090`).
   - Create dashboards to visualize metrics (e.g., HTTP request rates, JVM metrics).

## Troubleshooting
- **Service Not Starting**:
  - Check logs: `docker-compose logs <service-name>`
  - Ensure dependencies (e.g., Eureka, Kafka) are running.
- **Database Issues**:
  - Verify PostgreSQL containers are up.
  - Check connection strings in `application.yml`.
- **Kafka Issues**:
  - Ensure Zookeeper and Kafka containers are running.
  - Verify topic `order-created` exists.
- **JWT Errors**:
  - Ensure valid token is included in requests.
  - Check `auth-service` logs for token generation issues.
- **Eureka Issues**:
  - Verify services are registered at `http://localhost:8761`.

## Future Enhancements
- **Advanced Kafka Events**: Use JSON for Kafka messages and add more event types (e.g., order cancellation).
- **Distributed Tracing**: Integrate Zipkin for request tracing.
- **Circuit Breaker**: Add Resilience4j for fault tolerance.
- **CI/CD**: Set up GitHub Actions or Jenkins for automated builds and deployments.
- **User Management**: Enhance `auth-service` with a proper user database.

---

**Developed by**: [Your Name or "xAI Contributor"]
**Date**: April 21, 2025


### Step-by-Step Guide for Running the System
1. **Organize the Project**:
   - Create the directory `sales-management-system`.
   - Place each service's code in its respective folder (`product-service`, `customer-service`, `order-service`, `api-gateway`, `eureka-service`, `auth-service`).
   - Add `prometheus.yml` and `docker-compose.yml` to the root directory.

2. **Build and Run**:
   - Run `docker-compose up --build` in the root directory.
   - Wait for all services to start (check logs for errors).

3. **Access Eureka**:
   - Open `http://localhost:8761` to verify all services (`product-service`, `customer-service`, `order-service`, `api-gateway`, `auth-service`) are registered.

4. **Authenticate**:
   - Use the `/auth/login` endpoint to obtain a JWT token.
   - Include the token in all subsequent requests.

5. **Test APIs**:
   - Use Postman or curl to test CRUD operations for products, customers, and orders.
   - Verify Kafka events by checking stock updates in `product-service` after creating orders.

6. **Monitor the System**:
   - Access Prometheus at `http://localhost:9090` to view metrics.
   - Set up Grafana at `http://localhost:3000` with Prometheus as a data source.
