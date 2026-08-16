# 🚆 Railway Reservation Management System

A robust, enterprise-grade **Railway Reservation and Management System** built with **Core Java**, **JDBC**, and **MySQL**. The system models real-world train operations including route scheduling, multi-coach seat allocation, dynamic fare calculation, PNR generation, RAC/Waitlist queues with automatic seat upgradation on cancellation, payment processing, and administrative audit logging.

---

## 📑 Table of Contents
- [✨ Key Features](#-key-features)
  - [Passenger Features](#passenger-features)
  - [Administrator Features](#administrator-features)
  - [Guest Services](#guest-services)
- [🏛️ System Architecture](#️-system-architecture)
- [🛠️ Tech Stack](#️-tech-stack)
- [🗄️ Database Schema & Entities](#️-database-schema--entities)
- [🚀 Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Database Setup](#database-setup)
  - [Running the Project](#running-the-project)
- [🔑 Default Credentials](#-default-credentials)
- [📂 Project Structure](#-project-structure)
- [📄 License](#-license)

---

## ✨ Key Features

### 👤 Passenger Features
- **User Authentication**: Secure registration, login, profile view, and update.
- **Train Search**: Search direct and intermediate trains between source and destination stations by date.
- **Real-Time Seat Availability**: Live berth/seat status across classes (1A, 2A, 3A, SL, CC, 2S).
- **Ticket Booking & PNR Generation**:
  - Multi-passenger booking support with age, gender, and berth preference.
  - Automatic seat assignment: **Confirmed (CNF)**, **RAC (Reservation Against Cancellation)**, or **Waitlist (WL)**.
- **Instant Payment Gateway Simulation**: UPI, Net Banking, Credit/Debit card support.
- **Ticket Cancellation & Refund**:
  - Automated refund calculation based on cancellation timing and policy.
  - **Automatic Seat Upgradation**: When a confirmed ticket is cancelled, RAC passengers are automatically upgraded to Confirmed, and Waitlist passengers are promoted to RAC.
- **Booking History & PNR Status**: Track upcoming, completed, and cancelled journeys.

---

### 🛡️ Administrator Features
- **Train Management**: Add, update, and manage train metadata (Express, Superfast, Passenger, Rajdhani, Shatabdi).
- **Station & Route Management**: Define railway stations, intermediate stops, stoppage durations, and distances.
- **Coach & Seat Configuration**: Configure coach compositions (S1, S2, B1, A1, etc.) and berth layouts (Lower, Middle, Upper, Side Lower, Side Upper).
- **Fare Matrix Management**: Configure base fares, distance-based pricing, and coach class multipliers.
- **Passenger & Booking Oversight**: View real-time reservations, passenger manifests, and cancellation logs.
- **Financial & Occupancy Reports**: Revenue analytics, train occupancy statistics, and refund reports.
- **Security Audit Logs**: Track all administrative modifications with timestamps and action history.

---

### 🎫 Guest Services
- **Quick Train Search**: Search schedules and routes without logging in.
- **Quick PNR Enquiry**: Instant real-time status check for any 10-digit PNR number.

---

## 🏛️ System Architecture

The project follows a clean, decoupled **Layered Architecture** adhering to SOLID principles and industry design patterns:

```
┌────────────────────────────────────────────────────────┐
│                   Presentation Layer                   │
│         (MainMenu, UserMenu, AdminMenu, UI)            │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                     Service Layer                      │
│   (TrainService, BookingService, UserService, etc.)    │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│             Data Access Object (DAO) Layer             │
│    (TrainDAO, BookingDAO, UserDAO, AdminDAO, etc.)     │
└───────────────────────────┬────────────────────────────┘
                            │
┌───────────────────────────▼────────────────────────────┐
│                    MySQL Database                      │
│     (railway_reservation_system - JDBC Connection)     │
└────────────────────────────────────────────────────────┘
```

- **DAO Pattern**: Encapsulates all raw SQL queries, transactions, and PreparedStatements.
- **Service Layer**: Implements business rules (RAC/Waitlist progression, fare computation, refunds).
- **Custom Exceptions**: Specialized exception hierarchy (`RailwayException`, `BookingException`, `AuthenticationException`, etc.) for robust error handling.
- **Input Sanitization**: Reusable `InputValidator` utility preventing SQL injections and handling invalid user inputs gracefully.

---

## 🛠️ Tech Stack

- **Language**: Java 21 / 22 (Core Java)
- **Database**: MySQL 8.0+
- **Connectivity**: JDBC (Java Database Connectivity) via `mysql-connector-j-9.7.0.jar`
- **IDE**: Eclipse IDE / IntelliJ IDEA / VS Code
- **Version Control**: Git & GitHub

---

## 🗄️ Database Schema & Entities

The system manages the following core entities in MySQL:

| Entity | Description |
| :--- | :--- |
| **`users`** | Passenger profiles, contact information, and credentials |
| **`admins`** | Administrative users and roles (SuperAdmin, Station Master, Booking Manager) |
| **`stations`** | Station codes (e.g. `HYB`, `SC`, `VSKP`, `NDLS`), station names, and zones |
| **`trains`** | Train numbers, train names, total distance, and operational statuses |
| **`train_routes`** | Stop sequence, arrival/departure schedules, and distance from origin |
| **`train_coaches`**| Coach designations, coach types, and total capacities |
| **`coach_seats`**  | Individual seat/berth numbers and berth positions |
| **`train_fares`**  | Base fares and class multipliers |
| **`bookings`**     | PNR numbers, booking dates, travel dates, and payment summaries |
| **`booking_passengers`** | Passenger details, assigned coach/seat, and booking statuses (CNF/RAC/WL) |
| **`payments`**     | Transaction IDs, payment modes, amounts, and statuses |
| **`cancellations`**| Cancellation records and refund details |
| **`audit_logs`**   | Administrative actions and change tracking |

---

## 🚀 Getting Started

### Prerequisites
- **JDK 17 or higher** (JDK 21+ recommended)
- **MySQL Server 8.0+**
- **Git**

### Database Setup
1. Ensure your MySQL Server is running on `localhost:3306`.
2. Import the SQL scripts in sequential order (or execute them in MySQL Workbench / CLI):
   ```sql
   SOURCE 01_database.sql;
   SOURCE 02_lookup_tables.sql;
   SOURCE 03_lookup_data.sql;
   SOURCE 04_master_tables.sql;
   SOURCE 05_route_tables.sql;
   SOURCE 06_coach_seat_tables.sql;
   SOURCE 07_booking_tables.sql;
   SOURCE 08_payment_tables.sql;
   SOURCE 09_cancellation_tables.sql;
   SOURCE 10_additional_tables.sql;
   SOURCE 11_indexes.sql;
   SOURCE 12_views.sql;
   ```
3. Verify your database credentials in `src/com/railway/util/DBConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/railway_reservation_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
   private static final String USER = "root";
   private static final String PASSWORD = "your_mysql_password";
   ```

---

### Running the Project

#### Option A: Inside Eclipse IDE
1. Open Eclipse and import the project: **File** $\rightarrow$ **Open Projects from File System...** $\rightarrow$ Select project directory.
2. In the **Package Explorer**, expand `src` $\rightarrow$ `com.railway.main`.
3. Right-click **`Main.java`** $\rightarrow$ **Run As** $\rightarrow$ **1 Java Application**.
4. Use the Eclipse **Console** at the bottom to interact with the application.

#### Option B: From Terminal / PowerShell
```powershell
# Navigate to the project root directory
cd "Railway-Reservation-System"

# Run the application
java -cp "bin;lib/mysql-connector-j-9.7.0.jar" com.railway.main.Main
```

---

## 🔑 Default Credentials

### Administrator Accounts
| Username | Password | Role |
| :--- | :--- | :--- |
| `superadmin` | `admin@123` | Super Admin |
| `admin` | `admin123` | SuperAdmin |
| `railadmin01` | `rail@123` | Admin |
| `bookingadmin` | `booking@123` | Booking Manager |

---

## 📂 Project Structure

```text
Railway-Reservation-System/
├── lib/
│   └── mysql-connector-j-9.7.0.jar   # MySQL JDBC Driver
├── src/
│   └── com/railway/
│       ├── dao/                      # Data Access Object interfaces
│       │   └── impl/                 # DAO JDBC implementations
│       ├── exception/                # Custom business & domain exceptions
│       ├── main/
│       │   ├── Main.java             # Main Application Entrypoint
│       │   └── SystemIntegrationTest.java
│       ├── model/                    # Domain Model & Entity POJOs
│       ├── service/                  # Business Logic interfaces
│       │   └── impl/                 # Service implementations
│       ├── ui/                       # Console User Interface controllers
│       └── util/                     # Database Connection & Validator utilities
├── .gitignore
└── README.md
```

---

## 📄 License
This project is open source and available .
