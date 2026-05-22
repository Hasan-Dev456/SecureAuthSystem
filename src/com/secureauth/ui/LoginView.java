package com.secureauth.ui;

import com.secureauth.controllers.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class LoginView {

    public static Parent create(Stage stage) {

        Label title = new Label("Secure Login");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button loginButton = new Button("Login");
        Button registerPageButton = new Button("Create Account");

        Label message = new Label();

        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            boolean success = LoginController.loginUser(username, password);

            if (success) {
                Scene dashboardScene = new Scene(DashboardView.create(username), 450, 300);
                dashboardScene.getStylesheets().add(
                        LoginView.class.getResource("/com/secureauth/styles/style.css").toExternalForm()
                );
                stage.setScene(dashboardScene);
            } else {
                message.setText("Invalid username or password");
            }
        });

        registerPageButton.setOnAction(e -> {
            Scene registerScene = new Scene(RegisterView.create(stage), 400, 500);
            registerScene.getStylesheets().add(
                    LoginView.class.getResource("/com/secureauth/styles/style.css").toExternalForm()
            );
            stage.setScene(registerScene);        });

        VBox root = new VBox(15, title, usernameField, passwordField, loginButton, registerPageButton, message);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        return root;
    }
}