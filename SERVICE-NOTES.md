# Inventory Service — Detailed Notes

## 1. Purpose

`inventory-service` is a standalone Spring Boot microservice that answers one question: **does a product have enough inventory for a requested quantity?** It reads inventory from PostgreSQL and exposes the result over HTTP.

The service is intentionally read-only at the API level. It does not reserve stock, decrement quantities, or create orders.

## 2. Technology and runtime

- Java 17
- Spring Boot 3.3.5
- Spring Web for the REST endpoint
- Spring Data JPA and Hibernate for persistence
- PostgreSQL as the runtime database
- Jakarta Bean Validation for request validation
- Maven for build and dependency management
- Default HTTP port: `8082`

The application starts at `InventoryServiceApplication`. Spring component scanning discovers the controller, service, repository, and exception handler below the `com.javacodeex.inventory` package.

## 3. Request flow

```text
HTTP GET
  -> InventoryController.checkStock
  -> InventoryService.checkAvailability
  -> InventoryRepository.findByProductCode
  -> PostgreSQL inventory table
  -> InventoryResponse
  -> JSON response
```

The controller accepts the product code from the URL and the requested quantity from the query string. The service validates the quantity, loads the product, and compares the stored quantity with the requested quantity. The response reports the stored quantity and a boolean result; it does not modify the database.

## 4. API contract

### Check product availability

```http
GET /api/inventory/{productCode}?quantity={positive integer}
```

Example:

```http
GET http://localhost:8082/api/inventory/P1004?quantity=2
```

Successful response (`200 OK`):

```json
{
  "productCode": "P1004",
  "productName": "Shoes",
  "availableQuantity": 10,
  "available": true
}
```

When the requested quantity is greater than the stored quantity, the endpoint still returns `200 OK`; `available` is `false`.

## 5. Validation and errors

`quantity` is annotated with `@Positive`, so a missing, zero, negative, or otherwise invalid request parameter is rejected by Spring validation. The service also checks for `null` or non-positive values so the business rule remains enforced when the service is called directly.

Known error responses are produced by `GlobalExceptionHandler`:

| Condition | Status | Error code |
| --- | ---: | --- |
| Product code is not found | `404` | `PRODUCT_NOT_FOUND` |
| Quantity is null or not positive | `400` | `BAD_REQUEST` |

Error body shape:

```json
{
  "timestamp": "2026-10-24T10:15:30.123",
  "status": 404,
  "error": "PRODUCT_NOT_FOUND",
  "message": "Product not found: P9999",
  "path": "/api/inventory/P9999"
}
```

Validation errors raised by Spring before the controller method is entered are not explicitly mapped by the current advice. If clients need the same structured error body for those failures, add handlers for the relevant validation exceptions (for example, `ConstraintViolationException` and missing request parameters).

## 6. Data model

The `Inventory` entity maps to the `inventory` table:

| Java field | Database column | Notes |
| --- | --- | --- |
| `id` | `id` | Identity-generated primary key |
| `productCode` | `product_code` | Required and unique |
| `productName` | `product_name` | Required |
| `quantity` | `quantity` | Required current stock |

`InventoryRepository.findByProductCode` relies on Spring Data’s derived-query naming convention. The unique product-code constraint means one product code maps to at most one inventory row.

## 7. Configuration

`src/main/resources/application.properties` configures:

- application name: `inventory-service`
- server port: `8082`
- PostgreSQL URL: `jdbc:postgresql://localhost:5432/inventorydb`
- database user: `postgres`
- Hibernate schema mode: `update`
- SQL logging and formatted SQL: enabled
- SQL initialization: enabled after Hibernate initialization

The current local configuration contains a database password directly in the properties file. For shared, test, or production environments, externalize credentials with environment variables or a secret manager and use a profile-specific configuration.

## 8. Seed data

`src/main/resources/data.sql` inserts these sample rows:

| Product code | Product name | Quantity |
| --- | --- | ---: |
| `P1004` | Shoes | 10 |
| `P1005` | Tshirts | 20 |
| `P1006` | Electronics | 5 |

Each insert uses PostgreSQL `ON CONFLICT (product_code) DO NOTHING`, so restarting the application does not duplicate the sample rows. The table must exist before the statements run; with the current settings, start the application once so Hibernate can create/update it, or create the table manually.

## 9. Running locally

1. Start PostgreSQL.
2. Create the database if it does not already exist:

   ```sql
   CREATE DATABASE inventorydb;
   ```

3. Ensure the configured PostgreSQL user and password can connect.
4. Start the application:

   ```text
   mvn spring-boot:run
   ```

5. Call the endpoint with a seeded product code, for example:

   ```text
   http://localhost:8082/api/inventory/P1004?quantity=2
   ```

## 10. Build and verification

Compile and package the service with:

```text
mvn clean verify
```

The repository currently contains no test source files. A useful minimum test suite would cover:

- sufficient stock returns `available: true`;
- insufficient stock returns `available: false`;
- an unknown product produces `404`;
- zero, negative, and null quantities are rejected;
- the controller maps the URL and query parameter correctly.

## 11. Extension guidance

- Add stock mutation or reservation as a separate use case with transaction boundaries; do not overload `checkAvailability`.
- Add optimistic locking (for example, a JPA `@Version` field) before implementing concurrent stock updates.
- Validate the product-code format if the domain introduces a formal rule.
- Consider a DTO for error responses from validation failures so all client-visible errors have one contract.
- Replace `spring.jpa.hibernate.ddl-auto=update` with a migration tool such as Flyway or Liquibase for controlled environments.
- Move the database password out of source-controlled configuration.

## 12. Source map

| Class | Responsibility |
| --- | --- |
| `InventoryServiceApplication` | Application bootstrap |
| `InventoryController` | HTTP route and request validation |
| `InventoryService` | Availability business rule |
| `InventoryRepository` | Database access by product code |
| `Inventory` | JPA persistence model |
| `InventoryResponse` | Success response DTO |
| `ProductNotFoundException` | Missing-product domain error |
| `GlobalExceptionHandler` | Error-to-HTTP response mapping |
