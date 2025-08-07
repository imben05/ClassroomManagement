package com.thuchanh.quanlyphonghoc;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Booking;
import model.DatabaseUtil;
import model.Rooms;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import javafx.util.StringConverter;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class BookRoom extends Application {

    private TableView<Booking> bookingTableView = new TableView<>();
    private TableView<Rooms> roomTableView = new TableView<>();
    private TabPane tabPane = new TabPane();
    private Timeline statusUpdateTimeline;
    
    // Thêm reference để có thể truy cập từ các method khác
    private ComboBox<Rooms> roomComboBoxRef;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        
        // Tạo TabPane để chuyển đổi giữa 2 view
        Tab bookingTab = new Tab("Danh sách đặt phòng");
        bookingTab.setContent(createBookingTableView());
        bookingTab.setClosable(false);
        
        Tab roomTab = new Tab("Danh sách phòng");
        roomTab.setContent(createRoomTableView());
        roomTab.setClosable(false);
        
        tabPane.getTabs().addAll(bookingTab, roomTab);

        // Form đặt phòng
        VBox formBox = createBookingForm();

        root.setLeft(tabPane);
        root.setCenter(formBox);

        Scene scene = new Scene(root, 1100, 500);
        primaryStage.setTitle("Đặt phòng");
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Xử lý sự kiện đóng cửa sổ
        primaryStage.setOnCloseRequest(e -> {
            stopRoomStatusTimer();
        });
        
        // Load dữ liệu ban đầu
        refreshData();
    }
    
    // Tạo bảng hiển thị booking
    private VBox createBookingTableView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));
        
        TableColumn<Booking, String> colRoomCode = new TableColumn<>("Mã phòng");
        colRoomCode.setCellValueFactory(data -> {
            Rooms room = DatabaseUtil.getRoomById(data.getValue().getRoomId());
            return new SimpleStringProperty(room != null ? room.getCode() : "");
        });
        colRoomCode.setPrefWidth(80);

        TableColumn<Booking, String> colRoomName = new TableColumn<>("Tên phòng");
        colRoomName.setCellValueFactory(data -> {
            Rooms room = DatabaseUtil.getRoomById(data.getValue().getRoomId());
            return new SimpleStringProperty(room != null ? room.getName() : "");
        });
        colRoomName.setPrefWidth(120);

        TableColumn<Booking, String> colRoomType = new TableColumn<>("Loại phòng");
        colRoomType.setCellValueFactory(data -> {
            Rooms room = DatabaseUtil.getRoomById(data.getValue().getRoomId());
            return new SimpleStringProperty(room != null ? room.getType() : "");
        });
        colRoomType.setPrefWidth(100);
        
        TableColumn<Booking, Integer> colCapacity = new TableColumn<>("Sức chứa");
        colCapacity.setCellValueFactory(data -> {
            Rooms room = DatabaseUtil.getRoomById(data.getValue().getRoomId());
            return new SimpleIntegerProperty(room != null ? room.getCapacity() : 0).asObject();
        });
        colCapacity.setPrefWidth(80);

        TableColumn<Booking, String> colDate = new TableColumn<>("Ngày đặt");
        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate().toString()));
        colDate.setPrefWidth(100);

        TableColumn<Booking, String> colStart = new TableColumn<>("Giờ bắt đầu");
        colStart.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStartTime().toString()));
        colStart.setPrefWidth(80);

        TableColumn<Booking, String> colEnd = new TableColumn<>("Giờ kết thúc");
        colEnd.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEndTime().toString()));
        colEnd.setPrefWidth(80);
        
        TableColumn<Booking, String> colPurpose = new TableColumn<>("Mục đích");
        colPurpose.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPurpose()));
        colPurpose.setPrefWidth(150);

        bookingTableView.getColumns().addAll(colRoomCode, colRoomName, colRoomType, colCapacity, colDate, colStart, colEnd, colPurpose);
        bookingTableView.setPrefWidth(750);
        bookingTableView.setPrefHeight(400);
        
        // Thêm sự kiện double-click để chọn phòng từ booking
        bookingTableView.setRowFactory(tv -> {
            TableRow<Booking> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Booking selectedBooking = row.getItem();
                    selectRoomFromBooking(selectedBooking);
                }
            });
            return row;
        });
        
        Label instructionLabel = new Label("💡 Double-click vào booking để chọn phòng đó");
        instructionLabel.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
        
        container.getChildren().addAll(instructionLabel, bookingTableView);
        return container;
    }
    
    // Tạo bảng hiển thị phòng với tất cả booking
    private VBox createRoomTableView() {
        VBox container = new VBox(10);
        container.setPadding(new Insets(10));

        TableColumn<Rooms, String> colCode = new TableColumn<>("Mã phòng");
        colCode.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCode()));
        colCode.setPrefWidth(80);

        TableColumn<Rooms, String> colName = new TableColumn<>("Tên phòng");
        colName.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        colName.setPrefWidth(120);

        TableColumn<Rooms, String> colType = new TableColumn<>("Loại phòng");
        colType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        colType.setPrefWidth(100);
        
        TableColumn<Rooms, Integer> colCapacity = new TableColumn<>("Sức chứa");
        colCapacity.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCapacity()).asObject());
        colCapacity.setPrefWidth(80);

        TableColumn<Rooms, String> colStatus = new TableColumn<>("Trạng thái");
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));
        colStatus.setPrefWidth(100);
        
        // Hiển thị tất cả booking của phòng
        TableColumn<Rooms, String> colAllBookings = new TableColumn<>("Lịch đặt");
        colAllBookings.setCellValueFactory(data -> {
            ObservableList<Booking> bookings = DatabaseUtil.getBookingsByRoomId(data.getValue().getId());
            StringBuilder bookingInfo = new StringBuilder();
            
            for (Booking booking : bookings) {
                if (bookingInfo.length() > 0) bookingInfo.append("\n");
                bookingInfo.append(String.format("%s: %s-%s", 
                    booking.getDate().toString(),
                    booking.getStartTime().toString(),
                    booking.getEndTime().toString()));
            }
            
            return new SimpleStringProperty(bookingInfo.toString());
        });
        colAllBookings.setPrefWidth(200);
        
        // Cho phép text wrap trong cell
        colAllBookings.setCellFactory(tc -> {
            TableCell<Rooms, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(colAllBookings.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell;
        });

        roomTableView.getColumns().addAll(colCode, colName, colType, colCapacity, colStatus, colAllBookings);
        roomTableView.setPrefWidth(750);
        roomTableView.setPrefHeight(400);
        
        // Thêm sự kiện double-click để chọn phòng từ bảng rooms
        roomTableView.setRowFactory(tv -> {
            TableRow<Rooms> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Rooms selectedRoom = row.getItem();
                    selectRoomFromTable(selectedRoom);
                }
            });
            return row;
        });
        
        Label instructionLabel2 = new Label("💡 Double-click vào phòng để chọn phòng đó");
        instructionLabel2.setStyle("-fx-text-fill: #666; -fx-font-size: 12px;");
        
        container.getChildren().addAll(instructionLabel2, roomTableView);
        return container;
    }

    private VBox createBookingForm() {
        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(20));
        formBox.setStyle("-fx-background-color: #F5F5F5; -fx-border-color: #DDD; -fx-border-width: 1;");
        formBox.setAlignment(Pos.TOP_LEFT);
        formBox.setPrefWidth(300);

        // Tiêu đề
        Label titleLabel = new Label("FORM ĐẶT PHÒNG");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        // ComboBox để chọn phòng
        roomComboBoxRef = new ComboBox<>();
        roomComboBoxRef.setPromptText("Chọn phòng");
        roomComboBoxRef.setPrefWidth(250);
        roomComboBoxRef.setConverter(new StringConverter<Rooms>() {
            @Override
            public String toString(Rooms room) {
                return room != null ? "[" + room.getCode() + "] " + room.getName() + " (" + room.getType() + ", " + room.getCapacity() + " chỗ)" : "";
            }

            @Override
            public Rooms fromString(String string) {
                return null;
            }
        });

        DatePicker datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(250);
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #eeeeee;");
                }
            }
        });
        
        TextField startField = new TextField();
        startField.setPromptText("Giờ bắt đầu (HH:mm)");
        startField.setPrefWidth(250);

        TextField endField = new TextField();
        endField.setPromptText("Giờ kết thúc (HH:mm)");
        endField.setPrefWidth(250);

        TextArea noteArea = new TextArea();
        noteArea.setPromptText("Mục đích sử dụng");
        noteArea.setPrefRowCount(3);
        noteArea.setPrefWidth(250);
        noteArea.setWrapText(true);

        Button bookBtn = new Button("Đặt Phòng");
        bookBtn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px;");
        bookBtn.setPrefWidth(120);
        bookBtn.setOnAction(e -> handleBooking(roomComboBoxRef, datePicker, startField, endField, noteArea));

        Button refreshBtn = new Button("Làm mới");
        refreshBtn.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10px 20px;");
        refreshBtn.setPrefWidth(120);
        refreshBtn.setOnAction(e -> {
            refreshData();
            refreshComboBoxData(roomComboBoxRef);
            clearForm(roomComboBoxRef, datePicker, startField, endField, noteArea);
        });
        
        Button updateStatusBtn = new Button("Cập nhật trạng thái");
        updateStatusBtn.setStyle("-fx-background-color: #FFC107; -fx-text-fill: black; -fx-font-weight: bold; -fx-padding: 8px 15px;");
        updateStatusBtn.setPrefWidth(140);
        updateStatusBtn.setOnAction(e -> manualUpdateRoomStatuses());

        HBox buttonBox = new HBox(10, bookBtn, refreshBtn, updateStatusBtn);
        buttonBox.setAlignment(Pos.CENTER);

        formBox.getChildren().addAll(
                titleLabel,
                new Separator(),
                new Label("Chọn phòng:"), roomComboBoxRef,
                new Label("Ngày đặt:"), datePicker,
                new Label("Giờ bắt đầu:"), startField,
                new Label("Giờ kết thúc:"), endField,
                new Label("Mục đích:"), noteArea,
                buttonBox
        );

        // Load danh sách phòng vào ComboBox ngay lập tức
        refreshComboBoxData(roomComboBoxRef);

        return formBox;
    }
    
    // Method riêng để load dữ liệu vào ComboBox
    private void refreshComboBoxData(ComboBox<Rooms> roomComboBox) {
        try {
            ObservableList<Rooms> rooms = DatabaseUtil.getRooms();
            roomComboBox.setItems(rooms);
            System.out.println("Loaded " + rooms.size() + " rooms into ComboBox");
        } catch (Exception e) {
            System.err.println("Error loading rooms: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Method để chọn phòng từ bảng booking (double-click)
    private void selectRoomFromBooking(Booking booking) {
        Rooms room = DatabaseUtil.getRoomById(booking.getRoomId());
        if (room != null && roomComboBoxRef != null) {
            // Tìm và chọn room trong ComboBox
            for (Rooms r : roomComboBoxRef.getItems()) {
                if (r.getId() == room.getId()) {
                    roomComboBoxRef.setValue(r);
                    break;
                }
            }
            // Chuyển sang tab form đặt phòng
            tabPane.getSelectionModel().select(0);
            showAlert("Đã chọn phòng: [" + room.getCode() + "] " + room.getName() + "\nBạn có thể đặt thêm khung giờ khác cho phòng này!");
        }
    }
    
    // Method để chọn phòng từ bảng rooms (double-click)
    private void selectRoomFromTable(Rooms room) {
        if (room != null && roomComboBoxRef != null) {
            // Tìm và chọn room trong ComboBox
            for (Rooms r : roomComboBoxRef.getItems()) {
                if (r.getId() == room.getId()) {
                    roomComboBoxRef.setValue(r);
                    break;
                }
            }
            // Chuyển sang tab form đặt phòng
            tabPane.getSelectionModel().select(0);
            showAlert("Đã chọn phòng: [" + room.getCode() + "] " + room.getName() + "\nHãy chọn ngày giờ để đặt phòng!");
        }
    }

    private void handleBooking(ComboBox<Rooms> roomComboBox, DatePicker datePicker, TextField start, TextField end, TextArea note) {
        try {
            Rooms selectedRoom = roomComboBox.getValue();
            
            // Validation
            if (selectedRoom == null) {
                showAlert("Vui lòng chọn phòng.");
                return;
            }

            LocalDate date = datePicker.getValue();
            if (date == null) {
                showAlert("Vui lòng chọn ngày.");
                return;
            }
            
            String startText = start.getText().trim();
            String endText = end.getText().trim();
            
            if (startText.isEmpty() || endText.isEmpty()) {
                showAlert("Vui lòng nhập đầy đủ giờ bắt đầu và kết thúc.");
                return;
            }
            
            // Validate time format
            LocalTime startTime, endTime;
            try {
                startTime = LocalTime.parse(startText);
                endTime = LocalTime.parse(endText);
            } catch (Exception e) {
                showAlert("Định dạng giờ không hợp lệ. Vui lòng nhập theo định dạng HH:mm (ví dụ: 08:30)");
                return;
            }
            
            String purpose = note.getText().trim();
            
            if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
                showAlert("Giờ kết thúc phải sau giờ bắt đầu!");
                return;
            }

            // Kiểm tra trùng lịch
            if (isRoomAlreadyBooked(selectedRoom.getId(), date, startTime, endTime)) {
                showAlert("Phòng đã có người đặt trong khung giờ này!\nVui lòng chọn khung giờ khác.");
                return;
            }
            
            Booking booking = new Booking(
                    0,
                    selectedRoom.getId(),
                    1,
                    date,
                    startTime,
                    endTime,
                    purpose.isEmpty() ? "Không có ghi chú" : purpose
            );

            boolean success = DatabaseUtil.addBooking(booking);
            if (success) {
                // Cập nhật trạng thái phòng nếu cần
                DatabaseUtil.updateRoomStatus(selectedRoom.getId(), "Đã đặt");
                
                // Làm mới dữ liệu
                refreshData();
                refreshComboBoxData(roomComboBox);
                
                // Clear form
                clearForm(roomComboBox, datePicker, start, end, note);
                
                showAlert("Đặt phòng thành công!\nPhòng: [" + selectedRoom.getCode() + "] " + selectedRoom.getName() + 
                         "\nNgày: " + date + 
                         "\nGiờ: " + startTime + " - " + endTime);
            } else {
                showAlert("Đặt phòng thất bại. Vui lòng thử lại.");
            }        
        } catch (Exception e) {
            showAlert("Lỗi: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Helper method để clear form
    private void clearForm(ComboBox<Rooms> roomComboBox, DatePicker datePicker, TextField start, TextField end, TextArea note) {
        roomComboBox.setValue(null);
        datePicker.setValue(LocalDate.now());
        start.clear();
        end.clear();
        note.clear();
    }
    
    private void refreshData() {
        try {
            bookingTableView.setItems(DatabaseUtil.getBookings());
            roomTableView.setItems(DatabaseUtil.getRooms());
        } catch (Exception e) {
            System.err.println("Error refreshing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean isRoomAlreadyBooked(int roomId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        try {
            ObservableList<Booking> allBookings = DatabaseUtil.getBookings();
            for (Booking b : allBookings) {
                if (b.getRoomId() == roomId && b.getDate().equals(date)) {
                    // Kiểm tra overlap: hai khoảng thời gian overlap nếu không có khoảng nào kết thúc trước khi khoảng kia bắt đầu
                    boolean overlap = !(endTime.isBefore(b.getStartTime()) || endTime.equals(b.getStartTime()) || 
                                       startTime.isAfter(b.getEndTime()) || startTime.equals(b.getEndTime()));
                    if (overlap) {
                        System.out.println("Time conflict found: " + startTime + "-" + endTime + " overlaps with " + 
                                         b.getStartTime() + "-" + b.getEndTime());
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking booking conflicts: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    /**
     * Khởi động Timeline để tự động check và cập nhật trạng thái phòng
     * Chạy mỗi 30 giây để kiểm tra
     */
    private void startRoomStatusTimer() {
        statusUpdateTimeline = new Timeline(
            new KeyFrame(Duration.seconds(30), e -> updateAllRoomStatuses())
        );
        statusUpdateTimeline.setCycleCount(Timeline.INDEFINITE);
        statusUpdateTimeline.play();
        
        System.out.println("Room status timer started - checking every 30 seconds");
        
        // Chạy ngay lần đầu
        updateAllRoomStatuses();
    }
    
    /**
     * Cập nhật trạng thái tất cả phòng dựa trên thời gian hiện tại
     */
    private void updateAllRoomStatuses() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDate today = now.toLocalDate();
            LocalTime currentTime = now.toLocalTime();
            
            System.out.println("Checking room statuses at: " + now);
            
            ObservableList<Rooms> allRooms = DatabaseUtil.getRooms();
            boolean hasChanges = false;
            
            for (Rooms room : allRooms) {
                String newStatus = calculateRoomStatus(room.getId(), today, currentTime);
                
                // Chỉ update nếu status thay đổi
                if (!newStatus.equals(room.getStatus())) {
                    System.out.println("Updating room [" + room.getCode() + "] " + room.getName() + 
                                     " from '" + room.getStatus() + "' to '" + newStatus + "'");
                    
                    DatabaseUtil.updateRoomStatus(room.getId(), newStatus);
                    hasChanges = true;
                }
            }
            
            // Refresh UI nếu có thay đổi
            if (hasChanges) {
                refreshData();
                refreshComboBoxData(roomComboBoxRef);
                System.out.println("Room statuses updated and UI refreshed");
            }
            
        } catch (Exception e) {
            System.err.println("Error updating room statuses: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Tính toán trạng thái phòng dựa trên booking hiện tại
     * @param roomId ID của phòng
     * @param today Ngày hiện tại
     * @param currentTime Giờ hiện tại
     * @return Trạng thái phòng: "Đang sử dụng", "Đã đặt", hoặc "Trống"
     */
    private String calculateRoomStatus(int roomId, LocalDate today, LocalTime currentTime) {
        try {
            ObservableList<Booking> roomBookings = DatabaseUtil.getBookingsByRoomId(roomId);
            
            // Kiểm tra booking hôm nay
            for (Booking booking : roomBookings) {
                if (booking.getDate().equals(today)) {
                    LocalTime startTime = booking.getStartTime();
                    LocalTime endTime = booking.getEndTime();
                    
                    // Đang trong giờ sử dụng
                    if (!currentTime.isBefore(startTime) && currentTime.isBefore(endTime)) {
                        return "Đang sử dụng";
                    }
                    
                    // Đã đặt cho tương lai (hôm nay)
                    if (currentTime.isBefore(startTime)) {
                        return "Đã đặt";
                    }
                }
            }
            
            // Kiểm tra có booking trong tương lai không
            for (Booking booking : roomBookings) {
                if (booking.getDate().isAfter(today)) {
                    return "Đã đặt";
                }
            }
            
            // Không có booking nào
            return "Trống";
            
        } catch (Exception e) {
            System.err.println("Error calculating room status for room " + roomId + ": " + e.getMessage());
            return "Trống"; // Default status
        }
    }
    
    /**
     * Method để manually check và update trạng thái phòng
     * Có thể gọi từ button hoặc menu
     */
    public void manualUpdateRoomStatuses() {
        updateAllRoomStatuses();
        showAlert("Đã cập nhật trạng thái tất cả phòng!");
    }
    
    /**
     * Dừng timer khi đóng ứng dụng
     */
    public void stopRoomStatusTimer() {
        if (statusUpdateTimeline != null) {
            statusUpdateTimeline.stop();
            System.out.println("Room status timer stopped");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}