package com.secureauth.ui;

import com.secureauth.controllers.TechnicianDashboardController;
import com.secureauth.controllers.TicketController;
import com.secureauth.models.UserSession;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class TechnicianDashboardView {

    public static VBox create() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20));

        Label header = new Label("Global IT Incidents Response Queue");
        header.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<String> queueList = new ListView<>();
        queueList.getItems().addAll(TechnicianDashboardController.fetchGlobalQueue());

        HBox actionsContainer = new HBox(10);
        Button claimBtn = new Button("Claim Incident");
        Button progressBtn = new Button("Start Progress");
        Button resolveBtn = new Button("Mark Resolved");

        Label operationsLog = new Label("Select an active item to process operations.");

        // Action helper processing target ID out of the selection string format
        claimBtn.setOnAction(e -> {
            int selectedId = extractIdFromSelection(queueList.getSelectionModel().getSelectedItem());
            if (selectedId != -1) {
                TicketController.claimTicket(selectedId, UserSession.getUserId());
                refreshQueue(queueList, operationsLog, "Incident assigned to your console.");
            }
        });

        progressBtn.setOnAction(e -> {
            int selectedId = extractIdFromSelection(queueList.getSelectionModel().getSelectedItem());
            if (selectedId != -1) {
                TicketController.updateTicketStatus(selectedId, "IN_PROGRESS", UserSession.getUserId(), "Diagnostics active.");
                refreshQueue(queueList, operationsLog, "State machine moved to IN_PROGRESS.");
            }
        });

        resolveBtn.setOnAction(e -> {
            int selectedId = extractIdFromSelection(queueList.getSelectionModel().getSelectedItem());
            if (selectedId != -1) {
                TicketController.updateTicketStatus(selectedId, "RESOLVED", UserSession.getUserId(), "Issue remediated successfully.");
                refreshQueue(queueList, operationsLog, "Ticket marked as RESOLVED.");
            }
        });

        actionsContainer.getChildren().addAll(claimBtn, progressBtn, resolveBtn);
        layout.getChildren().addAll(header, queueList, actionsContainer, operationsLog);
        return layout;
    }

    private static int extractIdFromSelection(String selectedText) {
        if (selectedText == null) return -1;
        try {
            // Parses out the token starting right after "ID:" up to the first space
            String idString = selectedText.split(" ")[0].replace("ID:", "");
            return Integer.parseInt(idString);
        } catch (Exception e) {
            return -1;
        }
    }

    private static void refreshQueue(ListView<String> list, Label feedback, String message) {
        list.getItems().clear();
        list.getItems().addAll(TechnicianDashboardController.fetchGlobalQueue());
        feedback.setText("Log: " + message);
    }
}