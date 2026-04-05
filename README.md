# 🏨 Hotel Reservation System — Java Swing UI

A full-featured **Hotel Reservation Management System** built with **Java Swing** for the GUI and **JDBC** for MySQL database connectivity. Manage hotel reservations through a clean, interactive desktop interface.

---

## 🖥️ Preview

```
+--------------------------------------------------+
|        🏨 Hotel Reservation System               |
+------------+-------------------------------------+
|            |  ID | Guest Name | Room | Contact   |
| + Reserve  |-----|------------|------|-----------|
| View All   |  1  | Aditya     | 102  | 365474    |
| Update     |  2  | Mrinal     | 103  | 69874     |
| Delete     |                                     |
| Search     |                                     |
| Exit       |                                     |
+------------+-------------------------------------+
```

---

## ✨ Features

- ✅ **Reserve a Room** — Add new guest reservations via popup form
- ✅ **View All** — See all reservations in a live table
- ✅ **Update** — Modify existing reservation details
- ✅ **Delete** — Remove a reservation with confirmation dialog
- ✅ **Search** — Find a reservation by ID
- ✅ **Exit** — Close the application
- ✅ **Auto Refresh** — Table updates automatically after every operation
- ✅ **Secure Credentials** — DB password hidden via `config.properties`

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Java | Core language |
| Java Swing | Desktop GUI |
| JDBC | Database connectivity |
| MySQL | Database |
| MySQL Connector/J 9.6.0 | JDBC Driver |

---

## 🗄️ Database Setup

Run the following in MySQL:

```sql
CREATE DATABASE hotel_db;

USE hotel_db;

CREATE TABLE reservations (
    reservation_id   INT PRIMARY KEY AUTO_INCREMENT,
    guest_name       VARCHAR(50),
    room_number      INT,
    contact_number   VARCHAR(15),
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

---

## ⚙️ Configuration

This project uses a `config.properties` file to store database credentials securely.

1. Create a file named `config.properties` in the project root:

```properties
db.url=jdbc:mysql://localhost:3306/hotel_db
db.user=your_mysql_username
db.password=your_mysql_password
```

2. A `config.properties.example` file is provided as a template.

> ⚠️ `config.properties` is listed in `.gitignore` and will **never** be pushed to GitHub.

---

## 🚀 How to Run

1. Clone the repository:
```bash
git clone https://github.com/a-d-it-ya/Hotel-Reservation-System-Using-Swing-UI.git
cd Hotel-Reservation-System-Using-Swing-UI
```

2. Add the MySQL Connector JAR to your project.

3. Create `config.properties` with your DB credentials (see above).

4. Run `HotelReservationSystem.java` from VS Code or terminal.

---

## 📂 Project Structure

```
Hotel Reservation System Using Swing UI/
├── HotelReservationSystem.java     # Main file with GUI + JDBC
├── config.properties               # ❌ ignored by git (your credentials)
├── config.properties.example       # ✅ template for setup
├── .gitignore
└── README.md
```

---

## 🖥️ GUI Components Used

| Component | Purpose |
|---|---|
| `JFrame` | Main application window |
| `JPanel` | Sidebar container |
| `JButton` | Action buttons |
| `JTable` | Display reservations |
| `DefaultTableModel` | Manage table data |
| `JOptionPane` | Input forms and alerts |
| `JScrollPane` | Scrollable table view |
| `BorderLayout` | Main layout |
| `GridLayout` | Sidebar button layout |

---

## 📚 Concepts Covered

- Java Swing GUI development
- JDBC Connection & PreparedStatement
- MySQL CRUD operations from GUI
- Secure credential management
- Event handling with `ActionListener`
- Dynamic table updates with `DefaultTableModel`

---

## 👨‍💻 Author

**Aditya** — Java Swing & JDBC learning project
