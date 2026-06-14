package com.secureauth.controllers;

import com.secureauth.database.DatabaseConnection;
import com.secureauth.models.Ticket;
import com.secureauth.models.UserSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDashboardController {

    // Pulls only the tickets created by the logged-in employee
    public static List<String> fetchMyTickets() {
        List<String> userTickets = new ArrayList<>();
        String query = "SELECT ticket_id, title, priority, status FROM tickets WHERE created_by = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, UserSession.getUserId());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                userTickets.add(String.format("#%d %s [%s] - Status: %s",
                        rs.getInt("ticket_id"),
                        rs.getString("title"),
                        rs.getString("priority"),
                        rs.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userTickets;
    }
}