package com.thuchanh.quanlyphonghoc;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.DatabaseUtil;

public class Login extends Application {

    private StackPane passwordStack;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        Alert thongBao = new Alert(Alert.AlertType.INFORMATION);
        
        Label titleLabel = new Label("LOGIN ADMIN");
        
        titleLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: black; -fx-font-family: 'Tahoma'");
        BorderPane.setAlignment(titleLabel, javafx.geometry.Pos.CENTER);


       
        HBox hboxTitle= new HBox();
        hboxTitle.getChildren().addAll( titleLabel);
        
       
        VBox formContainer = new VBox(10);
        formContainer.setPadding(new Insets(20)); 
        
        TextField userNameField = new TextField();
        userNameField.setStyle("-fx-font-size: 16px; -fx-pref-width: 200px;-fx-border-radius: 10px; -fx-background-radius: 10px;");
        userNameField.setPromptText("UserName");

        passwordStack = new StackPane();
        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-font-size: 16px; -fx-pref-width: 200px; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        passwordField.setPromptText("Password");

        passwordStack.getChildren().addAll(passwordField);

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-font-size: 18px; -fx-pref-width: 100px; -fx-background-color:#000000; -fx-text-fill: white; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        loginButton.setOnAction(e -> {
        String username = userNameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
        showError("Please enter username and password.");
        return;
        }

        boolean loggedIn = DatabaseUtil.loginAdmin(username, password);
        if (loggedIn) {
            thongBao.setContentText("Login successful.");
            thongBao.show();
            openHomePage(primaryStage);
        } else {
            thongBao.setContentText("Invalid username or password.");
            thongBao.show();
        }
    });

        Button cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-font-size: 18px; -fx-pref-width: 100px;-fx-text-fill: white;-fx-background-color:#000000; -fx-border-radius: 10px; -fx-background-radius: 10px;");
        cancelButton.setOnAction(e -> primaryStage.close());

        HBox buttonContainer = new HBox(10);
        buttonContainer.getChildren().addAll(loginButton, cancelButton);

        formContainer.getChildren().addAll(
               userNameField,
                passwordStack,
                buttonContainer
        );
        VBox content = new VBox(20);
        content.getChildren().addAll(hboxTitle, formContainer);
        content.setPadding(new Insets(10, 20, 20, 20));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color:white");
        
    
        ImageView loginImage= new ImageView(new Image(getClass().getResourceAsStream("/images/logo.png")));
        
        Label lbLogin= new Label("ROOM MANAGEMENT");
        lbLogin.setStyle("-fx-font-size: 28px; -fx-text-fill: white; -fx-font-family: 'Arial'; -fx-font-weight: bold");
        
        VBox vboxLeft= new VBox(30);
        vboxLeft.getChildren().addAll(loginImage, lbLogin);
        vboxLeft.setStyle("-fx-background-color:#000000; -fx-alignment: center; -fx-padding: 20px;");
        vboxLeft.setPrefWidth(500);
             
        HBox hboxContent = new HBox();
        hboxContent.getChildren().addAll(vboxLeft, content);
        vboxLeft.setAlignment(Pos.TOP_CENTER);
        
        root.setCenter(hboxContent);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showError(String message) {
        System.err.println("Error: " + message);
    }

    private void showInfo(String message) {
        System.out.println("Info: " + message);
    }

    private void openHomePage(Stage primaryStage) {
        primaryStage.close();

        Stage homeStage = new Stage();
        Home home = new Home();
        home.start(homeStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
