package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Booking {
    private int id;
    private int roomId;
    private int userId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String purpose;
    private String roomName;
    private String roomCode;
    private String roomType;

    public Booking(int id, int roomId, int userId, LocalDate date, LocalTime startTime, LocalTime endTime, String purpose) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.purpose = purpose;
    }

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id;
    }

    public int getRoomId() { 
        return roomId;
    }
    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getUserId() { 
        return userId;
    }
    public void setUserId(int userId) { 
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) { 
        this.date = date;
    }

    public LocalTime getStartTime() { 
        return startTime;
    }
    public void setStartTime(LocalTime startTime) { 
        this.startTime = startTime;
    }

    public LocalTime getEndTime() { 
        return endTime;
    }
    public void setEndTime(LocalTime endTime) { 
        this.endTime = endTime;
    }

    public String getPurpose() { 
        return purpose; 
    }
    public void setPurpose(String purpose) { 
        this.purpose = purpose;
    }
    
    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    public String getRoomCode() { 
        return roomCode; 
    }
    public void setRoomCode(String roomCode) { 
        this.roomCode = roomCode; 
    }
    public String getRoomType() { 
        return roomType; 
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType; 
    }
}
