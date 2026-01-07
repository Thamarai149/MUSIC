# 🚂 Railway Reservation System

A comprehensive console-based Railway Reservation System built with Java and MySQL, featuring train booking, seat management, and reservation tracking for Tamil Nadu railway network.

## ✨ Features

- **🔍 Train Search**: Search for available trains between Tamil Nadu districts
- **🎫 Ticket Booking**: Book tickets with passenger details and automatic seat assignment
- **💺 Seat Management**: Real-time seat availability tracking with intelligent seat allocation
- **❌ Ticket Cancellation**: Cancel booked tickets with automatic seat release
- **✏️ Update Passenger Details**: Modify passenger name, email, and phone number for existing bookings
- **� Ticket  Viewing**: View detailed ticket information and passenger booking history
- **🗄️ MySQL Integration**: Persistent data storage with relational database
- **🏛️ Tamil Nadu Routes**: 24+ train routes covering major districts

## 🏗️ Project Structure

```
railway-reservation-system/
├── src/main/java/com/railway/
│   ├── config/
│   │   └── DatabaseConfig.java          # Database connection configuration
│   ├── model/
│   │   ├── Train.java                   # Train entity model
│   │   └── Ticket.java                  # Ticket entity model
│   ├── dao/
│   │   ├── TrainDAO.java                # Train data access operations
│   │   └── TicketDAO.java               # Ticket data access operations
│   ├── service/
│   │   └── ReservationService.java      # Business logic layer
│   └── RailwayReservationSystem.java    # Main application class
├── database/
│   └── schema.sql                       # Database schema and sample data
├── lib/
│   └── mysql-connector-j-8.0.33.jar    # MySQL JDBC driver
├── target/classes/                      # Compiled Java classes
├── pom.xml                              # Maven configuration
└── README.md                            # Project documentation
```

## 🛠️ Prerequisites

- ☕ **Java 11 or higher**
- 🗄️ **MySQL 8.0 or higher**
- 📦 **Maven 3.6 or higher** (optional)

## 🚀 Quick Start

### 1. Database Setup

1. **Install MySQL** and start the MySQL service
2. **Create the database** and tables:
   ```bash
   mysql -u root -p < database/schema.sql
   ```

### 2. Database Configuration

Update your MySQL password in `src/main/java/com/railway/config/DatabaseConfig.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/railway_db";
private static final String USERNAME = "root";
private static final String PASSWORD = "Thamarai@2006"; // Your MySQL password
```

### 3. Run the Application

**Option A: Using pre-compiled version (Ready to use)**
```bash
java -cp "target/classes;lib/mysql-connector-j-8.0.33.jar" com.railway.RailwayReservationSystem
```

**Option B: Using Maven**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.railway.RailwayReservationSystem"
```

## 🎮 Usage Guide

The application provides an intuitive menu-driven interface:

```
=== MAIN MENU ===
1. Search Trains           # Find trains between stations
2. Book Ticket             # Reserve seats with passenger details
3. Cancel Ticket           # Cancel existing bookings
4. View Ticket Details     # Check specific ticket information
5. View My Tickets         # See all tickets for a passenger
6. Update Passenger Details # Modify passenger name, email, phone
7. Print Ticket            # Generate professional ticket printout (Console/PDF)
8. Exit                    # Close the application
```

### Example Workflow:
1. **Search**: Enter "Chennai" → "Coimbatore"
2. **Select**: Choose from available trains (Chennai Express, Kovai Express, etc.)
3. **Book**: Provide passenger details (name, email, phone)
4. **Confirm**: Get ticket ID and seat number
5. **Update**: Modify passenger details if needed using ticket ID
6. **Manage**: View or cancel tickets as needed

## 🗄️ Database Schema

### 🚆 Trains Table
| Column | Type | Description |
|--------|------|-------------|
| `train_id` | INT (PK) | Unique train identifier |
| `train_name` | VARCHAR(100) | Train name (e.g., "Chennai Express") |
| `source` | VARCHAR(50) | Departure station |
| `destination` | VARCHAR(50) | Arrival station |
| `departure_time` | TIME | Departure time |
| `arrival_time` | TIME | Arrival time |
| `total_seats` | INT | Total seat capacity |
| `available_seats` | INT | Currently available seats |
| `fare` | DECIMAL(10,2) | Ticket price in rupees |

### 🎫 Tickets Table
| Column | Type | Description |
|--------|------|-------------|
| `ticket_id` | INT (PK) | Unique ticket identifier |
| `train_id` | INT (FK) | Reference to trains table |
| `passenger_name` | VARCHAR(100) | Passenger's full name |
| `passenger_email` | VARCHAR(100) | Passenger's email address |
| `passenger_phone` | VARCHAR(15) | Passenger's phone number |
| `seat_number` | INT | Assigned seat number |
| `fare` | DECIMAL(10,2) | Ticket fare |
| `booking_time` | TIMESTAMP | Booking timestamp |
| `status` | ENUM | BOOKED or CANCELLED |

## 🗺️ Tamil Nadu Railway Network

### Major Routes Available:

**🏙️ From Chennai:**
- Chennai → Coimbatore (Chennai Express, Kovai Express)
- Chennai → Madurai (Madurai Mail, Vaigai Express)
- Chennai → Tirunelveli (Nilgiri Express, Pandian Express)
- Chennai → Salem (Cheran Express)
- Chennai → Tiruchirappalli (Rockfort Express)

**🌆 Inter-District Connections:**
- Coimbatore ↔ Tirunelveli, Salem, Ooty
- Madurai ↔ Kanyakumari, Rameswaram, Kodaikanal
- Tiruchirappalli ↔ Thanjavur, Salem
- Salem ↔ Erode, Dharmapuri

**💰 Fare Range:** ₹45 - ₹435 based on distance
**🚂 Total Trains:** 24 different routes
**💺 Seat Capacity:** 55-120 seats per train

## 🛠️ Technologies Used

- ☕ **Java 17**: Core programming language
- 🗄️ **MySQL 8.0**: Database management system
- 🔌 **JDBC**: Database connectivity
- 📦 **Maven**: Build and dependency management
- 🏗️ **Layered Architecture**: Clean separation of concerns

## 🔧 Development Setup

### Manual Compilation (No Maven required):
```bash
# Compile Java files
javac -cp "lib/mysql-connector-j-8.0.33.jar" -d target/classes src/main/java/com/railway/*/*.java src/main/java/com/railway/*.java

# Run application
java -cp "target/classes;lib/mysql-connector-j-8.0.33.jar" com.railway.RailwayReservationSystem
```

### Using MySQL Workbench:
1. **Connect** to localhost:3306 with username `root`
2. **Open** `railway_db` database
3. **View** trains and tickets tables
4. **Run queries** to analyze booking data

## 🚀 Future Enhancements

- 🔐 **User Authentication**: Login system with role-based access
- 💳 **Payment Integration**: Online payment gateway
- 📧 **Email Notifications**: Booking confirmations and reminders
- 📊 **Admin Dashboard**: Train schedule and booking management
- 🌐 **Web Interface**: Modern web-based UI
- 📱 **Mobile App**: Android/iOS applications
- 📈 **Analytics**: Booking trends and revenue reports
- 🎯 **Seat Selection**: Visual seat map for selection

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 👨‍💻 Author

**Railway Reservation System**  
*A comprehensive Java-MySQL application for train booking management*

---

**🎯 Ready to book your next train journey across Tamil Nadu? Run the application and start exploring!** 🚂✨