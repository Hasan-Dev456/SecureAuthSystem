package com.secureauth.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/secure_auth_system";

    private static final String USER = "root";

    private static final String PASSWORD = "123456789";

    public static Connection connect() {

        try {

            Connection connection =
                    DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Database Connected Successfully!");

            return connection;

        } catch (Exception e) {

            System.out.println("Connection Failed!");
            e.printStackTrace();

            return null;
        }
    }
}