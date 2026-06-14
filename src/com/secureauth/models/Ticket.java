package com.secureauth.models;

import java.sql.Timestamp;

public class Ticket {
    private int ticketId;
    private String title;
    private String description;
    private String priority;
    private String status;
    private int createdBy;
    private Timestamp createdAt;

    // Constructor used when a user is creating a BRAND NEW ticket from the UI form
    public Ticket(String title, String description, String priority, int createdBy) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.createdBy = createdBy;
        this.status = "OPEN"; // Default state for a fresh ticket
    }

    // Constructor used when pulling existing records OUT of the database
    public Ticket(int ticketId, String title, String description, String priority, String status, int createdBy, Timestamp createdAt) {
        this.ticketId = ticketId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    // Getters
    public int getTicketId() { return ticketId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getPriority() { return priority; }
    public String getStatus() { return status; }
    public int getCreatedBy() { return createdBy; }
    public Timestamp getCreatedAt() { return createdAt; }

    // Setters (Useful for mutating status dynamically in the application)
    public void setStatus(String status) { this.status = status; }
}