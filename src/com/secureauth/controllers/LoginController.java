package com.secureauth.controllers;

import com.secureauth.database.DatabaseConnection;
import com.secureauth.utils.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    // Fix: Corrected method parameters to take actual credentials strings
    public static String loginUser(String username, String password) {
        // Fix: Target the updated database column 'password_hash'
        String query = "SELECT password_hash, role FROM users WHERE username = ?";

        try {
            Connection connection = DatabaseConnection.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String storedPassword = resultSet.getString("password_hash");
                String role = resultSet.getString("role");

                // Generates SHA-256 of entered text to match stored hash
                String enteredPassword = PasswordHasher.hashPassword(password);

                if (storedPassword.equals(enteredPassword)) {
                    String resetQuery = "UPDATE users SET failed_attempts = 0 WHERE username = ?";
                    PreparedStatement resetStatement = connection.prepareStatement(resetQuery);
                    resetStatement.setString(1, username);
                    resetStatement.executeUpdate();

                    System.out.println("Login Successful!");
                    return role;
                } else {
                    String updateQuery = "UPDATE users SET failed_attempts = failed_attempts + 1 WHERE username = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1, username);
                    updateStatement.executeUpdate();

                    System.out.println("Wrong Password!");
                    return null;
                }
            } else {
                System.out.println("User Not Found!");
                return null;
            }

        } catch (Exception e) {
            System.out.println("Login Failed!");
            e.printStackTrace();
            return null;
        }
    }
}