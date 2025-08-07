/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Rooms;

/**
 *
 * @author tring
 */
public class DatabaseUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/mysql?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    
    private static boolean isValidPassword(String password) {
        return password.length() >= 8;
    }

    private static boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private static boolean isValidUsername(String username) {
        return username.length() >= 8;
    }

    public static boolean loginAdmin(String username, String password) {
        String sql = "SELECT * FROM admins WHERE username = ? AND password = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("Error logging in: " + e.getMessage());
        }
        return false;
    }
    
    public static boolean addRooms(String code, String name, String type, int capacity,
                               String teacherNote, String imagePath) {
        String sql = "INSERT INTO rooms (code, name, type, capacity, status, teacher_note, image_path) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, code);
            statement.setString(2, name);
            statement.setString(3, type);
            statement.setInt(4, capacity);
            statement.setString(5, "Trống");
            statement.setString(6, (teacherNote == null || teacherNote.trim().isEmpty()) ? null : teacherNote);
            statement.setString(7, (imagePath == null || imagePath.trim().isEmpty()) ? null : imagePath);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error adding room: " + e.getMessage());
            return false;
        }
    }

    public static boolean deleteRooms(int roomId) {
        String query = "DELETE FROM rooms WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, roomId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean updateRooms(Rooms room) {
        String query = "UPDATE rooms SET code = ?, name = ?, type = ?, capacity = ?, status = ?, teacher_note = ?, image_path = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, room.getCode());
            statement.setString(2, room.getName());
            statement.setString(3, room.getType());
            statement.setInt(4, room.getCapacity());
            statement.setString(5, room.getStatus());

        // Kiểm tra nếu rỗng thì set null cho cột
            statement.setString(6, (room.getTeacherNote() == null || room.getTeacherNote().trim().isEmpty()) ? null : room.getTeacherNote());
            statement.setString(7, (room.getImagePath() == null || room.getImagePath().trim().isEmpty()) ? null : room.getImagePath());

            statement.setInt(8, room.getId());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static ObservableList<Rooms> getRooms() {
        ObservableList<Rooms> rooms = FXCollections.observableArrayList();
        String query = "SELECT * FROM rooms";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String code = rs.getString("code");
                String name = rs.getString("name");
                String type = rs.getString("type");
                int capacity = rs.getInt("capacity");
                String status = rs.getString("status");
                String teacherNote = rs.getString("teacher_note");
                String imagePath = rs.getString("image_path");

                rooms.add(new Rooms(id, code, name, type, capacity, status, teacherNote, imagePath));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
        }

        return rooms;
    }
    public static Rooms getRoomById(int roomId) {
        String sql = "SELECT * FROM rooms WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Rooms(
                    rs.getInt("id"),
                    rs.getString("code"),
                    rs.getString("name"),
                    rs.getString("type"),
                    rs.getInt("capacity"),
                    rs.getString("status"),
                    rs.getString("teacher_note"),
                    rs.getString("image_path")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean addBooking(Booking booking) {
        String sql = "INSERT INTO bookings (room_id, user_id, date, start_time, end_time, purpose) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, booking.getRoomId());
            stmt.setInt(2, booking.getUserId());
            stmt.setDate(3, Date.valueOf(booking.getDate()));
            stmt.setTime(4, Time.valueOf(booking.getStartTime()));
            stmt.setTime(5, Time.valueOf(booking.getEndTime()));
            stmt.setString(6, booking.getPurpose());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean updateRoomStatus(int roomId, String status) {
        String sql = "UPDATE rooms SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, roomId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating room status: " + e.getMessage());
            return false;
        }
    }
    
    public static ObservableList<Booking> getBookings() {
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        String query = 
    "SELECT " +
    "b.id, b.room_id, b.user_id, b.date, b.start_time, b.end_time, b.purpose, " +
    "r.code AS room_code, r.name AS room_name, r.type AS room_type " +
    "FROM bookings b " +
    "JOIN rooms r ON b.room_id = r.id " +
    "ORDER BY b.date ASC, b.start_time ASC";
    
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
        
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("id"),
                    rs.getInt("room_id"),
                    rs.getInt("user_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("start_time").toLocalTime(),
                    rs.getTime("end_time").toLocalTime(),
                    rs.getString("purpose")
                );
                booking.setRoomCode(rs.getString("room_code"));
                booking.setRoomName(rs.getString("room_name"));
                booking.setRoomType(rs.getString("room_type"));    
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    public static ObservableList<Booking> getBookingsByRoomId(int roomId) {
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        String sql = "SELECT * FROM bookings WHERE room_id = ? ORDER BY date ASC, start_time ASC";
        
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Booking booking = new Booking(
                    rs.getInt("id"),
                    rs.getInt("room_id"),
                    rs.getInt("user_id"),
                    rs.getDate("date").toLocalDate(),
                    rs.getTime("start_time").toLocalTime(),
                    rs.getTime("end_time").toLocalTime(),
                    rs.getString("purpose")
                );
                bookings.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
    
    public static int getTotalRooms() {
        String query = "SELECT COUNT(*) FROM rooms";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getBookedRoomCount() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM bookings";

        try (Connection conn = getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                count = rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }


    public static ObservableList<Statistic> getRoomTypeCounts() {
        ObservableList<Statistic> list = FXCollections.observableArrayList();
        String query = "SELECT type, COUNT(*) AS count FROM rooms GROUP BY type";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                list.add(new Statistic(rs.getString("type"), rs.getInt("count")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public static Booking getBookingByRoomId(int roomId) {
        Booking booking = null;
        String sql = "SELECT * FROM bookings WHERE room_id = ? ORDER BY date DESC, start_time DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roomId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    booking = new Booking(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("user_id"),
                        rs.getDate("date").toLocalDate(),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime(),
                        rs.getString("purpose")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return booking;
    }
    
    public static boolean hasBookingConflict(int roomId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        String query = "SELECT COUNT(*) FROM bookings WHERE room_id = ? AND date = ? " +
                   "AND (start_time < ? AND end_time > ?)";
    try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, roomId);
        stmt.setDate(2, Date.valueOf(date));
        stmt.setTime(3, Time.valueOf(endTime));
        stmt.setTime(4, Time.valueOf(startTime));

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}