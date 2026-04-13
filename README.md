# 🏭 ManuERP — Manufacturing ERP System

A full-stack Manufacturing ERP application built as a learning side project. Covers the complete web development stack: relational database, REST API backend, and a React frontend.

---

## 📸 What It Looks Like

> A dark industrial UI with an amber accent color scheme. Sidebar navigation with 9 modules, live data tables, modal forms, and a real-time dashboard.

---

## 🗂️ Project Structure

```
ERP/
├── erp-backend/        ← Java Spring Boot REST API
├── erp-frontend/       ← React + Vite frontend
└── erp_schema_mysql.sql ← MySQL database schema + seed data
```

---

## 🧱 Tech Stack

| Layer      | Technology                          |
|------------|-------------------------------------|
| Database   | MySQL 8.0+                          |
| Backend    | Java 21, Spring Boot 3.2, Spring Data JPA |
| Build Tool | Maven                               |
| Frontend   | React 18, Vite, React Router        |
| Styling    | Pure CSS (custom design system)     |
| HTTP       | Axios                               |
| Icons      | Lucide React                        |

---

## 📦 Modules

| Module | Description |
|---|---|
| **Dashboard** | Live stats, recent orders, low stock alerts, outstanding invoices |
| **Sales Orders** | Create and manage orders with line items, status pipeline |
| **Customers** | CRM — full customer directory with CRUD |
| **Invoices** | Auto-generate invoices from orders, track payment status |
| **Payments** | Record and view all payment transactions |
| **Products** | Inventory catalog with stock levels and low-stock alerts |
| **Suppliers** | Supplier directory |
| **Employees** | HR directory with department grouping |
| **Departments** | Department management |

---

## 🚀 Getting Started

### Prerequisites

- [Java 21](https://adoptium.net) (Temurin LTS)
- [Maven](https://maven.apache.org) or IntelliJ IDEA (has Maven built in)
- [MySQL 8.0+](https://dev.mysql.com/downloads/)
- [Node.js LTS](https://nodejs.org)

---

### 1. Set Up the Database

Open MySQL Workbench, run the full script:

```sql
-- Run this file in MySQL Workbench
erp_schema_mysql.sql
```

This creates the `manufacturing_erp` database, all 10 tables, and inserts seed data.

---

### 2. Configure the Backend

Edit `erp-backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/manufacturing_erp?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
```

---

### 3. Run the Backend

Open `erp-backend` in IntelliJ IDEA and click **Run**, or from terminal:

```bash
cd erp-backend
mvn spring-boot:run
```

Backend starts at: **http://localhost:8080**

---

### 4. Run the Frontend

```bash
cd erp-frontend
npm install
npm run dev
```

Frontend starts at: **http://localhost:5173**

> The frontend automatically proxies `/api` calls to `localhost:8080` — no CORS setup needed.

---

## 🌐 API Endpoints

| Module | Base URL |
|---|---|
| Departments | `GET/POST /api/departments` |
| Employees | `GET/POST /api/employees` |
| Customers | `GET/POST /api/customers` |
| Suppliers | `GET/POST /api/suppliers` |
| Products | `GET/POST /api/products` |
| Sales Orders | `GET/POST /api/sales-orders` |
| Invoices | `GET/POST /api/invoices` |
| Payments | `GET/POST /api/payments` |

All resources support `GET /{id}`, `PUT /{id}`, `DELETE /{id}`.

---

## 🗄️ Database Schema

```
departments ←── employees
customers   ←── sales_orders ←── sales_order_items ──→ products
                     │
                  invoices ←── payments
suppliers   ←── products
```

---

## 📁 Backend File Structure

```
src/main/java/com/erp/
├── ManufacturingErpApplication.java   ← Entry point
├── config/
│   ├── CorsConfig.java                ← Allows frontend to talk to backend
│   └── GlobalExceptionHandler.java    ← Handles all errors in one place
├── model/          ← JPA Entities (map to database tables)
├── repository/     ← Database query interfaces
├── service/        ← Business logic interfaces
│   └── impl/       ← Business logic implementations
├── dto/
│   ├── request/    ← What the frontend sends in
│   └── response/   ← What the backend sends back
└── controller/     ← REST API endpoints
```

---

## 📁 Frontend File Structure

```
src/
├── App.jsx              ← Router + layout shell
├── index.css            ← Full design system (colors, typography, components)
├── api/
│   └── index.js         ← All Axios HTTP calls in one place
├── components/
│   ├── Sidebar.jsx      ← Navigation sidebar
│   ├── Modal.jsx        ← Reusable popup modal
│   └── Badge.jsx        ← Color-coded status labels
└── pages/
    ├── Dashboard.jsx
    ├── Customers.jsx
    ├── Products.jsx
    ├── Employees.jsx
    ├── Suppliers.jsx
    ├── Departments.jsx
    ├── SalesOrders.jsx
    ├── Invoices.jsx
    └── Payments.jsx
```

---

## 🧠 Key Concepts Used

- **REST API** — HTTP verbs (GET, POST, PUT, PATCH, DELETE) map to database operations
- **JPA / Hibernate** — Java objects automatically map to database rows
- **DTOs** — Separate objects for incoming requests vs outgoing responses
- **React Router** — Client-side navigation without page reloads
- **Axios** — Promise-based HTTP client for calling the backend
- **Vite Proxy** — Dev server forwards `/api` requests to Spring Boot

---

## 🛠️ Built With Help From

This project was built as a learning exercise with AI-assisted code generation. The goal was to understand how all layers of a full-stack web application connect to each other.

---

## 📄 License

MIT — free to use, modify, and learn from.
