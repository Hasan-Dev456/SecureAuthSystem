package com.secureauth.ui;

import com.secureauth.models.UserSession;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

public class DashboardView {

    public static Parent create(String username, String role) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        // Standard Universal System Application Header Block
        Label appTitle = new Label("Enterprise Support Center Engine");
        appTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label accountTag = new Label("User Session Identity: " + username + " [" + role + "]");
        accountTag.setStyle("-fx-font-size: 13px; -fx-text-fill: #9ca3af;");

        root.getChildren().addAll(appTitle, accountTag, new Separator());

        // Dynamic Role-Based Layout Routing
        if (role.equalsIgnoreCase("EMPLOYEE")) {
            root.getChildren().add(EmployeeDashboardView.create());
        }
        else if (role.equalsIgnoreCase("TECHNICIAN")) {
            root.getChildren().add(TechnicianDashboardView.create());
        }
        else if (role.equalsIgnoreCase("ADMIN")) {
            // Administrative dashboard card layout or charts panel can hook right here
            Label adminInfo = new Label("Admin Center Panel: Metrics Dashboard Loading...");
            root.getChildren().add(adminInfo);
        }
        else {
            Label unauthorizedMsg = new Label("Security Error: Access Blocked.");
            unauthorizedMsg.setStyle("-fx-text-fill: #ef4444;");
            root.getChildren().add(unauthorizedMsg);
        }

        return root;
    }
}