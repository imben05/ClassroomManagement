package com.thuchanh.quanlyphonghoc;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Booking;
import model.DatabaseUtil;

public class HistoryBookings extends Application {

    private TableView<Booking> tableView = new TableView<>();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label title = new Label("Booking History");
        title.setFont(Font.font("Tahoma", 28));
        VBox topBox = new VBox(title);
        topBox.setPadding(new Insets(10));
        root.setTop(topBox);

        TableColumn<Booking, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Booking, String> codeCol = new TableColumn<>("Mã phòng");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("roomCode"));

        TableColumn<Booking, String> nameCol = new TableColumn<>("Tên phòng");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("roomName"));

        TableColumn<Booking, String> typeCol = new TableColumn<>("Loại phòng");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("roomType"));;

        TableColumn<Booking, String> dateCol = new TableColumn<>("Ngày");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Booking, String> startCol = new TableColumn<>("Thời gian bắt đầu");
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));

        TableColumn<Booking, String> endCol = new TableColumn<>("Thời gian kết thúc");
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));

        TableColumn<Booking, String> purposeCol = new TableColumn<>("Ghi chú");
        purposeCol.setCellValueFactory(new PropertyValueFactory<>("purpose"));

        tableView.getColumns().addAll(idCol, codeCol, nameCol, typeCol, dateCol, startCol, endCol, purposeCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        root.setCenter(tableView);

        ObservableList<Booking> bookings = DatabaseUtil.getBookings();
        tableView.setItems(bookings);

        Scene scene = new Scene(root, 950, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Lịch sử đặt phòng");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
