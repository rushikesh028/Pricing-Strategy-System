# Pricing Strategy System

## Overview

Pricing Strategy System is a Machine Learning-powered pricing platform built using Spring Boot and Weka. The application helps businesses optimize product pricing by analyzing market factors such as demand, competitor pricing, and inventory levels.

The system provides intelligent price recommendations, revenue simulation, and multiple pricing strategies through REST APIs and a web-based dashboard.

---

## Key Features

### Product Management

* Create, update, retrieve, and delete products
* Store product information, pricing data, demand metrics, and inventory details

### Machine Learning Price Prediction

* Predict optimal product prices using a Weka Linear Regression model
* Generate data-driven pricing recommendations

### Dynamic Pricing Strategies

* Profit Maximization Strategy
* Market Penetration Strategy
* Competitive Pricing Strategy

### Revenue Simulation

* Simulate expected revenue based on different pricing scenarios
* Compare pricing strategies before implementation

### Dashboard

* Interactive dashboard for product management
* Pricing analysis and recommendation views

### API Documentation

* Swagger UI integration for easy API testing and exploration

---

## Technology Stack

* Java 17
* Spring Boot
* Spring Web MVC
* Spring Data JPA
* Hibernate
* H2 Database / MySQL
* Thymeleaf
* Weka 3.8.6
* Maven
* Docker

---

## Project Architecture

```text
Client/UI
    │
    ▼
Controllers
    │
    ▼
Services
    │
    ├── Pricing Engine
    ├── Revenue Simulator
    └── ML Prediction Service (Weka)
    │
    ▼
Repositories
    │
    ▼
Database (H2/MySQL)
```

---

## Project Structure

```text
src/main/java/com/File/Pricing/Strategy/System
├── controller
├── service
├── repository
├── model
├── ml
└── PricingStrategySystemApplication

src/main/resources
├── application.properties
├── pricing-data.arff
└── templates

frontend/
├── App.js
└── components
```

---

## Prerequisites

* Java 17
* Maven 3.8+
* MySQL (Optional)
* Docker (Optional)

---

## Configuration

Default configuration uses an H2 in-memory database.

```properties
spring.datasource.url=jdbc:h2:mem:pricing_runtime_db;DB_CLOSE_DELAY=-1;MODE=MySQL
spring.datasource.username=${DB_USERNAME:}
spring.datasource.password=${DB_PASSWORD:}
```

For MySQL deployment:

```text
DB_URL=jdbc:mysql://localhost:3306/pricing_db
DB_USERNAME=${DB_USERNAME:}
DB_PASSWORD=${DB_PASSWORD:}
```

---

## Running the Application

### Using Maven

Windows

```bash
mvnw.cmd spring-boot:run
```

Linux/macOS

```bash
./mvnw spring-boot:run
```

### Build the Application

```bash
mvnw clean package
```

---

## Docker Deployment

Build Docker Image

```bash
docker build -t pricing-strategy-system .
```

Run Container

```bash
docker run -d \
-p 8080:8080 \
-e DB_URL=jdbc:mysql://host.docker.internal:3306/pricing_db \
-e DB_USERNAME=${DB_USERNAME:}
-e DB_PASSWORD=${DB_PASSWORD:}
pricing-strategy-system
```

---

## User Interface

### Dashboard

```text
GET /dashboard
```

Displays:

* Product inventory
* Pricing recommendations
* Revenue simulations

### Pricing Results

```text
GET /price/{id}
```

Displays:

* Predicted price
* Pricing explanation
* Strategy comparison

---

## REST APIs

### Product APIs

```text
POST   /api/products
GET    /api/products
GET    /api/products/{id}
PUT    /api/products/{id}
DELETE /api/products/{id}
```

### Pricing APIs

```text
GET /api/pricing/{productId}
GET /api/pricing/simulate/revenue
GET /api/pricing/strategy
```

---

## Swagger Documentation

After application startup:

```text
http://localhost:8080/swagger-ui/index.html
```

OpenAPI Specification:

```text
http://localhost:8080/v3/api-docs
```

---

## Machine Learning Model

The application trains a Weka Linear Regression model during startup using the dataset:

```text
src/main/resources/pricing-data.arff
```

Prediction factors include:

* Base Price
* Demand
* Competitor Price
* Inventory Levels

---

## Future Enhancements

* Real-time competitor price monitoring
* Advanced forecasting models
* Cloud deployment on AWS
* Analytics dashboard
* Role-Based Authentication and Authorization

---

## Author

Rushikesh Asawale

Java Backend Developer | Spring Boot | REST APIs | Docker | Machine Learning
