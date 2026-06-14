package com.secureauth.controllers;

import com.secureauth.database.DatabaseConnection;
import com.secureauth.models.User;
import com.secureauth.utils.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class RegisterController {

    public static boolean registerUser(User user) {
        // Fix: Changed column name from 'password' to 'password_hash'
        String query = "INSERT INTO users(username, email, password_hash, role) VALUES (?, ?, ?, ?)";

        try {
            Connection connection = DatabaseConnection.connect();
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getEmail());

            // Keeps your original SHA-256 hashing method intact
            String hashedPassword = PasswordHasher.hashPassword(user.getPassword());
            preparedStatement.setString(3, hashedPassword);
            preparedStatement.setString(4, user.getRole());

            preparedStatement.executeUpdate();
            System.out.println("User Registered Successfully!");
            return true;

        } catch (Exception e) {
            System.out.println("Registration Failed!");
            e.printStackTrace();
            return false;
        }
    }
}