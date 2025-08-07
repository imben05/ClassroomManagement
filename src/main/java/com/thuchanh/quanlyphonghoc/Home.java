package com.thuchanh.quanlyphonghoc;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Home extends Application {
    private boolean isLoggedIn = true;
    private BorderPane root;
    private Label lblTime;
    private Label lblPM;
    private Label lblDay;
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("EEEE, dd-MM-yyyy");

    @Override
    public void start(Stage primaryStage) {
        root = new BorderPane();

        ImageView backgroundImage = new ImageView();
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/room.png"));
            backgroundImage.setImage(image);
            backgroundImage.setFitWidth(1200);
            backgroundImage.setFitHeight(720);
            backgroundImage.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }

        GridPane gridPane = createGridPane();
        gridPane.setAlignment(Pos.CENTER_LEFT);

        lblTime = createLabel(LocalDateTime.now().format(timeFormatter), 24, Color.WHITE);
        lblPM = createLabel("PM", 24, Color.WHITE);
        lblDay = createLabel(LocalDateTime.now().format(dateFormatter), 24, Color.WHITE);

        Label lblTitle = createLabel("ROOM MANAGER", 40, Color.web("#FFFFFF"));
        //bán kính blur mờ 10 - spread - độ lan tỏa bóng - offset x,y : lệch sang phải xuống dưới 2px
        lblTitle.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.8), 10, 0.5, 2, 2);");
        lblTitle.setPadding(new Insets(10));

        HBox topLabels = new HBox(10, lblTime, lblPM, lblDay);
        topLabels.setPadding(new Insets(20, 20, 0, 0));
        topLabels.setAlignment(Pos.TOP_RIGHT);

        StackPane rightSide = new StackPane();
        rightSide.getChildren().addAll(backgroundImage, lblTitle, topLabels);
        StackPane.setAlignment(backgroundImage, Pos.CENTER);
        StackPane.setAlignment(lblTitle, Pos.TOP_CENTER);
        StackPane.setAlignment(topLabels, Pos.TOP_RIGHT);

        Button loginButton = new Button(isLoggedIn ? "Log Out" : "Login");
        loginButton.setOnAction(e -> handleButtonClick("Log Out", loginButton, primaryStage));
        styleButton(loginButton);
        loginButton.setMinWidth(150);
        loginButton.setPrefWidth(200);
        loginButton.setMinHeight(40);
        loginButton.setPrefHeight(40);

        ImageView additionalImageView = new ImageView();
        try {
            Image additionalImage = new Image(getClass().getResourceAsStream("/images/logo.png"));
            additionalImageView.setImage(additionalImage);
            additionalImageView.setPreserveRatio(true);
        } catch (Exception e) {
            System.out.println("Error loading additional image: " + e.getMessage());
        }

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        VBox.setMargin(additionalImageView, new Insets(30, 10, 0, 10));

        VBox leftSide = new VBox(25, additionalImageView, gridPane, spacer, loginButton);
        leftSide.setPadding(new Insets(30, 25, 30, 25));
        leftSide.setStyle("-fx-background-color: #000000");
        leftSide.setAlignment(Pos.TOP_CENTER);

        root.setLeft(leftSide);
        root.setRight(rightSide);

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            lblTime.setText(LocalDateTime.now().format(timeFormatter));
            lblDay.setText(LocalDateTime.now().format(dateFormatter));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(root, 1380, 720);
        primaryStage.setTitle("ROOM MANAGER - Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(20);

        String[] buttonLabels = {"Add Rooms", "Manage Rooms", "Book Rooms", "History", "Statistics"};
        for (int i = 0; i < buttonLabels.length; i++) {
            Button button = new Button(buttonLabels[i]);
            styleButton(button);
            button.setMinWidth(150);
            button.setPrefWidth(200);
            button.setMinHeight(40);
            button.setPrefHeight(40);

            button.setOnAction(e -> handleButtonClick(button.getText(), null, null));
            gridPane.add(button, 0, i);
        }

        return gridPane;
    }

    private void handleButtonClick(String buttonText, Button loginButton, Stage currentStage) {
        if (buttonText.equals("Add Rooms")) {
            AddRoom addRoom = new AddRoom();
            addRoom.start(new Stage());
        } else if (buttonText.equals("Manage Rooms")) {
            ManageRooms mnR = new ManageRooms();
            mnR.start(new Stage());
        } else if (buttonText.equals("Book Rooms")) {
            BookRoom bkR = new BookRoom();
            bkR.start(new Stage());
        } else if (buttonText.equals("History")) {
            HistoryBookings hBooking = new HistoryBookings();
            hBooking.start(new Stage());
        } else if (buttonText.equals("Statistics")) {
            StatisticView svSta = new StatisticView();
            svSta.start(new Stage());
        } else if (buttonText.equals("Log Out")) {
            isLoggedIn = false;
            if (loginButton != null) {
                loginButton.setText("Login");
            }
            showSuccessMessage("Logged out successfully.");
            if (currentStage != null) {
                Login login = new Login();
                login.start(new Stage());
                currentStage.close();
            }
        }
    }

    private Label createLabel(String text, int fontSize, Color color) {
        Label label = new Label(text);
        label.setFont(new Font("SansSerif", fontSize));
        label.setTextFill(color);
        return label;
    }

    private void styleButton(Button button) {
        button.setFont(new Font("SansSerif", 18));
        button.setStyle("-fx-background-color: transparent; " +
                "-fx-border-color: white; " +
                "-fx-border-width: 1px; " +
                "-fx-text-fill: white; " +
                "-fx-border-radius: 8px; " +
                "-fx-background-radius: 8px;");

        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: #6a6a6a;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;"
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 1px;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-background-radius: 8px;"
        ));
    }

    private void showSuccessMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
        