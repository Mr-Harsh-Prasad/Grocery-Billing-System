# 🛒 Grocery Billing System

> A modern desktop-based grocery store management and billing
> application built with **JavaFX, SQLite, and Java**.

![Java](https://img.shields.io/badge/Java-26-orange?style=for-the-badge&logo=openjdk)
![JavaFX](https://img.shields.io/badge/JavaFX-26-4285F4?style=for-the-badge)
![SQLite](https://img.shields.io/badge/SQLite-3-003B57?style=for-the-badge&logo=sqlite)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven)
![Status](https://img.shields.io/badge/Status-In%20Development-yellow?style=for-the-badge)

------------------------------------------------------------------------

## ✨ Overview

**Grocery Billing System** is a desktop application designed to simplify
day-to-day grocery store operations.

It combines billing, product management, customer management, inventory
tracking, and sales insights into a single JavaFX application.

The project is being developed as a team-based academic project with a
focus on clean architecture, database-driven functionality, and a
practical cashier workflow.

------------------------------------------------------------------------

## 🚀 Features

### 🧾 Billing

-   Create new bills
-   Search products by name
-   Browse products by category
-   Add products to the current bill
-   Manage quantities
-   Apply discounts
-   Calculate GST
-   Calculate subtotal, taxable amount, and grand total
-   Select customer for a bill
-   Support walk-in customers
-   Cash / UPI / Card payment selection
-   Calculate return/change amount
-   Generate invoice numbers
-   Save bills and bill items to SQLite
-   Automatically reduce product stock after successful payment

### 📦 Product Management

-   Add products
-   Update products
-   Delete products
-   View product inventory
-   Product categories
-   Purchase and selling prices
-   Stock quantity tracking
-   Reorder level
-   Product search
-   Category filtering
-   Low-stock identification

### 👥 Customer Management

-   Add customers
-   Update customer details
-   Delete customers
-   Search customers
-   Select customers directly from Billing
-   Create a new customer without leaving the Billing screen

### 📊 Dashboard

-   Total sales
-   Total orders
-   Total products
-   Total profit
-   Sales chart
-   Low-stock alerts
-   Top-selling products
-   Recent transactions

### 🗃️ Database

-   Local SQLite database
-   JDBC-based database access
-   DAO-based database layer
-   Transaction-safe bill saving
-   Automatic inventory deduction after billing

------------------------------------------------------------------------

## 🖥️ Application Structure

``` text
Grocery Billing System
│
├── Dashboard
│   ├── Sales Overview
│   ├── Low Stock Alerts
│   ├── Top Products
│   └── Recent Transactions
│
├── Billing
│   ├── Product Search
│   ├── Category Filter
│   ├── Cart / Bill Table
│   ├── Customer Selection
│   ├── Payment
│   └── Invoice
│
├── Products
│   ├── Add
│   ├── Update
│   ├── Delete
│   ├── Search
│   └── Inventory
│
├── Customers
│   ├── Add
│   ├── Update
│   ├── Delete
│   └── Search
│
├── History
│   └── Billing History
│
└── Settings
    └── Application Settings
```

------------------------------------------------------------------------

## 🏗️ Architecture

The application follows a simple layered structure:

``` text
FXML / JavaFX UI
       │
       ▼
Controllers
       │
       ▼
DAO Layer
       │
       ▼
SQLite Database
```

### Main packages

``` text
Application
├── controllers
├── database
├── models
├── views
└── style
```

-   **`models`** --- application data models such as Product, Customer,
    and BillItem
-   **`controllers`** --- JavaFX event handling and UI logic
-   **`database`** --- SQLite connection and DAO classes
-   **`views`** --- JavaFX FXML screens
-   **`style`** --- application CSS

------------------------------------------------------------------------

## 🗄️ Database

The current database contains the core entities required for the
application:

``` text
products
customers
bills
bill_items
```

### Relationship

``` text
Customer
   │
   │ 1
   ▼
 Bills
   │
   │ 1
   ▼
Bill Items
   │
   │ N
   ▼
Products
```

A bill can also be created without a registered customer, allowing
**walk-in billing**.

------------------------------------------------------------------------

## 🧰 Tech Stack

  Technology                Purpose
  ------------------------- ---------------------------------
  ☕ Java 26                Core application
  🎨 JavaFX 26              Desktop UI
  🧱 FXML / Scene Builder   UI design
  🗃️ SQLite                 Local database
  🔌 JDBC                   Database connectivity
  📦 Maven                  Dependency & build management
  🎨 CSS                    JavaFX styling
  🔀 Git & GitHub           Version control & collaboration

------------------------------------------------------------------------

## ⚙️ Requirements

Before running the project, make sure you have:

-   **JDK 26**
-   **JavaFX 26**
-   **Maven**
-   **IntelliJ IDEA** or another Java IDE
-   SQLite JDBC driver

------------------------------------------------------------------------

## ▶️ Run the Project

### 1. Clone the repository

``` bash
git clone <your-repository-url>
cd "Grocery Billing System"
```

### 2. Build the project

``` bash
mvn clean package
```

### 3. Run the application

Run:

``` text
Application.Main
```

from your IDE with JavaFX configured.

> JavaFX must be available through the configured module path / Maven
> dependencies used by the project.

------------------------------------------------------------------------

## 🔐 Billing Transaction Flow

One of the important parts of the project is the payment transaction.

``` text
Select Products
      ↓
Create Bill
      ↓
Calculate Total
      ↓
Select Customer / Walk-in
      ↓
Payment
      ↓
Create Bill
      ↓
Create Bill Items
      ↓
Reduce Product Stock
      ↓
Commit Transaction
```

If stock is insufficient or the transaction fails:

``` text
        ❌ Error
           ↓
       Rollback
           ↓
No partial bill / stock update
```

This prevents the database from being left in an inconsistent state.

------------------------------------------------------------------------

## 📸 Screens

The application includes dedicated interfaces for:

-   Dashboard
-   Billing
-   Products
-   Customers
-   History
-   Settings

> Screenshots can be added here as the UI is finalized.

Example:

``` markdown
![Dashboard](docs/screenshots/dashboard.png)
![Billing](docs/screenshots/billing.png)
![Products](docs/screenshots/products.png)
```

------------------------------------------------------------------------

## 🛣️ Roadmap

### ✅ Completed

-   [x] JavaFX application structure
-   [x] SQLite database setup
-   [x] Product CRUD
-   [x] Customer CRUD
-   [x] Product search and category filtering
-   [x] Billing cart
-   [x] GST and bill calculations
-   [x] Stock deduction after payment
-   [x] Customer selection in Billing
-   [x] Dashboard data layer

### 🔄 In Progress

-   [ ] Complete Dashboard integration
-   [ ] Billing polish and validation
-   [ ] History screen
-   [ ] Settings screen
-   [ ] Print Bill
-   [ ] Final UI/UX polishing

------------------------------------------------------------------------

## 🤝 Team Development

This project is developed collaboratively using Git and GitHub.

Recommended workflow:

``` bash
git checkout -b feature-name
git add .
git commit -m "Add feature"
git push origin feature-name
```

Changes are reviewed and merged into the main branch after testing.

------------------------------------------------------------------------

## 📌 Project Status

**🚧 In Development**

The core database, product management, customer management, and billing
workflows are implemented. Dashboard, history, printing, and final
application polish are being completed.

------------------------------------------------------------------------

## 🎯 Project Goal

The goal is to build a practical grocery-store desktop application that
makes everyday operations faster and easier:

> **Manage products → manage customers → create bills → process payments
> → update inventory → analyze sales.**

------------------------------------------------------------------------

## 👨‍💻 Contributors

Built as a team project.

Add your team members here:

-   **Harsh**
-   **\[Team Member\]**
-   **\[Team Member\]**

------------------------------------------------------------------------

## 📄 License

This project is developed for educational and academic purposes.
