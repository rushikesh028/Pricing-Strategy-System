# Pricing Strategy System

A Spring Boot application that manages products and calculates AI-assisted pricing using a Weka linear regression model.

## Tech Stack

- Java 17
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- MySQL (runtime)
- Thymeleaf (dashboard UI)
- Weka (`weka-stable` 3.8.6)
- Maven Wrapper (`mvnw`, `mvnw.cmd`)

## Features

- CRUD APIs for products
- AI price prediction endpoint
- Strategy-based pricing (`PROFIT`, `PENETRATION`, `COMPETITIVE`)
- Revenue simulation endpoint
- Basic dashboard and pricing result pages

## Project Structure

```text
src/main/java/com/File/Pricing/Strategy/System
  controller/   # REST + dashboard controllers
  ml/           # Weka model training and prediction
  model/        # JPA entities
  repository/   # Spring Data repositories
  service/      # business logic

src/main/resources
  application.properties
  pricing-data.arff
  templates/    # Thymeleaf views

frontend/
  App.js
  components/   # standalone React source (optional separate UI)
```

## Prerequisites

1. JDK 17 installed and available on PATH
2. MySQL running locally
3. Database created:
   - `pricing_db`

## Configuration

Current DB settings are in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:pricing_runtime_db;DB_CLOSE_DELAY=-1;MODE=MySQL
spring.datasource.username=sa
spring.datasource.password=
```

For MySQL, override with env vars:

```text
DB_URL=jdbc:mysql://localhost:3306/pricing_db
DB_USERNAME=root
DB_PASSWORD=your_password
```

The app runs on:

- `http://localhost:8080` (or the value of `PORT`)

## Run Locally

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

macOS/Linux:

```bash
./mvnw spring-boot:run
```

## Build and Test

Build:

```powershell
.\mvnw.cmd clean package
```

Test:

```powershell
.\mvnw.cmd test
```

Tests use H2 in-memory DB (`src/test/resources/application.properties`), so they do not require MySQL.

## Docker Deployment

Build image:

```powershell
docker build -t pricing-strategy-system:latest .
```

Run container:

```powershell
docker run -d --name pricing-strategy-system ^
  -p 8080:8080 ^
  -e DB_URL=jdbc:mysql://host.docker.internal:3306/pricing_db ^
  -e DB_USERNAME=root ^
  -e DB_PASSWORD=your_password ^
  pricing-strategy-system:latest
```

Optional JVM tuning:

```powershell
-e JAVA_OPTS="-Xms256m -Xmx512m"
```

## UI Endpoints

- `GET /dashboard` - list products and simulate revenue
- `GET /price/{id}` - show predicted price and explanation

## REST API

### Product API (`/api/products`)

- `POST /api/products`
- `GET /api/products`
- `GET /api/products/{id}`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`

Example create request:

```http
POST /api/products
Content-Type: application/json

{
  "name": "Wireless Mouse",
  "imageUrl": "https://example.com/mouse.jpg",
  "basePrice": 999,
  "demand": 80,
  "competitorPrice": 950,
  "stock": 30
}
```

### Pricing API (`/api/pricing`)

- `GET /api/pricing/{productId}` - predicted price + explanation
- `GET /api/pricing/simulate/revenue?productId={id}&price={price}` - revenue simulation
- `GET /api/pricing/strategy?productId={id}&strategy={PROFIT|PENETRATION|COMPETITIVE}` - strategy pricing

## API Documentation (Swagger)

After starting the app, open:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Notes

- The ML model is trained on startup from `src/main/resources/pricing-data.arff`.
- If the ARFF file is missing or invalid, startup fails with a clear error.

## IP Protection

- This project is now marked as proprietary with an `All rights reserved` [LICENSE](LICENSE).
- Source files are excluded from `git archive` exports via `.gitattributes` (`export-ignore`), so generated archive downloads can be limited.
- Runtime credentials are no longer hardcoded; use `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` environment variables.

Important limitation: no technical control can fully prevent copying once a person has source access (for example through `git clone`). To actually enforce this, keep the repository private and only distribute built artifacts.
