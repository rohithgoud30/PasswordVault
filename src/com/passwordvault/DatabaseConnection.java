package com.passwordvault;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "rohith123";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            createTables(conn);

            System.out.println("Database connection successful!");
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }

    private static void createTables(Connection conn) throws SQLException {
		try {
			Statement stmt = conn.createStatement();
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    username VARCHAR(100) PRIMARY KEY,
                    user_id INT AUTO_INCREMENT UNIQUE
                )
                """;

            String createCredentialsTable = """
                CREATE TABLE IF NOT EXISTS credentials (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(100) NOT NULL,
                    service_name VARCHAR(100) NOT NULL,
                    login_username VARCHAR(100) NOT NULL,
                    password VARCHAR(100) NOT NULL,
                    FOREIGN KEY (username) REFERENCES users(username) ON DELETE CASCADE
                )
                """;

            stmt.execute(createUsersTable);
            stmt.execute(createCredentialsTable);

            try {
                stmt.execute("ALTER TABLE users ADD COLUMN user_id INT AUTO_INCREMENT UNIQUE");
            } catch (SQLException e) {
            }

            System.out.println("Tables created/verified successfully!");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
            throw e;
        }
    }
}