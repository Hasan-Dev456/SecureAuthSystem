package com.secureauth.models;

public class UserSession {
    private static int userId;
    private static String username;
    private static String role;

    // Call this immediately after a successful login authentication check
    public static void startSession(int id, String user, String userRole) {
        userId = id;
        username = user;
        role = userRole;
    }

    // Call this if the user clicks a "Logout" button
    public static void cleanSession() {
        userId = 0;
        username = null;
        role = null;
    }

    public static int getUserId() { return userId; }
    public static String getUsername() { return username; }
    public static String getRole() { return role; }
}