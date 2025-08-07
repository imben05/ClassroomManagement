package model;

public class Rooms {
    private int id;
    private String code;         
    private String name;         
    private String type;         
    private int capacity;        
    private String status;       
    private String teacherNote;  
    private String imagePath;  

    public Rooms(int id, String code, String name, String type, int capacity, String status, String teacherNote, String imagePath) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.capacity = capacity;
        this.status = status;
        this.teacherNote = teacherNote;
        this.imagePath = imagePath;
    }

    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public String getCode() {
        return code; 
    }
    public void setCode(String code) { 
        this.code = code;
    }

    public String getName() { 
        return name; 
    }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getType() { 
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() { 
        return capacity; 
    }
    public void setCapacity(int capacity) { 
        this.capacity = capacity;
    }

    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status;
    }

    public String getTeacherNote() { 
        return teacherNote; 
    }
    public void setTeacherNote(String teacherNote) {
        this.teacherNote = teacherNote;
    }

    public String getImagePath() { 
        return imagePath; 
    }
    public void setImagePath(String imagePath) { 
        this.imagePath = imagePath;
    }
}
