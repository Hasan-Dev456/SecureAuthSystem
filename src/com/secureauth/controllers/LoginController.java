package com.secureauth.controllers;

import com.secureauth.database.DatabaseConnection;
import com.secureauth.utils.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    public static boolean loginUser(String username, String password) {

        String query = "SELECT password FROM users WHERE username = ?";

        try {
            Connection connection = DatabaseConnection.connect();

            PreparedStatement preparedStatement =
                    connection.prepareStatement(query);

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String storedPassword = resultSet.getString("password");
                String enteredPassword = PasswordHasher.hashPassword(password);

                if (storedPassword.equals(enteredPassword)) {

                    String resetQuery =
                            "UPDATE users SET failed_attempts = 0 WHERE username = ?";

                    PreparedStatement resetStatement =
                            connection.prepareStatement(resetQuery);

                    resetStatement.setString(1, username);

                    resetStatement.executeUpdate();

                    System.out.println("Login Successful!");

                    return true;
                }else {

                    String updateQuery =
                            "UPDATE users SET failed_attempts = failed_attempts + 1 WHERE username = ?";

                    PreparedStatement updateStatement =
                            connection.prepareStatement(updateQuery);

                    updateStatement.setString(1, username);

                    updateStatement.executeUpdate();

                    System.out.println("Wrong Password!");

                    return false;
                }

            } else {
                System.out.println("User Not Found!");
                return false;
            }

        } catch (Exception e) {
            System.out.println("Login Failed!");
            e.printStackTrace();
            return false;
        }
    }
}