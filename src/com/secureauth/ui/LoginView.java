package com.secureauth.ui;

import com.secureauth.controllers.LoginController;
import com.secureauth.models.UserSession; // Fixed Error 1: Added missing import
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class LoginView {

    public static Parent create(Stage stage) {

        Label title = new Label("Secure Ticket Login");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerPageButton = new Button("Create Account");

        Label message = new Label();
        message.setStyle("-fx-text-fill: #ef4444;");

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (username.isEmpty() || password.isEmpty()) {
                message.setText("Please fill out all fields.");
                return;
            }

            String role = LoginController.loginUser(username, password);

            if (role != null) {
                // Initialize active tracking session context
                UserSession.startSession(1, username, role);

                Scene dashboardScene = new Scene(DashboardView.create(username, role), 650, 550);

                // Fixed Error 2: Changed from getClass() to LoginView.class
                dashboardScene.getStylesheets().add(
                        LoginView.class.getResource("/com/secureauth/styles/style.css").toExternalForm()
                );

                stage.setScene(dashboardScene);
            } else {
                message.setText("Invalid username or password.");
            }
        });

        registerPageButton.setOnAction(e -> {
            Scene registerScene = new Scene(RegisterView.create(stage), 400, 500);

            // Fixed Error 2: Changed from getClass() to LoginView.class
            registerScene.getStylesheets().add(
                    LoginView.class.getResource("/com/secureauth/styles/style.css").toExternalForm()
            );
            stage.setScene(registerScene);
        });

        VBox root = new VBox(15, title, usernameField, passwordField, loginButton, registerPageButton, message);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        return root;
    }
}