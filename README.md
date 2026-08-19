# Inventory Management System

A Java-based enterprise inventory management application for warehouse operations. The system supports stock tracking, supplier management, purchase orders, sales billing, returns handling, invoice PDF generation, and authentication backend support.

## Tech Stack

- Java 17
- JavaFX and FXML
- JDBC
- PostgreSQL
- Maven
- Spring Boot
- Spring Security with JWT
- iText PDF

## Main Modules

- Dashboard with inventory, sales, purchase, supplier, return, and low-stock metrics
- Inventory and product management with stock and reorder threshold tracking
- Supplier CRUD management
- Purchase order management
- Sales billing with stock checking and invoice PDF generation
- Sales returns and purchase returns
- Authentication backend with register/login APIs and JWT token generation

## Project Structure

```text
Inventory-Management-main/
src/                     JavaFX desktop application
authbackend/             Spring Boot authentication backend
product-backend/         Spring Boot product API backend
database/schema.sql      PostgreSQL schema and sample data
pom.xml                  JavaFX app Maven build
README.md
```

## Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE inventory_management;
```

Run the schema file:

```text
database/schema.sql
```

Database configuration:

```text
src/main/resources/application.properties
```

The application supports environment variables for local database credentials:

```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="your_postgresql_password"
```

## Run JavaFX Desktop App

```powershell
mvn clean javafx:run
```

Main class:

```text
com.example.inventorydashboard.App
```

## Run Auth Backend

```powershell
cd authbackend
mvn spring-boot:run
```

Auth APIs:

```text
POST /api/auth/register
POST /api/auth/login
```

## Demo Flow

1. Open the dashboard and show inventory, sales, purchase, supplier, return, and low-stock metrics.
2. Open inventory and show product stock, reorder point, and stock status.
3. Add or update a supplier.
4. Create a purchase order.
5. Open the sales page, check stock, and generate an invoice PDF.
6. Show sales return and purchase return records.
7. Show the generated invoice file from the local `invoices/` folder.
