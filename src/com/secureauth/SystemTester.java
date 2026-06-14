package com.secureauth;

import com.secureauth.controllers.LoginController;
import com.secureauth.controllers.RegisterController;
import com.secureauth.controllers.TicketController;
import com.secureauth.models.User;
import com.secureauth.models.Ticket;

public class SystemTester {
    public static void main(String[] args) {
        System.out.println("=== STARTING HELP DESK SYSTEM INTEGRATION TRACE ===");

        // 1. TEST USER REGISTRATION
        // Let's mock a new employee and technician register sequence
        User mockEmployee = new User("johndoe", "john@company.com", "Password123", "EMPLOYEE");
        User mockTech = new User("tech_ahmed", "ahmed@company.com", "TechPass456", "TECHNICIAN");

        System.out.println("\n--- Testing Registration Layer ---");
        boolean empRegistered = RegisterController.registerUser(mockEmployee);
        boolean techRegistered = RegisterController.registerUser(mockTech);
        System.out.println("Employee Registration Result: " + empRegistered);
        System.out.println("Technician Registration Result: " + techRegistered);


        // 2. TEST AUTHENTICATION LAYER
        System.out.println("\n--- Testing Login Layer ---");
        // Test with correct details
        String loginRoleSuccess = LoginController.loginUser("johndoe", "Password123");
        System.out.println("Login Success Test (Expected 'EMPLOYEE'): " + loginRoleSuccess);

        // Test with invalid password to ensure validation handles failures
        String loginRoleFail = LoginController.loginUser("johndoe", "WrongPassword");
        System.out.println("Login Failure Test (Expected null): " + loginRoleFail);


        // 3. TEST TICKET CREATION LOGIC
        System.out.println("\n--- Testing Ticket Submission Layer ---");
        // We will assume 'johndoe' has a user_id of 1 in a fresh database setup
        Ticket sampleTicket = new Ticket(
                "Office Wi-Fi Down",
                "I cannot connect to the primary local office Wi-Fi network from my laptop.",
                "HIGH",
                1
        );

        boolean ticketCreated = TicketController.createTicket(sampleTicket);
        System.out.println("Ticket Creation Result (Expected true): " + ticketCreated);


        // 4. TEST TICKET WORKFLOW LIFE CYCLE (Claiming & Status Updates)
        System.out.println("\n--- Testing Ticket Workflow State Tracking ---");
        // Assume Ticket ID is 1 and Technician ID is 2 based on insertion ordering
        int targetTicketId = 1;
        int targetTechId = 2;

        // Technician claims the ticket (OPEN -> ASSIGNED)
        boolean ticketClaimed = TicketController.claimTicket(targetTicketId, targetTechId);
        System.out.println("Technician Claim Result (Expected true): " + ticketClaimed);

        // Technician moves ticket to active troubleshooting state (ASSIGNED -> IN_PROGRESS)
        boolean inProgressUpdated = TicketController.updateTicketStatus(
                targetTicketId,
                "IN_PROGRESS",
                targetTechId,
                "Investigating routers and verified the access point configuration logs."
        );
        System.out.println("Move to In-Progress Result: " + inProgressUpdated);

        // Technician resolves the issue (IN_PROGRESS -> RESOLVED)
        boolean resolvedUpdated = TicketController.updateTicketStatus(
                targetTicketId,
                "RESOLVED",
                targetTechId,
                "Updated the outdated wireless firmware on the router. Connection is stable."
        );
        System.out.println("Move to Resolved Result: " + resolvedUpdated);

        System.out.println("\n=== END OF INTEGRATION TEST TRACE ===");
    }
}