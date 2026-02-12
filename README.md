# Eco-Tourism Marketplace

## Purpose

Backend platform built on a microservices architecture for an eco-tourism marketplace. The system handles user and provider management, tourism service cataloging with external data enrichment, shopping cart and order processing, payment handling, and a reviews-and-questions module. Inter-service communication uses Apache Kafka for asynchronous event propagation, while Spring Cloud provides service discovery, centralized configuration, and API gateway routing with Keycloak-based authentication.

---

## Architecture

The system follows a microservices architecture orchestrated via Docker Compose. All services register with a Netflix Eureka server and retrieve configuration from a Spring Cloud Config Server (native profile, classpath-based).

```
                            ┌─────────────────┐
                            │  Config Server   │ :8888
                            │ (native profile) │
                            └────────┬────────┘
                                     │ config distribution
     ┌───────────────────────────────┼───────────────────────────────┐
     │              ┌────────────────┼────────────────┐              │
     │              │                │                │              │
     ▼              ▼                ▼                ▼              ▼
┌──────────┐ ┌────────────┐ ┌──────────────┐ ┌───────────┐ ┌────────────┐
│ User     │ │  Service   │ │   Cart       │ │ Review &  │ │  Payment   │
│ Profile  │ │  Catalog   │ │   Service    │ │ Questions │ │  Service   │
│  :8081   │ │   :8082    │ │    :8085     │ │   :8083   │ │   :8087    │
└────┬─────┘ └─────┬──────┘ └──────┬───────┘ └─────┬─────┘ └─────┬──────┘
     │             │               │               │             │
     │             │               │               │             │
     └──────┬──────┴───────┬───────┴───────┬───────┴─────────────┘
            │              │               │
            ▼              ▼               ▼
     ┌────────────┐  ┌──────────┐  ┌──────────────────┐
     │   Eureka   │  │  Kafka   │  │   PostgreSQL 15  │
     │   :8761    │  │  Broker  │  │     :5432        │
     └────────────┘  └──────────┘  └──────────────────┘

            ▲
            │  route + JWT validation
     ┌──────┴──────┐
     │ API Gateway │ :8080   ◄── Keycloak OAuth2 / JWT
     │  (WebFlux)  │
     └─────────────┘
```

Each business service follows a layered structure: `Controller → Service (interface) → ServiceImpl → Repository`, using DTOs and Mapper classes (MapStruct / Lombok) to decouple the API contract from the persistence model. Downstream services replicate upstream data into local cache tables via Kafka events, avoiding synchronous cross-service calls.

### Service Inventory

| Service | Port | Description |
|---|---|---|
| `config-server` | 8888 | Centralized configuration (native profile) |
| `eureka-server` | 8761 | Service discovery |
| `gateway-server` | 8080 | API gateway with Keycloak JWT authentication |
| `user-profile-service` | 8081 | User, Client, Provider, and Role management |
| `service-catalog` | 8082 | Tourism service CRUD with external API enrichment (REST + GraphQL) |
| `cart-service` | 8085 | Shopping cart and order lifecycle |
| `service-review-questions` | 8083 | Reviews (1–5 stars), questions, and answers |
| `payment-service` | 8087 | Payment processing and receipt generation |

---

## Kafka Event Flow

Events propagate across services using named Kafka topics. Producers use `StreamBridge` (Spring Cloud Stream); consumers use `@KafkaListener`.

```
user-profile-service                service-catalog                  service-review-questions
┌──────────────────┐               ┌──────────────────┐              ┌──────────────────────┐
│ UserEventPublisher│──────────────────────────────────────────────►│ UserCreatedConsumer   │
│ (user.created)   │               │                  │              │ → user_cache table    │
│                  │               │                  │              │                      │
│ ProviderEvent    │──────────────►│ ProviderCreated  │              │                      │
│  Publisher       │               │  Consumer        │              │                      │
│ (provider.created)│              │ → provider_cache  │              │                      │
└──────────────────┘               │                  │              │                      │
                                   │ ServiceEvent     │──────────────►│ ServiceCreated       │
                                   │  Publisher       │              │  Consumer            │
                                   │ (service.created)│              │ → service_cache table │
                                   └──────────────────┘              └──────────────────────┘

cart-service                        payment-service
┌──────────────────┐               ┌──────────────────┐
│ MQCheckoutService│──────────────►│ OrderCreated     │
│ (paymentsRequest)│               │  Consumer        │
└──────────────────┘               └──────────────────┘
```


---

## API Layer

All business services expose RESTful endpoints documented via **SpringDoc OpenAPI** (Swagger UI at `/swagger-ui.html` per service).

The `service-catalog` additionally exposes a **GraphQL** API (Spring for GraphQL + Playground) with queries such as `servicesByCategory`, `servicesByCity`, `searchServicesByTitle`, `servicesByPriceRange`, and `activeServices`.

---

## Authentication

Handled at the API Gateway using **Keycloak** (OAuth2 / OpenID Connect). The `KeycloakJwtAuthenticationConverter` extracts `realm_access.roles` from JWT claims and maps them to Spring Security authorities. `SecurityConfig` enforces role-based route authorization. The `user-profile-service` also integrates with the Keycloak Admin Client for user provisioning.

---

## Database

A **PostgreSQL 15** instance (`marketplace-db`) with 19 tables:

| Domain | Tables |
|---|---|
| Users & Roles | `users`, `roles`, `user_role`, `client`, `provider` |
| Service Catalog | `service`, `service_category`, `service_image` |
| Social | `social_network`, `provider_social_network` |
| Reviews & Q&A | `review`, `question`, `answer` |
| Cart & Orders | `cart`, `cart_item`, `orders`, `order_item` |
| Payments | `payment`, `payment_detail`, `receipt` |
| Event Caching | `provider_cache`, `service_cache`, `user_cache` |

---

## Technology Stack

| Layer | Technologies |
|---|---|
| Core | Java 17, Spring Boot 3.5.5 / 3.3.4, Spring Cloud 2025.0.0 / 2023.0.3 |
| Infrastructure | Netflix Eureka, Spring Cloud Config, Spring Cloud Gateway (WebFlux) |
| Messaging | Apache Kafka 2.13-3.9.1, Spring Cloud Stream |
| Data | Spring Data JPA, PostgreSQL 15, Spring for GraphQL |
| Security | Keycloak (OAuth2 / JWT), Spring Security |
| Tooling | MapStruct 1.5.5, Lombok 1.18.36, SpringDoc OpenAPI 2.8.5 |
| DevOps | Docker (eclipse-temurin:17-jdk-alpine), Docker Compose, Jenkins, Maven |
| Testing | JUnit 5, H2 (in-memory), Spring Kafka Test |

---

## Setup

### Prerequisites

- Java 17 (JDK), Apache Maven 3.8+, Docker and Docker Compose
- Apache Kafka (included in `kafka_2.13-3.9.1/` or use an external broker)
- Keycloak instance (realm: `marketplace-realm`)

### Environment Configuration

Configure the `.env` file at the project root:

```env
spring_datasource_url=jdbc:<db_url>
spring_datasource_username=<db_user>
spring_datasource_password=<db_password>
spring_datasource_driver_class_name=org.postgresql.Driver

CONFIG_SERVER_URL=http://config-server:8888
EUREKA_SERVER_URL=http://eureka-server:8761/eureka/

POSTGRES_DB=marketplace-db
POSTGRES_USER=<db_user>
POSTGRES_PASSWORD=<db_password>
```

### Build and Run

```bash
# Build all modules
mvn clean package

# Start all services
docker-compose up -d --build
```

Startup order is managed by Docker Compose `depends_on`: config-server → eureka-server → business services + gateway. The database starts independently.

### CI/CD

The `Jenkinsfile` defines a declarative pipeline: `mvn clean package` → `docker-compose up -d --build`. See [`docs/JENKINS_PIPELINE.md`](docs/JENKINS_PIPELINE.md) and [`docs/EC2_SETUP.md`](docs/EC2_SETUP.md) for details.

---

## Architecture Diagrams

Available in `docs/architecture/`:
- **Component diagrams** — `docs/architecture/components-diagrams/`
- **Domain diagrams** — `docs/architecture/domain-diagrams/`
- **Logical diagrams** — `docs/architecture/logical-diagrams/`
- **Deployment diagram** — `docs/architecture/deployment-diagram.png`

---

## Development Team

| Member              | GitHub Profile | 
|:------------------:|:--------------:|
| Julian Ramos        | [@juliancramos](https://github.com/juliancramos)  
| Sergio Asencio       | [@S3rg100](https://github.com/S3rg100)    
| Santiago Pinto        | [@SantiagoP67](https://github.com/SantiagoP67)


