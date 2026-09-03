# Inventory Service

This is an independent Spring Boot Maven project. Open this directory by itself in IntelliJ IDEA or STS.

## Run

Create the database with `CREATE DATABASE inventorydb;`, ensure PostgreSQL accepts user/password `postgres`/`postgres`, then run:

```text
mvn spring-boot:run
```

The service listens on port 8082. The sample statements in `src/main/resources/data.sql` can be run in `inventorydb` after the first application start.

## API

```http
GET http://localhost:8082/api/inventory/P1001?quantity=2
```

Expected response:

```json
{"productCode":"P1001","productName":"Laptop","availableQuantity":10,"available":true}
```
