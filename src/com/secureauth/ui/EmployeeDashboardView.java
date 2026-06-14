package com.secureauth.ui;

import com.secureauth.controllers.EmployeeDashboardController;
import com.secureauth.controllers.TicketController;
import com.secureauth.models.Ticket;
import com.secureauth.models.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class EmployeeDashboardView {

    public static VBox create() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));

        Label sectionHeader = new Label("My Support Tickets Log");
        sectionHeader.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // List component rendering historical tickets from SQL
        ListView<String> ticketsListView = new ListView<>();
        ticketsListView.getItems().addAll(EmployeeDashboardController.fetchMyTickets());
        ticketsListView.setPrefHeight(150);

        // Ticket Submission Form Area
        Label formTitle = new Label("Create a Support Ticket");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);

        TextField titleField = new TextField();
        titleField.setPromptText("What is breaking?");

        TextArea descField = new TextArea();
        descField.setPromptText("Provide more technical detail...");
        descField.setPrefRowCount(3);

        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("LOW", "MEDIUM", "HIGH", "CRITICAL");
        priorityBox.setValue("MEDIUM");

        formGrid.add(new Label("Issue Summary:"), 0, 0);
        formGrid.add(titleField, 1, 0);
        formGrid.add(new Label("Full Description:"), 0, 1);
        formGrid.add(descField, 1, 1);
        formGrid.add(new Label("Urgency Priority:"), 0, 2);
        formGrid.add(priorityBox, 1, 2);

        Button submitBtn = new Button("Submit Ticket");
        Label statusMsg = new Label();

        submitBtn.setOnAction(e -> {
            if (titleField.getText().isEmpty() || descField.getText().isEmpty()) {
                statusMsg.setText("Error: All fields must be populated.");
                statusMsg.setStyle("-fx-text-fill: #ef4444;");
                return;
            }

            Ticket newTicket = new Ticket(
                    titleField.getText(),
                    descField.getText(),
                    priorityBox.getValue(),
                    UserSession.getUserId()
            );

            if (TicketController.createTicket(newTicket)) {
                statusMsg.setText("Ticket submitted successfully!");
                statusMsg.setStyle("-fx-text-fill: #10b981;");
                titleField.clear();
                descField.clear();

                // Live refresh the log window list immediately
                ticketsListView.getItems().clear();
                ticketsListView.getItems().addAll(EmployeeDashboardController.fetchMyTickets());
            }
        });

        layout.getChildren().addAll(sectionHeader, ticketsListView, new Separator(), formTitle, formGrid, submitBtn, statusMsg);
        return layout;
    }
}