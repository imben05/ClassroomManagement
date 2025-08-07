# QuanLyPhongHoc – Classroom Management System

A Java-based console application for managing classroom reservations and usage.  
Developed with NetBeans IDE using Object-Oriented Programming (OOP) and file I/O for data persistence.

---

## 📌 Overview

The application allows admins to:
- Add, edit, and delete room records
- Book and manage classroom usage
- View booking history and usage statistics
- Login with role-based access (Admin)

The system runs entirely in the **console**.

---

## 🧩 Main Features

### ✅ Admin Management
- Login with username & password
- Pre-defined admin accounts

### 🏫 Classroom Management
- Add new rooms with room type, capacity, and code
- Edit or delete existing room information
- View all current rooms

### 🗓️ Booking Management
- Book rooms by date and time
- View current bookings
- Prevent overlapping or duplicate bookings

### 📊 Statistics
- Track room usage frequency
- Display booking trends and summary

---

## 🛠 Technologies Used

| Technology | Purpose |
|------------|---------|
| Java | Core programming language |
| NetBeans IDE | Development environment |
| Java Collections | Data structures (ArrayList, etc.) |
| File I/O | Saving/loading data |
| OOP | Class design, inheritance, encapsulation |

---

## 🗂 Project Structure
```
src/
├── com.thuchanh.quanlyphonghoc/
│ ├── AddRoom.java
│ ├── BookRoom.java
│ ├── HistoryBookings.java
│ ├── Home.java
│ ├── Login.java
│ ├── ManageRooms.java
│ └── StatisticView.java
│
├── model/
│ ├── Admin.java
│ ├── Booking.java
│ ├── DatabaseUtil.java
│ ├── Rooms.java
│ └── Statistic.java
```
---

## 🚀 How to Run the Project

1. **Clone the repository:**

```bash
git clone https://github.com/imben05/ClassroomManagement.git
```
2. **Open in NetBeans:**
- File > Open Project
- Select QuanLyPhongHoc folder

3. **Run the project:**
- Right-click the project > Run
- Follow console instructions for login and feature navigation

---

## 👤 Author

- **Nguyễn Lê Xuân Trí**
- GitHub: [@imben05](https://github.com/imben05)

---

## 🔧 Future Improvements

- Add GUI using Java Swing or JavaFX
- Store data in a database (e.g., SQLite or MySQL)
- Implement unit tests for core modules
- Add input validation and error handling
- Export booking reports to CSV or PDF
