package com.secureauth.ui;

import com.secureauth.controllers.RegisterController;
import com.secureauth.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterView {

    public static Parent create(Stage stage) {

        Label title = new Label("Create Account");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("USER", "ADMIN");
        roleBox.setValue("USER");

        Button registerButton = new Button("Register");
        Button backButton = new Button("Back to Login");

        Label message = new Label();

        backButton.setOnAction(e -> {

            Scene loginScene = new Scene(LoginView.create(stage), 400, 450);

            loginScene.getStylesheets().add(
                    RegisterView.class
                            .getResource("/com/secureauth/styles/style.css")
                            .toExternalForm()
            );

            stage.setScene(loginScene);
        });

        registerButton.setOnAction(e -> {

            User user = new User(
                    usernameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    roleBox.getValue()
            );

            boolean success = RegisterController.registerUser(user);

            if (success) {
                message.setText("Account created successfully!");
            } else {
                message.setText("Registration failed. Username or email already exists.");
            }
        });

        backButton.setOnAction(e -> {
            stage.setScene(new Scene(LoginView.create(stage), 400, 450));
        });

        VBox root = new VBox(15, title, usernameField, emailField, passwordField, roleBox, registerButton, backButton, message);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        return root;
    }
}