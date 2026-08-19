# SpareLink API Gateway

[![CI](https://github.com/tadiwanashe-mashongwa/api-gateway/actions/workflows/ci.yml/badge.svg)](https://github.com/tadiwanashe-mashongwa/api-gateway/actions/workflows/ci.yml)
[![Java 21](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot 3.5.5](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen)](https://spring.io/projects/spring-boot)

The SpareLink API Gateway is the single public entry point for the platform. It routes requests to the owning microservice, applies CORS, exposes health checks, and validates Keycloak JWTs for protected routes.

## Routes

| Route | Target | Access |
| --- | --- | --- |
| `/api/parts`, `/api/brands`, `/api/categories` | Catalogue | Public |
| `/api/inventory`, `/api/orders`, `/api/payments` | Inventory, Order, Payment | JWT required |
| `/api/customers`, `/api/shipments`, `/api/notifications` | Customer, Shipping, Notification | JWT required |

## Run

Run the platform Compose stack. Gateway is available on `http://localhost:8088`.

| Resource | URL |
| --- | --- |
| Health | `http://localhost:8088/actuator/health` |
| OpenAPI | `http://localhost:8088/v3/api-docs` |
| Swagger UI | `http://localhost:8088/swagger-ui/index.html` |

## Tests

```powershell
mvn test
```

The route contract test verifies every SpareLink backend service is exposed by the gateway. JaCoCo reports are generated at `target/site/jacoco/index.html`.
