package com.secureauth.controllers;

import com.secureauth.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TechnicianDashboardController {

    // Pulls all open or unassigned tickets across the organization
    public static List<String> fetchGlobalQueue() {
        List<String> globalQueue = new ArrayList<>();
        String query = "SELECT ticket_id, title, priority, status FROM tickets WHERE status != 'CLOSED'";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                globalQueue.add(String.format("ID:%d | %s [%s] - (%s)",
                        rs.getInt("ticket_id"),
                        rs.getString("title"),
                        rs.getString("priority"),
                        rs.getString("status")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return globalQueue;
    }
}