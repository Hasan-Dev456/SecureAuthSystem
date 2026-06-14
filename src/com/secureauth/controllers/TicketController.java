package com.secureauth.controllers;

import com.secureauth.database.DatabaseConnection;
import com.secureauth.models.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TicketController {

    // 1. CREATE TICKET (Used by Employees)
    public static boolean createTicket(Ticket ticket) {
        String query = "INSERT INTO tickets (title, description, priority, status, created_by) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, ticket.getTitle());
            preparedStatement.setString(2, ticket.getDescription());
            preparedStatement.setString(3, ticket.getPriority());
            preparedStatement.setString(4, ticket.getStatus());
            preparedStatement.setInt(5, ticket.getCreatedBy());

            preparedStatement.executeUpdate();
            System.out.println("Ticket created successfully!");
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to create ticket.");
            e.printStackTrace();
            return false;
        }
    }

    // 2. CLAIM TICKET (Used by Technicians to change status from OPEN to ASSIGNED)
    public static boolean claimTicket(int ticketId, int technicianId) {
        String assignQuery = "INSERT INTO assignments (ticket_id, technician_id) VALUES (?, ?)";
        String statusQuery = "UPDATE tickets SET status = 'ASSIGNED' WHERE ticket_id = ?";

        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            // Start transaction manually to guarantee atomic database updates
            connection.setAutoCommit(false);

            // Insert claim details into Assignments table
            try (PreparedStatement assignStmt = connection.prepareStatement(assignQuery)) {
                assignStmt.setInt(1, ticketId);
                assignStmt.setInt(2, technicianId);
                assignStmt.executeUpdate();
            }

            // Move the parent Ticket status flag to ASSIGNED
            try (PreparedStatement statusStmt = connection.prepareStatement(statusQuery)) {
                statusStmt.setInt(1, ticketId);
                statusStmt.executeUpdate();
            }

            // Log update history trace
            logTicketUpdate(connection, ticketId, technicianId, "Ticket claimed by Technician.");

            connection.commit();
            System.out.println("Ticket claimed successfully!");
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to claim ticket.");
            if (connection != null) {
                try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try { connection.setAutoCommit(true); connection.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // 3. ADVANCE STATE CYCLE & APPEND WORK LOG (Used for IN_PROGRESS, RESOLVED, CLOSED statuses)
    public static boolean updateTicketStatus(int ticketId, String newStatus, int userId, String workNote) {
        String query = "UPDATE tickets SET status = ? WHERE ticket_id = ?";

        Connection connection = null;
        try {
            connection = DatabaseConnection.connect();
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, newStatus);
                preparedStatement.setInt(2, ticketId);
                preparedStatement.executeUpdate();
            }

            // Record dynamic note entries into History logs tracking table
            logTicketUpdate(connection, ticketId, userId, workNote);

            connection.commit();
            System.out.println("Ticket status updated to " + newStatus);
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to update ticket status.");
            if (connection != null) {
                try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null) {
                try { connection.setAutoCommit(true); connection.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // Private internal helper utility to push trace entries into ticket_updates table
    private static void logTicketUpdate(Connection connection, int ticketId, int updatedBy, String note) throws SQLException {
        String query = "INSERT INTO ticket_updates (ticket_id, updated_by, note) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, ticketId);
            stmt.setInt(2, updatedBy);
            stmt.setString(3, note);
            stmt.executeUpdate();
        }
    }
}