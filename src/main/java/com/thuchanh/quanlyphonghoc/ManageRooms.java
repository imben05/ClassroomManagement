package com.thuchanh.quanlyphonghoc;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DatabaseUtil;
import model.Rooms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import static javafx.application.Application.launch;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;

public class ManageRooms extends Application {
    
    private TextField searchField = new TextField();
    private FilteredList<Rooms> filteredRooms;
    private TableView<Rooms> tableView = new TableView<>();
    private TextField codeField = new TextField();
    private TextField nameField = new TextField();
    private TextField typeField = new TextField();
    private TextField capacityField = new TextField();
    private TextField statusField = new TextField();
    private TextField noteField = new TextField();
    private TextField imageField = new TextField();

    private Button updateButton = new Button("Update");
    private Button deleteButton = new Button("Delete");
    private Button browseButton = new Button("Browse");

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        VBox inputBox = new VBox(10);
        inputBox.setPadding(new Insets(10));
        inputBox.setStyle("-fx-background-color: #f4f4f4");

        codeField.setPromptText("Mã phòng");
        nameField.setPromptText("Tên phòng");
        typeField.setPromptText("Loại phòng");
        capacityField.setPromptText("Sức chứa");
        statusField.setPromptText("Trạng thái");
        noteField.setPromptText("Ghi chú");
        imageField.setPromptText("URL Hình ảnh");
        imageField.setEditable(false);

        browseButton.setText("Browse");
        browseButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        browseButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        browseButton.setOnAction(e -> browseImage());
        
        searchField.setPromptText("Tìm kiếm...");
        searchField.setMaxWidth(300);
        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
        String lower = newValue.toLowerCase();
        filteredRooms.setPredicate(room -> {
            if (lower.isEmpty()) return true;
            return room.getName().toLowerCase().contains(lower) || room.getCode().toLowerCase().contains(lower) || room.getType().toLowerCase().contains(lower);
        });
    });

        inputBox.getChildren().addAll(
            codeField, nameField, typeField, capacityField,
            statusField, noteField, imageField, browseButton, searchField
        );

        TableColumn<Rooms, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50);

        TableColumn<Rooms, String> codeCol = new TableColumn<>("Mã phòng");
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));

        TableColumn<Rooms, String> nameCol = new TableColumn<>("Tên phòng");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Rooms, String> typeCol = new TableColumn<>("Loại phòng");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Rooms, Integer> capCol = new TableColumn<>("Sức chứa");
        capCol.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        TableColumn<Rooms, String> statusCol = new TableColumn<>("Trạng thái");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Rooms, String> noteCol = new TableColumn<>("Ghi chú");
        noteCol.setCellValueFactory(new PropertyValueFactory<>("teacherNote"));

        TableColumn<Rooms, String> imgCol = new TableColumn<>("URL Hình ảnh");
        imgCol.setCellValueFactory(new PropertyValueFactory<>("imagePath"));
        imgCol.setCellFactory(param -> new ImageViewTableCell<>());
        imgCol.setPrefWidth(100);

        tableView.getColumns().addAll(idCol, codeCol, nameCol, typeCol, capCol, statusCol, noteCol, imgCol);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                codeField.setText(newVal.getCode());
                nameField.setText(newVal.getName());
                typeField.setText(newVal.getType());
                capacityField.setText(String.valueOf(newVal.getCapacity()));
                statusField.setText(newVal.getStatus());
                noteField.setText(newVal.getTeacherNote());
                imageField.setText(newVal.getImagePath());
            }
        });
        loadRooms();

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10));

        updateButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        updateButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        updateButton.setOnAction(e -> updateRooms());

        deleteButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 14));
        deleteButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> deleteRooms());

        buttons.getChildren().addAll(updateButton, deleteButton);

        root.setTop(inputBox);
        root.setCenter(tableView);
        root.setBottom(buttons);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Quản lý phòng học");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadRooms() {
        ObservableList<Rooms> rooms = DatabaseUtil.getRooms();
        filteredRooms = new FilteredList<>(rooms, p -> true);
        SortedList<Rooms> sortedRooms = new SortedList<>(filteredRooms);
        sortedRooms.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedRooms);
    }

    private void updateRooms() {
        Rooms selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                selected.setCode(codeField.getText());
                selected.setName(nameField.getText());
                selected.setType(typeField.getText());
                selected.setCapacity(Integer.parseInt(capacityField.getText()));
                selected.setStatus(statusField.getText());
                selected.setTeacherNote(noteField.getText());
                selected.setImagePath(imageField.getText());

                if (DatabaseUtil.updateRooms(selected)) {
                    loadRooms();
                }
            } catch (NumberFormatException e) {
                System.out.println("Sức chứa phải là số.");
            }
        }
    }

    private void deleteRooms() {
        Rooms selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null && DatabaseUtil.deleteRooms(selected.getId())) {
            loadRooms();
            clearFields();
        }
    }

    private void clearFields() {
        codeField.clear();
        nameField.clear();
        typeField.clear();
        capacityField.clear();
        statusField.clear();
        noteField.clear();
        imageField.clear();
    }

    private void browseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn hình ảnh");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            imageField.setText(file.getAbsolutePath());
        }
    }

    private static class ImageViewTableCell<S> extends TableCell<S, String> {
        private final ImageView imageView = new ImageView();

        @Override
        protected void updateItem(String path, boolean empty) {
            super.updateItem(path, empty);
            if (empty || path == null) {
                setGraphic(null);
            } else {
                try {
                    Image image = new Image(new FileInputStream(path));
                    imageView.setImage(image);
                    imageView.setFitWidth(60);
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                } catch (FileNotFoundException e) {
                    setGraphic(null);
                    System.err.println("Image not found: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
