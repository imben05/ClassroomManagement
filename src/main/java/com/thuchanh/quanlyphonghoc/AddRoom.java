package com.thuchanh.quanlyphonghoc;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.DatabaseUtil;

import java.io.File;
import javafx.scene.control.Alert;

public class AddRoom extends Application {

    private Stage stage;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #FFFFFF;");
        Alert thongBao = new Alert(Alert.AlertType.INFORMATION);

        Label titleLabel = new Label("Add Room");
        titleLabel.setFont(Font.font("Tahoma", 36));
        titleLabel.setTextFill(Color.BLACK);
        BorderPane.setMargin(titleLabel, new Insets(20, 0, 20, 0));
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        TextField codeField = createTextField("Kí hiệu phòng (HA0804)");
        TextField nameField = createTextField("Tên phòng");
        TextField typeField = createTextField("Loại phòng (Lý thuyết, Thực hành...)");
        TextField capacityField = createTextField("Sức chứa");
        TextField noteField = createTextField("Ghi chú");

        TextField imageField = new TextField();
        imageField.setPromptText("Hình ảnh");
        imageField.setFont(Font.font("Tahoma", 14));
        imageField.setPrefHeight(40);
        imageField.setEditable(false);

        Button browseButton = new Button("Browse");
        browseButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        browseButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        browseButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose Room Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
            );
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                imageField.setText(selectedFile.getAbsolutePath());
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        saveButton.setOnAction(e -> {
            try {
                String code = codeField.getText();
                String name = nameField.getText();
                String type = typeField.getText();
                String capacityText = capacityField.getText();
                int capacity = Integer.parseInt(capacityText);
                String status = "Trống";
                String teacherNote = noteField.getText();
                String imagePath = imageField.getText();
                
                if (code.isEmpty()) {
                    thongBao.setContentText("Vui lòng nhập mã phòng.");
                    thongBao.show();
                return;
                }
                if (name.isEmpty()) {
                    thongBao.setContentText("Vui lòng nhập tên phòng.");
                    thongBao.show();
                    return;
                }
                if (type.isEmpty()) {
                    thongBao.setContentText("Vui lòng nhập loại phòng.");
                    thongBao.show();
                    return;
                }
                if (capacityText.isEmpty()) {
                    thongBao.setContentText("Vui lòng nhập sức chứa.");
                    thongBao.show();
                    return;
                }
                boolean added = DatabaseUtil.addRooms(code, name, type, capacity, teacherNote, imagePath);

                if (added) {
                    thongBao.setContentText("Thêm phòng thành công!");
                    thongBao.show();
                    clearFields(codeField, nameField, typeField, capacityField, noteField, imageField);
                } else {
                    thongBao.setContentText("Thêm phòng thất bại!");
                    thongBao.show();
                }
            } catch (NumberFormatException ex) {
                System.out.println("Sức chứa phải là số.");
            }
        });

        Button backButton = new Button("Trở về");
        backButton.setFont(Font.font("Tahoma", FontWeight.BOLD, 24));
        backButton.setStyle("-fx-background-color: #332C25; -fx-text-fill: white;");
        backButton.setOnAction(e -> stage.close());

        VBox formBox = new VBox(10,
                codeField, nameField, typeField, capacityField,
                noteField, imageField, browseButton);
        formBox.setSpacing(15);
        formBox.setAlignment(Pos.CENTER);

        HBox buttonBox = new HBox(10, saveButton, backButton);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.setAlignment(Pos.CENTER);

        VBox centerBox = new VBox(formBox, buttonBox);
        centerBox.setSpacing(10);
        centerBox.setAlignment(Pos.CENTER);

        root.setTop(titleLabel);
        root.setCenter(centerBox);

        Scene scene = new Scene(root, 600, 650);
        primaryStage.setTitle("Add Room");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private TextField createTextField(String prompt) {
        TextField textField = new TextField();
        textField.setPromptText(prompt);
        textField.setFont(Font.font("Tahoma", 14));
        textField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #cccccc;");
        textField.setPrefHeight(40);
        return textField;
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
