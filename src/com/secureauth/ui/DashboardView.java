package com.secureauth.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DashboardView {

    public static Parent create(String username) {

        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label welcome = new Label("Welcome, " + username);
        welcome.setStyle("-fx-font-size: 18px;");

        Label status = new Label("Login successful. Your account is protected.");

        VBox root = new VBox(20, title, welcome, status);
        root.setPadding(new Insets(40));
        root.setAlignment(Pos.CENTER);

        return root;
    }
}